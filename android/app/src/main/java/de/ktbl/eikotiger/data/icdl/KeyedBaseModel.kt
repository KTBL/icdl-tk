package de.ktbl.eikotiger.data.icdl

import androidx.room.ColumnInfo
import de.ktbl.eikotiger.data.BaseModel

open class KeyedBaseModel : BaseModel() {
    @ColumnInfo(name = "__key")
    var __key: String = ""
}