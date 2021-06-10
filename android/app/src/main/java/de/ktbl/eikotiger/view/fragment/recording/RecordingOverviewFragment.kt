package de.ktbl.eikotiger.view.fragment.recording

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.fragment.BaseRecyclerFragment
import de.ktbl.eikotiger.R
import de.ktbl.eikotiger.databinding.FragmentRecordingOverviewBinding
import de.ktbl.eikotiger.di.Injectable
import de.ktbl.eikotiger.view.adapter.shared.IndicatorListItemAdapter
import de.ktbl.eikotiger.view.noRecordingActiveQuit
import de.ktbl.eikotiger.view.viewmodel.RecordingStateVM
import de.ktbl.eikotiger.view.viewmodel.recording.RecordingOverviewVM
import javax.inject.Inject

class RecordingOverviewFragment : BaseRecyclerFragment(), Injectable {

    lateinit var binding: FragmentRecordingOverviewBinding

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProvider.Factory

    lateinit var indicatorListItemAdapter: IndicatorListItemAdapter

    private val recordingStateVM: RecordingStateVM by activityViewModels(this::viewModelProviderFactory)


    override val navHostId: Int
        get() = R.id.main_nav_host_fragment


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (recordingStateVM.isRecordingActive.value == false) {
            noRecordingActiveQuit(binding.root, this::onNavigationRequest)
            return
        }
        binding.lifecycleOwner = viewLifecycleOwner
        val vm = ViewModelProvider(
            this,
            viewModelProviderFactory
        ).get(RecordingOverviewVM::class.java)
        vm.load(recordingStateVM.recordingMaster!!)
        indicatorListItemAdapter = IndicatorListItemAdapter(ArrayList(), viewLifecycleOwner)
        binding.indicatorListRecycler.adapter = indicatorListItemAdapter
        binding.vm = recordingStateVM.recordingMasterBindable
        vm.availableIndicatorItems.observe(
            viewLifecycleOwner,
            this::onIndicatorRecyclerContentUpdated
        )

        vm.observeNavigationRequests(viewLifecycleOwner, this::onNavigationRequest)
        recordingStateVM.observeNavigationRequests(viewLifecycleOwner, this::onNavigationRequest)

        binding.changeAnimalGroupBtn.setOnClickListener {
            val action =
                RecordingOverviewFragmentDirections.actionRecordingOverviewFragmentToChooseAnimalGroupDialog()
            this.onNavigationRequest(NavigationCommand(action))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordingOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun onIndicatorRecyclerContentUpdated(updatedList: List<RecordingOverviewIndicatorListItem>) {
        val adapter = binding.indicatorListRecycler.adapter as IndicatorListItemAdapter
        adapter.dataset = updatedList
    }

}