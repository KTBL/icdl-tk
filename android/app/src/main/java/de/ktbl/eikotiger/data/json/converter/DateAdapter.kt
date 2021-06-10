package de.ktbl.eikotiger.data.json.converter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*

class DateAdapter {
    @ToJson
    fun toJson(date: Date) = date.time

    @FromJson
    fun fromJson(ts: Long) = Date(ts)
}