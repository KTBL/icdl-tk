package de.ktbl.eikotiger.data.json

import de.ktbl.eikotiger.data.icdl.animalcategory.*
import de.ktbl.eikotiger.data.icdl.indicator.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ICDLParserRepository @Inject constructor(
        private val animalCategoryDao: AnimalCategoryDao,
        private val branchDao: BranchDao,
        private val indicatorDao: IndicatorDao,
        private val assessmentDao: AssessmentDao,
        private val composedVarDao: ComposedVarDao,
        private val groupDao: GroupDao,
        private val varDao: VarDao,
) {

    companion object {
        val TAG = ICDLParserRepository::class.simpleName
        val taggedTimber = Timber.tag(TAG)
    }


    suspend fun insertAnimalCategories(animalCategoryBranchMap: Map<AnimalCategory, List<Branch>>): List<AnimalCategoryWithBranches> {
        val animalCategoryWithBranches = mutableListOf<AnimalCategoryWithBranches>()
        val curDate = Date()
        for (animalCategory in animalCategoryBranchMap.keys) {
            animalCategory.inserted = curDate
            val animalCategoryIds = animalCategoryDao.insert(animalCategory)
            if (animalCategoryIds.size != 1) {
                taggedTimber.w("Somehow insertAll did return ${animalCategoryIds.size} ids instead of one Id. Skipping")
                continue
            } else if (animalCategoryIds[0] == -1L) {
                taggedTimber.w("AnimalCategory '${animalCategory.__key}' exists already! Setting to null and Skipping!")
                continue
            }
            animalCategory.id = animalCategoryIds[0]
            val branches = mutableListOf<Branch>()
            for (branch in animalCategoryBranchMap[animalCategory]!!) {
                branch.animalCategoryId = animalCategory.id
                branch.inserted = curDate
                val branchIds = branchDao.insertAll(branch)
                if (branchIds.size != 1) {
                    taggedTimber.w("Somehow insertAll did return ${branchIds.size} ids instead of one Id. Skipping")
                } else {
                    branch.id = branchIds[0]
                    branches.add(branch)
                }
            }
            animalCategoryWithBranches.add(AnimalCategoryWithBranches(animalCategory, branches))
        }
        return animalCategoryWithBranches
    }

    /**
     * TODO Branch-Indicator-Cross-Ref?
     */
    suspend fun insertIndicators(indicators: List<Indicator>) {
        // A list cannot be spread itself. Thus we create an array
        // of the list first
        val toSpreadArray = indicators.toTypedArray()
        val ids = indicatorDao.insert(*toSpreadArray)
        for (i in indicators.indices) {
            indicators[i].id = ids[i]
        }
    }

    suspend fun insertIndicatorBranchCrossRefs(indicatorBranchCrossRefs: List<IndicatorBranchCrossRef>) {
        val toSpreadArray = indicatorBranchCrossRefs.toTypedArray()
        indicatorDao.insertIndicatorBranchCrossRef(*toSpreadArray)
    }

    suspend fun insertCompleteAssessments(indicatorCompleteAssessments: HashMap<Indicator, CompleteAssessment>) {
        for ((indicator, completeAssessment) in indicatorCompleteAssessments) {
            // Field to temporarily hold IDs of newly insert elements
            var id: Long
            // Inserting the top level Assessment itself
            val assessment = completeAssessment.assessment
            assessment.indicatorId = indicator.id
            id = assessmentDao.insert(assessment)
            assessment.id = id
            // Inserting the ComposedVar elements
            for (composedVar in completeAssessment.composedVars) {
                composedVar.assessmentId = assessment.id
                id = composedVarDao.insert(composedVar)
                composedVar.id = id
            }
            // Inserting GroupsWithVars
            for (groupWithVars in completeAssessment.groups) {
                // First the Group
                val group = groupWithVars.group
                group.assessmentId = assessment.id
                id = groupDao.insert(group)
                group.id = id
                // Then the Vars
                for (newVar in groupWithVars.variables) {
                    newVar.groupId = group.id
                    id = varDao.insert(newVar)
                    newVar.id = id
                }
            }
        }
    }
}