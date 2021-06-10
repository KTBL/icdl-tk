package de.ktbl.eikotiger.data.json.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Class to be used to parse ICDL Json files.
 * This class is currently written in Java, since every other
 * to be parsed class is also in Java. Together with the other ones
 * this will be translated to kotlin in the near future - hopefully
 */
@JsonClass(generateAdapter = true)
class JsonRoot {
    @Json(name = "AnimalCategory")
    var allAnimalCategories: List<JsonAnimalCategory> = listOf()

    @Json(name = "Indicator")
    var allIndicators: List<JsonIndicator> = listOf()
}