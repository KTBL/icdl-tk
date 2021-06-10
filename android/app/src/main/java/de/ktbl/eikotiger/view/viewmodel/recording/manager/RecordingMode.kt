package de.ktbl.eikotiger.view.viewmodel.recording.manager

/**
 * Defines possible RecordingModes and constants in regard to the modes
 */
enum class RecordingMode(val flag: Int, val isTestMode: Boolean) {
    /**
     * "Tierweise Erfassung"
     */
    BY_ANIMAL(1, false),

    /**
     * "Tierweise Erfassung" for testing / education purposes
     */
    BY_ANIMAL_TEST(2, true),

    /**
     * "Indikatorweise Erfassung"
     */
    BY_INDICATOR(4, false),

    /**
     * "Indikatorweise Erfassung" for testing / education purposes
     */
    BY_INDICATOR_TEST(8, true),

    /**
     * "Erfassung von Büroindikatoren"
     */
    OFFICE_INDICATORS(16, false),

    /**
     * Erfassung von Büroindikatoren" for testing / education purposes
     */
    OFFICE_INDICATORS_TEST(32, true);

    fun toTestMode(): RecordingMode = when (this) {
        BY_ANIMAL -> BY_ANIMAL_TEST
        BY_INDICATOR -> BY_INDICATOR_TEST
        OFFICE_INDICATORS -> OFFICE_INDICATORS_TEST
        else              -> this
    }

}