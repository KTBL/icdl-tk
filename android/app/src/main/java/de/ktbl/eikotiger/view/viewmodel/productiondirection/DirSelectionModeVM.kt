package de.ktbl.eikotiger.view.viewmodel.productiondirection

import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.eikotiger.data.apprepository.AnimalCategoryListRepository
import de.ktbl.eikotiger.view.viewmodel.recording.manager.RecordingEnvironment
import javax.inject.Inject

class DirSelectionModeVM @Inject constructor(dataAccessRepository: AnimalCategoryListRepository) : DirListModeVM(
        dataAccessRepository) {

    lateinit var recordingEnvironment: RecordingEnvironment

    init {
        isFABVisible.value = false
    }

    override fun onListItemSelected(item: ProductionDirectionListItem) {
        recordingEnvironment.animalCategoryInstanceID.value = item.id
        val navCommand = NavigationCommand()
        this.navigationEventHandler.notifyLiveEvent(navCommand)
    }
}