package de.ktbl.eikotiger.view.viewmodel.productiondirection.group

import androidx.lifecycle.*
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.data.mastermodel.location.Location
import de.ktbl.eikotiger.view.datainterface.productiondirection.group.IGroupDetailsDA
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class GroupDetailVM @Inject constructor(
        private val repository: IGroupDetailsDA
) : AbstractBaseViewModel() {

    //The id of currently active section
    var sectionId: MutableLiveData<Long> = MutableLiveData()

    //The id of current group instance
    private val groupId = MutableLiveData<Long>()

    var location: LiveData<Location> = Transformations.switchMap(groupId) {
        repository.loadLocationByID(it)

    }


//    lateinit var area: MediatorLiveData<Double>
//    lateinit var length: MediatorLiveData<Double>
//    lateinit var width: MediatorLiveData<Double>
//    val isCalculateAreaActive: MediatorLiveData<Boolean> = MediatorLiveData()

    val groupSize = MediatorLiveData<Int>()

    val latestLocationUpdate: LiveData<Date> = Transformations.map(location) {
        getLocationLatestChange(it)
    }

    init {
        setUpBindingLiveData()
    }

    /**
     * We are using a converter for the group size, we are using a backing field within the VM.
     */
    private fun setUpBindingLiveData() {

        val transformCountAnimals = Transformations.map(location) {
            it.count
        }
        groupSize.addSource(transformCountAnimals) { value: Int -> groupSize.setValue(value) }
    }

    /**
     * @param groupId   database ID of the group
     * @param sectionId database ID of the section the group, referenced by groupID, is part of.
     */
    fun loadLocation(groupId: Long, sectionId: Long) {
        if (this.groupId.value != groupId) {
            this.groupId.value = groupId
        }
        this.sectionId.value = sectionId
    }

    /**
     *
     */
    fun createAndLoadLocation(sectionId: Long) {
        viewModelScope.launch {
            val newLocationId = repository.createNewlocationForStock(sectionId)
            if (newLocationId == -1L) {
                Timber.tag(TAG).wtf("No new Location coul be crated, therefore abort loading!")
                return@launch
            }
            this@GroupDetailVM.sectionId.postValue(sectionId)
            this@GroupDetailVM.groupId.postValue(newLocationId)
        }
    }

    /**
     * Stores changes made to the [Location] object currently loaded to the database.
     */
    suspend fun saveLocation() {
        taggedTimber.i("Saving Location")
        val currentLocation = this.location.value
        if (currentLocation == null) {
            taggedTimber.i("Nothing to save.")
            return
        }
        currentLocation.count = this.groupSize.value ?: currentLocation.count
        repository.saveLocation(currentLocation)
    }


    private fun getLocationLatestChange(location: Location): Date {
        return location.updated ?: location.inserted!!
    }

    companion object {
        private val TAG = GroupDetailVM::class.java.simpleName
        private val taggedTimber = Timber.tag(TAG)
    }
}