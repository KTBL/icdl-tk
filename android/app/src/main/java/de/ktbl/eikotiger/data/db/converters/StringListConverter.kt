package de.ktbl.eikotiger.data.db.converters

import androidx.room.TypeConverter

/**
 * Transforming a List of String to a single string.
 * As delimiter a pipe ("|") is used. This is the case
 * since this symbol cannot be part of a path
 */
class StringListConverter {
    @TypeConverter
    fun fromString(listAsString: String): List<String> {
        return listAsString.split("|")
    }

    @TypeConverter
    fun toString(list: List<String>): String {
        return list.joinToString("|")
    }
}