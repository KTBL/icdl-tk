package de.ktbl.eikotiger.data.icdl.indicator

import androidx.room.*
import de.ktbl.eikotiger.data.BaseModel
import de.ktbl.eikotiger.data.json.models.JsonGroup

@Entity(tableName = "Group",
        indices = [
            Index(value = ["assessmentId"]),
            Index(value = ["id"])
        ],
        foreignKeys = [
            ForeignKey(entity = Assessment::class,
                       parentColumns = arrayOf("id"),
                       childColumns = arrayOf("assessmentId"),
                       onDelete = ForeignKey.CASCADE,
                       onUpdate = ForeignKey.CASCADE
            )]
)
class Group : BaseModel() {
    @ColumnInfo(name = "name")
    var name = ""

    @ColumnInfo(name = "assessmentId")
    var assessmentId: Long? = null

    companion object {
        fun fromJsonGroup(jsonGroup: JsonGroup): Group {
            return Group().apply {
                this.name = jsonGroup.name
            }
        }
    }
}

data class GroupWithVars(
        @Embedded val group: Group,
        @Relation(
                entity = Var::class,
                parentColumn = "id",
                entityColumn = "groupId"
        )
        val variables: List<Var>
)