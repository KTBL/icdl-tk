package de.ktbl.eikotiger.view.viewmodel.recording.manager

import de.ktbl.eikotiger.data.icdl.indicator.IndicatorWithAssessment
import de.ktbl.eikotiger.data.icdl.indicator.Var
import de.ktbl.eikotiger.view.datainterface.recording.IRecordingBaseManagerDA
import timber.log.Timber

class PerIndicatorRecordingProcessManager(repository: IRecordingBaseManagerDA) : BaseRecordingProcessManager(
        repository) {
    companion object {
        val TAG = PerIndicatorRecordingProcessManager::class.simpleName
        val taggedTimber = Timber.tag(TAG)
    }

    override suspend fun nextAnimal(selection: List<Var>?): IndicatorWithAssessment {
        initializedCheck()
        createAndAddRecord(selection, currentIndicator.indicator)
        taggedTimber.i("nextAnimal: nothing to do, we stay at the current indicator")
        return currentIndicator
    }

    override suspend fun forward(selection: List<Var>?): IndicatorWithAssessment {
        initializedCheck()
        createAndAddRecord(selection, currentIndicator.indicator)
        return currentIndicator
    }

    override suspend fun fastForward(selection: List<Var>?): IndicatorWithAssessment {
        return forward(selection)
    }

}