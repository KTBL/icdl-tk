package de.ktbl.eikotiger.data.json

import android.annotation.SuppressLint
import android.content.res.AssetManager
import androidx.annotation.VisibleForTesting
import com.squareup.moshi.Moshi
import de.ktbl.eikotiger.data.Constants.ICDL_FOLDER
import de.ktbl.eikotiger.data.icdl.animalcategory.AnimalCategory
import de.ktbl.eikotiger.data.icdl.animalcategory.AnimalCategoryWithBranches
import de.ktbl.eikotiger.data.icdl.animalcategory.Branch
import de.ktbl.eikotiger.data.icdl.indicator.*
import de.ktbl.eikotiger.data.json.models.JsonIndicator
import de.ktbl.eikotiger.data.json.models.JsonRoot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.Source
import okio.buffer
import okio.source
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject


/**
 * Json parser to be used to parse ICDL data, provided in a json format, and insert it
 * into the app's database.
 *
 * This parser is built upon ICDL-Json as generated using the following icdl-to-json
 * converter:
 *
 */
class ICDLParser @Inject constructor(
        val assetManager: AssetManager,
        val repository: ICDLParserRepository
) {

    companion object {
        val TAG = ICDLParser::class.java.simpleName
        val taggedTimber = Timber.tag(TAG)

    }

    suspend fun parseAndInsertAllAssets() {
        var files: Array<String>? = null
        try {
            files = assetManager.list(ICDL_FOLDER) as Array<String>
        } catch (ioe: IOException) {
            taggedTimber.e(ioe, "Could not read asset list!")
        }
        if (files == null) {
            taggedTimber.e("Since asset list could not be read, returning")
            return
        }

        for (filename in files) {
            val filepath = "$ICDL_FOLDER/$filename"
            taggedTimber.d("Parsing File: $filepath")
            val jsonRoot = parseToJsonModel(filepath)
            if (jsonRoot == null) {
                // Just double checking jsonRoot is really filled now
                taggedTimber.wtf("Something went wrong while reading $filepath, skipping.")
                continue
            }
            retrieveData(jsonRoot)
        }
    }


    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun parseToJsonModel(filepath: String): JsonRoot? {
        val source: Source
        var jsonRoot: JsonRoot? = null
        try {
            // Warning below:
            // Inappropriate blocking method call
            // This is probably a false positive, since this
            // piece of code is already within a Dispatchers.IO
            // context
            val inputStream = assetManager.open(filepath)
            source = inputStream.source()
            val bufferedSource = source.buffer()
            val moshi = Moshi.Builder().build()
            bufferedSource.use {
                val adapter = moshi.adapter(JsonRoot::class.java)
                jsonRoot = adapter.fromJson(bufferedSource)
            }
        } catch (ioe: Exception) {
            taggedTimber.e(ioe, "Error while reading $filepath")
        }
        taggedTimber.v("test")
        return jsonRoot
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun retrieveData(jsonRoot: JsonRoot) = withContext(Dispatchers.Default) {
        taggedTimber.v("(1) Convert Animal Categories")
        val animalCategories = insertAnimalCategories(jsonRoot)
        taggedTimber.v("(2) Convert Indicators")
        val indicators = insertIndicators(jsonRoot, animalCategories)
    }

    /**
     * Converts and then inserts AnimalCategories including the contained Branch elements
     * into the database.
     *
     * @return A map containing AnimalCategory keys as Key and AnimalCategoryWithBranches as value
     */
    private suspend fun insertAnimalCategories(jsonRoot: JsonRoot): HashMap<String, AnimalCategoryWithBranches> {
        val animalCategoryBranchMap = convertAnimalCategories(jsonRoot)
        val animalCategoryWithBranches = repository.insertAnimalCategories(animalCategoryBranchMap)
        val map = HashMap<String, AnimalCategoryWithBranches>()
        for (item in animalCategoryWithBranches) {
            map[item.animalCategory.__key] = item
        }
        return map
    }

    private fun convertAnimalCategories(jsonRoot: JsonRoot): Map<AnimalCategory, List<Branch>> {
        val map = HashMap<AnimalCategory, List<Branch>>()
        for (jsonAnimalCategory in jsonRoot.allAnimalCategories) {
            val animalCategory = AnimalCategory.fromJsonAnimalCategory(jsonAnimalCategory)
            val branches = ArrayList<Branch>()
            for (jsonBranch in jsonAnimalCategory.branches) {
                branches.add(Branch.fromJsonBranch(jsonBranch))
            }
            map[animalCategory] = branches
        }
        taggedTimber.d("Converted ${map.size} AnimalCategory element.")
        return map
    }

    /**
     *
     *
     * TODO: Return Type must be fixed!
     */
    private suspend fun insertIndicators(jsonRoot: JsonRoot,
                                         animalCategories: HashMap<String, AnimalCategoryWithBranches>): Any {
        // Step I: Convert first level of JsonIndicators and insert them
        // Step I/a: Convert them
        val indicators = convertIndicators(jsonRoot.allIndicators, animalCategories)
        // Step I/b: Insert them to get them filled with IDs
        repository.insertIndicators(indicators.values.toList())
        // Step I/c: Next we have to create IndicatorBranchCross References
        val indicatorBranchCrossRefs = createIndicatorBranchCrossRefs(indicators, animalCategories)
        // Step II/d: And insert them into the DB
        repository.insertIndicatorBranchCrossRefs(indicatorBranchCrossRefs)
        // Step II: Convert Assessment and Insert them
        // Step II/a: Convert
        val assessments = convertAssessments(indicators)
        // Step II/b: Insert
        repository.insertCompleteAssessments(assessments)
        // Step III: Convert Evaluation and Insert them
        // Step III/a: Convert

        // Step III/b: Insert

        return indicators
    }

    private fun convertAssessments(indicators: Map<JsonIndicator, Indicator>):
            HashMap<Indicator, CompleteAssessment> {
        val map = HashMap<Indicator, CompleteAssessment>()
        for (jsonIndicator in indicators.keys) {
            val groups = mutableListOf<GroupWithVars>()
            val composedVars = mutableListOf<ComposedVar>()
            // Creating the Assessment itself
            val assessment = Assessment.fromJsonAssessment(jsonIndicator.assessment)
            // Within the Assessment there are Options / Groups of Variables
            val jsonOptions = jsonIndicator.assessment.options
            for (jsonGroup in jsonOptions.groups) {
                val group = Group.fromJsonGroup(jsonGroup)
                val groupVars = mutableListOf<Var>()
                for (jsonVar in jsonGroup.variables) {
                    val newVar = Var.fromJsonVar(jsonVar)
                    groupVars.add(newVar)
                }
                groups.add(GroupWithVars(
                        group,
                        groupVars
                ))
            }
            // And also within the Assessment there are ComposedVar elements
            for (jsonComposedVar in jsonOptions.composedVars) {
                val composedVar = ComposedVar.fromJsonComposedVar(jsonComposedVar)
                composedVars.add(composedVar)
            }
            // Last: add them all together to create the object mapping
            val indicator = indicators[jsonIndicator]!!
            map[indicator] = CompleteAssessment(assessment, groups, composedVars)
        }
        return map
    }

    /**
     * First level conversion of JsonIndicators. => no nested elements are converted!
     * This is done to first insert the Indicator into the DB. This way the Indicator
     * gets a DB id which is latter then used for the nested-objects.
     */
    @SuppressLint("BinaryOperationInTimber")
    private fun convertIndicators(jsonIndicatorList: List<JsonIndicator>,
                                  animalCategories: HashMap<String, AnimalCategoryWithBranches>): Map<JsonIndicator, Indicator> {
        val map = HashMap<JsonIndicator, Indicator>()
        for (jsonIndicator in jsonIndicatorList) {
            val indicator = Indicator.fromJsonIndicator(jsonIndicator)
            val animalCategory = animalCategories[jsonIndicator.animalCategory]?.animalCategory
            if (animalCategory == null) {
                taggedTimber.wtf("No AnimalCategory with Key ${jsonIndicator.animalCategory} " +
                                         "found. Indicator ${jsonIndicator.__key} alias ${jsonIndicator.name} is discarded!")
            } else {
                indicator.animalCategoryId = animalCategory.id
                map[jsonIndicator] = indicator
            }
        }
        return map
    }

    private fun createIndicatorBranchCrossRefs(indicators: Map<JsonIndicator, Indicator>,
                                               animalCategories: Map<String, AnimalCategoryWithBranches>): List<IndicatorBranchCrossRef> {
        val crossRefs = mutableListOf<IndicatorBranchCrossRef>()
        // We are using a Branchmap to quickly map a Branch-key to an ID
        val categoryBranchMap = HashMap<String, HashMap<String, Long>>()
        for ((catKey, category) in animalCategories) {
            val branchMap = HashMap<String, Long>()
            for (branch in category.branches) {
                branchMap[branch.__key] = branch.id
            }
            categoryBranchMap[catKey] = branchMap
        }

        for ((jsonIndicator, indicator) in indicators) {
            val branches = mutableListOf<Long>()
            val category = animalCategories[jsonIndicator.animalCategory]
            if (category == null) {
                taggedTimber.wtf("Indicator referencing not found animal category with key ${jsonIndicator.animalCategory}. Skipping Indicator")
                continue
            }
            if (jsonIndicator.branches.isEmpty()) {
                branches.addAll(category.branches.map { it.id })
            } else {
                // Since we have already successful access the animalCategory above
                // this should never fail! If it does, there's some basic
                // logic flaw
                val branchMap = categoryBranchMap[jsonIndicator.animalCategory]!!
                for (branch in jsonIndicator.branches) {
                    val id = branchMap[branch]
                    if (id == null) {
                        taggedTimber.wtf("No Branch with key $branch found in category ${jsonIndicator.__key} - skipping")
                    } else {
                        branches.add(id)
                    }
                }
            }
            for (id in branches) {
                crossRefs.add(IndicatorBranchCrossRef(id, indicator.id))
            }
        }
        return crossRefs
    }
}