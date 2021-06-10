package de.ktbl.eikotiger.view.fragment.recording.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.fragment.AbstractBaseFragment
import de.ktbl.eikotiger.R
import de.ktbl.eikotiger.databinding.FragmentRecordingSetupMenuBinding
import de.ktbl.eikotiger.di.Injectable
import de.ktbl.eikotiger.view.viewmodel.RecordingStateVM
import de.ktbl.eikotiger.view.viewmodel.recording.manager.RecordingEnvironment
import de.ktbl.eikotiger.view.viewmodel.recording.setup.RecordingMenuVM
import timber.log.Timber
import javax.inject.Inject

class RecordingSetupMenuFragment : AbstractBaseFragment(), Injectable {

    companion object {
        private val TAG = RecordingSetupMenuFragment::class.java.simpleName
    }

    lateinit var binding: FragmentRecordingSetupMenuBinding

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProvider.Factory

    private val recordingEnvironment: RecordingEnvironment by activityViewModels(this::viewModelProviderFactory)
    private val recordingMenuVM: RecordingMenuVM by viewModels { viewModelProviderFactory }
    private val recordingStateVM: RecordingStateVM by activityViewModels(this::viewModelProviderFactory)

    private val navObserver = Observer { navCommand: NavigationCommand ->
        this.onNavigationRequest(navCommand)
    }


    override val navHostId: Int
        get() = R.id.main_nav_host_fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        this.binding = FragmentRecordingSetupMenuBinding.inflate(inflater, container, false)
        this.binding.lifecycleOwner = viewLifecycleOwner
        recordingMenuVM.observeNavigationRequests(this, this.navObserver)
        recordingMenuVM.load(recordingEnvironment.build(), recordingStateVM::initializeRecording)
        this.binding.vm = recordingMenuVM
        this.binding.recordingEnv = recordingEnvironment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.tag(TAG).d("%d", this.recordingEnvironment.animalCategoryInstanceID.value)
    }
}