package de.ktbl.eikotiger.view.viewmodel.recording.manager

import de.ktbl.eikotiger.data.icdl.indicator.IndicatorWithAssessment
import de.ktbl.eikotiger.data.icdl.indicator.Var
import de.ktbl.eikotiger.view.datainterface.recording.IRecordingBaseManagerDA

class PerAnimalRecordingProcessManager(repository: IRecordingBaseManagerDA) : BaseRecordingProcessManager(
        repository) {

    override suspend fun nextAnimal(selection: List<Var>?): IndicatorWithAssessment {
        initializedCheck()
        createAndAddRecord(selection, currentIndicator.indicator)
        currentIndicatorIndex = 0
        return currentIndicator
    }

    override suspend fun forward(selection: List<Var>?): IndicatorWithAssessment {
        return super.nextIndicator(selection)
    }

    override suspend fun fastForward(selection: List<Var>?): IndicatorWithAssessment {
        return nextAnimal(selection)
    }
}