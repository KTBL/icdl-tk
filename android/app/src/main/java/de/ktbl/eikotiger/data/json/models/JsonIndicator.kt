package de.ktbl.eikotiger.data.json.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Class representing an Indicator as provided via JSON
 */
@JsonClass(generateAdapter = true)
class JsonIndicator {

    @Json(name = "__key")
    var __key = ""

    @Json(name = "last-modified")
    var lastModified = ""

    @Json(name = "name")
    var name = ""

    @Json(name = "description")
    var description = ""

    @Json(name = "what-and-why")
    var whatAndWhy = ""

    @Json(name = "when-and-how-often")
    var whenAndHowOften = ""

    @Json(name = "which-and-how-many")
    var whichAndHowMany = ""

    @Json(name = "how")
    var how = ""

    @Json(name = "branches")
    var branches = listOf<String>()

    @Json(name = "sorting")
    var sorting = -1

    @Json(name = "animal-category")
    var animalCategory = ""

    @Json(name = "Assessment")
    var assessment = JsonAssessment()

    @Json(name = "Evaluation")
    var evaluation = JsonEvaluation()

}

/**
 * Class representing an Assessment as provided via JSON.
 * This element appears exactly once in an JsonIndicator.
 */
@JsonClass(generateAdapter = true)
class JsonAssessment {

    @Json(name = "default_type")
    var defaultType = ""

    @Json(name = "type_changeable")
    var typeChangeable = false

    @Json(name = "presentation")
    var presentation = ""

    @Json(name = "presentation_changeable")
    var presentationChangeable = false

    @Json(name = "Options")
    var options = JsonOptions()

}

/**
 * Class representing an Options-element as provided via JSON.
 * This element appears exactly once in an JsonAssessment.
 */
@JsonClass(generateAdapter = true)
class JsonOptions {
    @Json(name = "Group")
    var groups = listOf<JsonGroup>()

    @Json(name = "ComposedVar")
    var composedVars = listOf<JsonComposedVar>()
}

/**
 * Class representing an Group-element as provided via JSON.
 * This element appears 1 to N times in a JsonOptions element.
 */
@JsonClass(generateAdapter = true)
class JsonGroup {
    @Json(name = "Var")
    var variables = listOf<JsonVar>()

    @Json(name = "name")
    var name = ""
}

/**
 * Class representing an Var element as provided via JSON.
 * This element appears 1 to N times in a JsonOptions element.
 */
@JsonClass(generateAdapter = true)
class JsonVar {
    @Json(name = "__key")
    var __key = ""

    @Json(name = "name")
    var name = ""

    @Json(name = "thumbnail")
    var thumbnail = ""

    @Json(name = "short-description")
    var shortDescription = ""

    @Json(name = "images")
    var images = listOf<String>()

    @Json(name = "long-description")
    var longDescription = ""
}

/**
 * Class representing a ComposedVar as provided via JSON.
 * This element appears 0 to N times in a JsonGroup
 */
@JsonClass(generateAdapter = true)
class JsonComposedVar {
    @Json(name = "__key")
    var __key = ""

    @Json(name = "expression")
    var expression = ""
}

/**
 * Class representing an Evaluation element as provided via JSON.
 * This element appears exactly once in an JsonIndicator
 */
@JsonClass(generateAdapter = true)
class JsonEvaluation {
    @Json(name = "Formula")
    var formulas = listOf<JsonFormula>()
}

/**
 * Class representing an Formula as provided via JSON.
 * This element appears 0 to N times in a JsonEvaluation.
 */
@JsonClass(generateAdapter = true)
class JsonFormula {
    @Json(name = "name")
    var name = ""

    @Json(name = "description")
    var description = ""

    @Json(name = "expression")
    var expression = ""

    @Json(name = "Ranges")
    var ranges = listOf<JsonRange>()
}

/**
 * Class representing a Range as provided via JSON.
 * This element appears 0 to N times in a JsonFormula
 */
@JsonClass(generateAdapter = true)
class JsonRange {
    @Json(name = "unit")
    var unit = ""

    @Json(name = "description")
    var description = ""

    @Json(name = "visual-lowerbound")
    var visualLowerBound = 0.0

    @Json(name = "visual-upperbound")
    var visualUpperBound = 100.0

    @Json(name = "TargetRange")
    var targetRange = JsonTargetAlertRange()

    @Json(name = "AlertRange")
    var alertRange = JsonTargetAlertRange()

    @Json(name = "WarningRange")
    var warningRange = JsonWarningRange()

    @JsonClass(generateAdapter = true)
    class JsonTargetAlertRange

    @JsonClass(generateAdapter = true)
    class JsonWarningRange
}