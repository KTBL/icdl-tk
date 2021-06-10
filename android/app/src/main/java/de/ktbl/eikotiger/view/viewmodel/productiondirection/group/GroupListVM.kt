package de.ktbl.eikotiger.view.viewmodel.productiondirection.group

import androidx.lifecycle.*
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.data.mastermodel.location.Location
import de.ktbl.eikotiger.view.datainterface.productiondirection.group.IGroupListDA
import de.ktbl.eikotiger.view.fragment.productiondirection.group.GroupListFragmentDirections
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class GroupListVM @Inject constructor(
        private val repository: IGroupListDA) : AbstractBaseViewModel() {

    var groupListItems: MediatorLiveData<List<GroupListItem>> = MediatorLiveData()

    @JvmField
    var sumAnimalsOverAllGroups: LiveData<Int>

    //Assume we always know the section (and by this the instance and production type)
    private val sectionId: MutableLiveData<Long?> = MutableLiveData()
    private var locationList: LiveData<List<Location>> = MediatorLiveData()
    private fun initLocationList() {
        //Assumes required data is present in database
        Timber.tag(TAG)
                .d("Initiating Location List")
        //make the list change on change of the query string
        locationList = Transformations.switchMap(sectionId) { id: Long? ->
            return@switchMap repository.loadAllLocationsBySectionId(id!!)
        }
    }

    //Set Paramter
    fun setSectionId(sectionId: Long?) {
        Timber.tag(TAG)
                .v("Set to %s", sectionId)
        if (sectionId != null && sectionId != this.sectionId.value) { //filter has changed
            this.sectionId.value = sectionId
        }
    }

    //See below both should do almost the same
    private fun groupsToListItems() {
        //Do in background
        viewModelScope.launch {
            Timber.tag(TAG)
                    .i("Converting groups to title-description-items. %s ",
                       locationList.value)
            val groups = locationList.value
            val listItems: MutableList<GroupListItem> = ArrayList()
            if (groups != null && groups.isNotEmpty()) {
                for (group in groups) {
                    listItems.add(GroupListItem(group) {
                        val action = GroupListFragmentDirections.actionShowGroupDetails(
                            it.id,
                            it.stockId!!
                        )
                        navigationEventHandler.notifyLiveEvent(NavigationCommand(action))
                    })
                    Timber.tag(TAG)
                        .i("Load : %s", group.editLabel)
                    Timber.tag(TAG)
                        .v("Added groupListItem %s", group.editLabel)
                }
            }
            groupListItems.postValue(listItems)
        }
    }

    /**
     * Called when a new instance should be created.
     * This should naviagate to detail view of a new group instace.
     */
    fun onAddFABClicked() {
        if (sectionId.value != null) {
            Timber.tag(TAG)
                    .d("Add new group for %s", sectionId.value)
            val action = GroupListFragmentDirections.actionShowGroupDetails(-1L, sectionId.value!!)
            action.groupId = -1L
            action.sectionId = sectionId.value!!
            navigationEventHandler.notifyLiveEvent(NavigationCommand(action))
        } else {
            Timber.tag(TAG)
                    .e("No section specified!")
        }
    }

    override fun loadDataFromDB(): Boolean {
        // yet not implemented = enforce refresh
        // Actually to enforce a refresh we need to copy the paramters again -sg
        if (sectionId.value != null && sectionId.value!! > 0L) {
            setSectionId(sectionId.value)
            return true
        }
        return false
    }

    fun deleteGroups(itemsToDelete: List<GroupListItem?>) {
        if (itemsToDelete.isNotEmpty()) {
            val deleteMap: List<Location> = itemsToDelete.map { Location(it!!.id) }
            Timber.tag(TAG)
                    .v("Requested to delte %s items", itemsToDelete.size)
            viewModelScope.launch {
                repository.deleteLocationsByIds(deleteMap)
            }
        }
    }

    companion object {
        private val TAG = GroupListVM::class.java.simpleName
    }

    init {  //set section id later
        initLocationList()
        Timber.tag(TAG)
                .d("initiating DisplayListItems")
        //This neatly does the job without requiring tons of logic in Transformation- sg
        groupListItems.addSource(locationList) {
            groupsToListItems()
        }

        // if we ever upgrade to API 24 as Min:
        //this.sumAnimalsOverAllGroups = Transformations.map(locationList, list -> list.stream().mapToInt(loc -> loc.count == null ? 0 : loc.count).sum());
        sumAnimalsOverAllGroups = Transformations.map(locationList) { list: List<Location> ->
            var sum = 0
            for (loc in list) {
                sum += loc.count
            }
            sum
        }
    }
}