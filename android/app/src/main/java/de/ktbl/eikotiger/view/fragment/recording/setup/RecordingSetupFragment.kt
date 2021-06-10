package de.ktbl.eikotiger.view.fragment.recording.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.fragment.AbstractBaseFragment
import de.ktbl.eikotiger.R
import de.ktbl.eikotiger.databinding.FragmentRecordingSetupBinding
import de.ktbl.eikotiger.di.Injectable
import de.ktbl.eikotiger.view.viewmodel.recording.manager.RecordingEnvironment
import de.ktbl.eikotiger.view.viewmodel.recording.setup.RecordingSetupVM
import java.util.*
import javax.inject.Inject

class RecordingSetupFragment : AbstractBaseFragment(), Injectable {

    companion object {
        @Suppress("unused")
        private val TAG = RecordingSetupFragment::class.java.simpleName
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var binding: FragmentRecordingSetupBinding
    val navObserver = Observer { navCommand: NavigationCommand ->
        this.onNavigationRequest(navCommand)
    }

    private val recordingEnvironment: RecordingEnvironment by activityViewModels(this::viewModelFactory)


    override val navHostId: Int
        get() = R.id.main_nav_host_fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        this.binding = FragmentRecordingSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val vm = ViewModelProvider(this, this.viewModelFactory).get(RecordingSetupVM::class.java)
        vm.observeNavigationRequests(this, this.navObserver)
        this.binding.lifecycleOwner = viewLifecycleOwner
        this.binding.vm = vm
        this.binding.fragment = this
        this.binding.sharedVM = recordingEnvironment
    }

    fun onChooseCalendar() {
        val datePicker = MaterialDatePicker.Builder
                .datePicker()
                .setTitleText("Auf welchen Tag bezieht sich die Erfassung?")
                .build()
        datePicker.addOnPositiveButtonClickListener {
            val date = Date()
            date.time = it
            this.binding.sharedVM?.dateOfRecord?.value = date
        }
        datePicker.show(this.parentFragmentManager, datePicker.tag)
    }
}