package de.ktbl.eikotiger.data.json.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Class representing an AnimalCategory as provided via json.
 */
@JsonClass(generateAdapter = true)
class JsonAnimalCategory {

    @Json(name = "last-modified")
    var lastModified = ""

    @Json(name = "__key")
    var __key = ""

    @Json(name = "name")
    var name = ""

    @Json(name = "species")
    var species = ""

    @Json(name = "assessment_duration")
    var assessmentDuration = -1

    @Json(name = "description")
    var description = ""

    @Json(name = "thumbnail")
    var thumbnail = ""

    @Json(name = "Branch")
    var branches: List<JsonBranch> = listOf()
}

/**
 * Class representing an Branch as provided via json.
 * This element is embedded in a AnimalCategory within json.
 */
@JsonClass(generateAdapter = true)
class JsonBranch {

    @Json(name = "__key")
    var __key = ""

    @Json(name = "name")
    var name = ""

    @Json(name = "description")
    var description = ""

}