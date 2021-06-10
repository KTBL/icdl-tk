package de.ktbl.eikotiger.data.mastermodel.instance

import androidx.room.ColumnInfo
import com.google.gson.Gson

class BasicCategory {
    //These fields are required to indentify the branch
    @JvmField
    @ColumnInfo(name = "id")
    var id: Long? = null

    @JvmField
    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "species")
    var species: String? = null

    @JvmField
    @ColumnInfo(name = "description")
    var description: String? = ""
    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}