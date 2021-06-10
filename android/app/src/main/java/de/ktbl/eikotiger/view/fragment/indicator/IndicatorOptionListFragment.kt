package de.ktbl.eikotiger.view.fragment.indicator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import de.ktbl.android.sharedlibrary.livedata.valueOrDefault
import de.ktbl.android.sharedlibrary.view.fragment.BaseRecyclerFragment
import de.ktbl.eikotiger.R
import de.ktbl.eikotiger.databinding.FragmentIndicatorOptionListBinding
import de.ktbl.eikotiger.di.Injectable
import de.ktbl.eikotiger.view.adapter.shared.ClassificationOptionListAdapter
import de.ktbl.eikotiger.view.noRecordingActiveQuit
import de.ktbl.eikotiger.view.viewmodel.RecordingStateVM
import de.ktbl.eikotiger.view.viewmodel.indicator.ClassificationOptionVM
import de.ktbl.eikotiger.view.viewmodel.indicator.IndicatorOptionListBaseVM
import de.ktbl.eikotiger.view.viewmodel.indicator.IndicatorOptionsOverviewVM
import de.ktbl.eikotiger.view.viewmodel.recording.ClassificationInputVM
import timber.log.Timber
import javax.inject.Inject

enum class IndicatorOptionListFragmentMode {
    /**
     * This mode is used to present an overview of all available options
     * within an Indicator. This mode ignores the Indicator::Assessment::presentation
     * property and simply lists the options without the possibility to make any inputs.
     * This is only for information purposes.
     */
    OVERVIEW,

    /**
     * This mode is used to present an indicator the classification way and receive user
     * inputs.
     */
    INPUT_CLASSIFICATION,
}

/**
 *
 */
class IndicatorOptionListFragment : BaseRecyclerFragment(), Injectable {



    companion object {
        val TAG = IndicatorOptionListFragment::class.java.simpleName
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentIndicatorOptionListBinding
    private lateinit var optionListAdapter: ClassificationOptionListAdapter

    private val recordingStateVM: RecordingStateVM by activityViewModels(this::viewModelFactory)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        this.binding = FragmentIndicatorOptionListBinding.inflate(inflater, container, false)
        this.binding.lifecycleOwner = this
        return this.binding.root
    }

    override val navHostId: Int
        get() = R.id.main_nav_host_fragment


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = this.getArgs(IndicatorOptionListFragmentArgs::fromBundle, savedInstanceState)
        if (args == null) {
            Timber.tag(TAG)
                    .wtf("%s has been created without any args", TAG)
            return
        }

        val vm: IndicatorOptionListBaseVM
        if (args.mode == IndicatorOptionListFragmentMode.OVERVIEW) {
            vm = ViewModelProvider(
                this,
                this.viewModelFactory
            ).get(IndicatorOptionsOverviewVM::class.java)
            vm.loadData(args.indicatorId)
            Timber.tag(TAG)
                .v("Use IndicatorOptionsOverviewVM")

        } else {
            vm = setupClassificationRecording(args.indicatorId)
            Timber.tag(TAG)
                .v("Use ClassificationInputVM")
        }
        this.binding.vm = vm
        vm.observeNavigationRequests(viewLifecycleOwner, this::onNavigationRequest)
        this.optionListAdapter = ClassificationOptionListAdapter(ArrayList(), viewLifecycleOwner)
        this.binding.indicatorOptionList.adapter = this.optionListAdapter
        this.setupRecyclerView(this.binding.indicatorOptionList, null)
        vm.optionItems.observe(this.viewLifecycleOwner,
                               { updateOptionsList ->
                                   if (updateOptionsList != null && updateOptionsList.isNotEmpty()) {
                                       this.onOptionsListUpdated(updateOptionsList)
                                   }
                                   Timber.tag(TAG)
                                           .v("Update UI with %s", updateOptionsList.toString())
                               })
    }

    private fun setupClassificationRecording(indicatorId: Long): IndicatorOptionListBaseVM {
        if (!recordingStateVM.isRecordingActive.valueOrDefault(false)) {
            noRecordingActiveQuit(binding.root, this::onNavigationRequest)
        }

        val vm = ViewModelProvider(this,
                                   this.viewModelFactory).get(ClassificationInputVM::class.java)
        recordingStateVM.observeNavigationRequests(viewLifecycleOwner, this::onNavigationRequest)
        vm.loadData(indicatorId, recordingStateVM.recordingMaster!!)
        vm.observeSnackbarNotificationRequests(viewLifecycleOwner) {
            if (!it.hasBeenHandled) {
                it.hasBeenHandled = true
                Snackbar.make(
                        binding.root,
                        it.text ?: "",
                        it.length
                ).show()
            }
        }

        return vm
    }

    private fun onOptionsListUpdated(updatedOptionsList: List<ClassificationOptionVM>) {
        Timber.tag(TAG)
            .v("Received updated options list")
        for (oldVM in this.optionListAdapter.dataset) {
            oldVM.removeNavigationObservers(viewLifecycleOwner)
        }
        for (newVM in updatedOptionsList) {
            newVM.observeNavigationRequests(viewLifecycleOwner, this::onNavigationRequest)
        }
        this.optionListAdapter.dataset = updatedOptionsList
    }

    override fun onPause() {
        super.onPause()
        binding.vm?.clear()
    }
}