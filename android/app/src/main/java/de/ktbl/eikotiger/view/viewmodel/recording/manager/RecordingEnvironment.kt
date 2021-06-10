package de.ktbl.eikotiger.view.viewmodel.recording.manager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import de.ktbl.android.sharedlibrary.IllegalAppStateException
import de.ktbl.android.sharedlibrary.util.validator
import de.ktbl.eikotiger.data.mastermodel.instance.Instance
import de.ktbl.eikotiger.data.mastermodel.stock.StockWithBranch
import de.ktbl.eikotiger.util.AbsentLiveData
import de.ktbl.eikotiger.view.datainterface.recording.IRecordingEnvironmentDA
import java.util.*
import javax.inject.Inject

/**
 * Shared ViewModel class representing the recording environment.
 * Purpose of this class is to serve as basic recording configuration,
 * by holding the currently chosen {@link AnimalCategoryInstance} and
 * {@link Branch} or. {@link Stock}
 *
 * This ViewModel is at least shared between:
 *  - Fragments within de.ktbl.eikotiger.view.fragment.recording.setup.*
 *  -
 *
 *  @property animalCategoryInstance
 *  @property animalCategoryInstanceID
 *  @property currentStock
 *  @property currentStockId
 *  @property dateOfRecord
 *  @property isPracticingRecord
 */
class RecordingEnvironment @Inject constructor(
        repository: IRecordingEnvironmentDA
) : ViewModel() {

    val animalCategoryInstanceID = MutableLiveData<Long?>()
    val animalCategoryInstance = Transformations.switchMap(animalCategoryInstanceID) {
        this.currentStockId.value = null
        if (it == null) {
            AbsentLiveData.create()
        } else {
            repository.loadInstanceById(it)
        }
    }

    val currentStockId = MutableLiveData<Long?>()
    val currentStock = Transformations.switchMap(currentStockId) {
        if (it == null) {
            AbsentLiveData.create()
        } else {
            repository.loadStockWithBranch(it)
        }
    }

    val dateOfRecord = MutableLiveData(Date())

    val isPracticingRecord = MutableLiveData(false)

    fun validate() = validator(true) {
        isNotNull(animalCategoryInstance.value, msg = "animalCategoryInstance == null")
        isNotNull(currentStock.value, msg = "currentStock == null")
        isNotNull(dateOfRecord.value, msg = "dateOfRecord == null")
        isNotNull(isPracticingRecord.value, msg = "isPracticingRecord == null")
    }

    fun reset() {

    }

    fun from(protectedRecEnv: ProtectedRecordingEnvironment) {
        this.animalCategoryInstanceID.value = protectedRecEnv.animalCategoryInstance.id
        this.isPracticingRecord.value = protectedRecEnv.isPracticeRecording
        this.dateOfRecord.value = protectedRecEnv.dateOfRecord
        this.currentStockId.value = protectedRecEnv.chosenStock.stock.id

    }

    /**
     *
     */
    fun build(): ProtectedRecordingEnvironment {
        return if (this.validate()) {
            ProtectedRecordingEnvironment(this.animalCategoryInstance.value!!,
                                          this.currentStock.value!!,
                                          this.dateOfRecord.value!!,
                                          this.isPracticingRecord.value!!)
        } else {
            throw IllegalAppStateException()
        }
    }

}

/**
 * Immutable variant of the RecordingEnvironment.
 * Since while the recording is active, nothing of the elements below should be changed, this class
 * is introduced to provide a protected variant.
 * @property animalCategoryInstance
 * @property chosenStock
 * @property dateOfRecord
 * @property isPracticeRecording
 */
data class ProtectedRecordingEnvironment(val animalCategoryInstance: Instance,
                                         val chosenStock: StockWithBranch,
                                         val dateOfRecord: Date,
                                         val isPracticeRecording: Boolean)





