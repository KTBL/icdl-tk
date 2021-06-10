package de.ktbl.eikotiger.data.icdl.indicator

import androidx.room.*
import de.ktbl.eikotiger.data.db.converters.StringListConverter
import de.ktbl.eikotiger.data.icdl.KeyedBaseModel
import de.ktbl.eikotiger.data.json.models.JsonVar

@Entity(tableName = "Var",
        indices = [
            Index("id"),
            Index("groupId")
        ],
        foreignKeys = [
            ForeignKey(
                    entity = Group::class,
                    parentColumns = arrayOf("id"),
                    childColumns = arrayOf("groupId"),
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE
            )
        ]
)
@TypeConverters(StringListConverter::class)
class Var : KeyedBaseModel() {
    @ColumnInfo(name = "name")
    var name = ""

    @ColumnInfo(name = "thumbnail")
    var thumbnail = ""

    @ColumnInfo(name = "images")
    var images: List<String> = listOf()

    @ColumnInfo(name = "shortDescription")
    var shortDescription = ""

    @ColumnInfo(name = "longDescription")
    var longDescription = ""

    @ColumnInfo(name = "groupId")
    var groupId: Long? = null

    companion object {
        fun fromJsonVar(jsonVar: JsonVar): Var {
            return Var().apply {
                name = jsonVar.name
                thumbnail = jsonVar.thumbnail
                images = jsonVar.images
                shortDescription = jsonVar.shortDescription
                longDescription = jsonVar.longDescription
            }
        }
    }
}