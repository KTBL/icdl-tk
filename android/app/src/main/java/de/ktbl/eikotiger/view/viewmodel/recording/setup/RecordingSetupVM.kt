package de.ktbl.eikotiger.view.viewmodel.recording.setup

import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.view.fragment.productiondirection.ProductionDirectionsOverviewFragmentMode
import de.ktbl.eikotiger.view.fragment.recording.setup.RecordingSetupFragmentDirections
import javax.inject.Inject


class RecordingSetupVM @Inject constructor() : AbstractBaseViewModel() {


    fun onSelectCategoryInstance() {
        val navDir = RecordingSetupFragmentDirections.actionRecordingSetupFragmentToProdDirListFragment(
            ProductionDirectionsOverviewFragmentMode.SELECTION_MODE
        )
        this.navigationEventHandler.notifyLiveEvent(NavigationCommand(navDir))
    }

    fun onSelectBranch() {
        val navDir = RecordingSetupFragmentDirections.actionRecordingSetupFragmentToChooseAnimalCategoryBranchDialog()
        this.navigationEventHandler.notifyLiveEvent(NavigationCommand(navDir))
    }

    fun onContinue() {
        val action = RecordingSetupFragmentDirections.actionRecordingSetupFragmentToRecordingSetupMenuFragment()
        this.navigationEventHandler.notifyLiveEvent(NavigationCommand(action))
    }
}