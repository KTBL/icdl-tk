package de.ktbl.eikotiger.view.viewmodel.recording

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.data.recordingmodel.location.LocationWithSnapshot
import de.ktbl.eikotiger.view.viewmodel.productiondirection.group.GroupListItem
import de.ktbl.eikotiger.view.viewmodel.recording.manager.RecordingMaster
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChooseAnimalGroupVM @Inject constructor() : AbstractBaseViewModel() {

    private lateinit var recordingMaster: RecordingMaster
    var locationListItems: LiveData<List<GroupListItem>>? = null

    fun loadData(recordingMaster: RecordingMaster) {
        this.recordingMaster = recordingMaster
        val bindable = recordingMaster.bindable
        this.locationListItems =
            Transformations.map(bindable.availableLocations, this::createLocationItems)
    }

    private fun createLocationItems(locations: List<LocationWithSnapshot>): List<GroupListItem> {
        val items = mutableListOf<GroupListItem>()
        for (loc in locations) {
            val newItem = GroupListItem(loc.location) {
                viewModelScope.launch {
                    recordingMaster.setActiveLocation(it.id)
                    navigationEventHandler.postNotifyLiveEvent(NavigationCommand())
                }
            }
            items.add(newItem)
        }
        return items
    }

}