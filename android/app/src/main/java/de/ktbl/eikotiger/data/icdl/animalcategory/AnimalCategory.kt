package de.ktbl.eikotiger.data.icdl.animalcategory

import androidx.room.*
import de.ktbl.eikotiger.data.icdl.KeyedBaseModel
import de.ktbl.eikotiger.data.json.models.JsonAnimalCategory

@Entity(
        tableName = "AnimalCategory",
        indices = [
            Index("__key", unique = true)
        ]
)
class AnimalCategory : KeyedBaseModel() {

    /**
     * Property for [JsonAnimalCategory.species]
     */
    @ColumnInfo(name = "species")
    var species: String? = null

    /**
     * Property for [JsonAnimalCategory.lastModified]
     */
    @ColumnInfo(name = "lastModified")
    var lastModified: String? = null

    /**
     * Property for [JsonAnimalCategory.name]
     */
    @ColumnInfo(name = "name")
    var name: String? = null

    /**
     * Property for [JsonAnimalCategory.description]
     */
    @ColumnInfo(name = "description")
    var description: String? = null

    /**
     * Property for [JsonAnimalCategory.thumbnail]
     */
    @ColumnInfo(name = "thumbnail")
    var thumbnail: String? = null

    /**
     * Property for [JsonAnimalCategory.assessmentDuration]
     */
    @ColumnInfo(name = "assessmentDuration")
    var assessmentDuration: Int? = null

    companion object {
        fun fromJsonAnimalCategory(jsonAnimalCategory: JsonAnimalCategory): AnimalCategory {
            return AnimalCategory().apply {
                species = jsonAnimalCategory.species
                __key = jsonAnimalCategory.__key
                lastModified = jsonAnimalCategory.lastModified
                name = jsonAnimalCategory.name
                thumbnail = jsonAnimalCategory.thumbnail
                assessmentDuration = jsonAnimalCategory.assessmentDuration
                description = jsonAnimalCategory.description
            }
        }
    }

}

data class AnimalCategoryWithBranches(
        @Embedded val animalCategory: AnimalCategory,
        @Relation(
                parentColumn = "id",
                entityColumn = "animalCategoryId"
        )
        val branches: List<Branch>
)