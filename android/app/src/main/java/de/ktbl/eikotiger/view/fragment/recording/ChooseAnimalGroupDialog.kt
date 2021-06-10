package de.ktbl.eikotiger.view.fragment.recording

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import de.ktbl.android.sharedlibrary.view.activity.BarHider
import de.ktbl.eikotiger.databinding.DialogChangeAnimalLocationBinding
import de.ktbl.eikotiger.di.Injectable
import de.ktbl.eikotiger.view.adapter.shared.GroupListItemAdapter
import de.ktbl.eikotiger.view.fragment.recording.setup.ChooseAnimalCategoryBranchDialog
import de.ktbl.eikotiger.view.getDefaultListSpaceItem
import de.ktbl.eikotiger.view.viewmodel.RecordingStateVM
import de.ktbl.eikotiger.view.viewmodel.productiondirection.group.GroupListItem
import de.ktbl.eikotiger.view.viewmodel.recording.ChooseAnimalGroupVM
import timber.log.Timber
import javax.inject.Inject

class ChooseAnimalGroupDialog : DialogFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var barHider: BarHider
    private lateinit var binding: DialogChangeAnimalLocationBinding
    private lateinit var adapter: GroupListItemAdapter
    private val recordingStateVM: RecordingStateVM by activityViewModels(this::viewModelFactory)
    private val chooseAnimalGroupVM: ChooseAnimalGroupVM by viewModels { this.viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogChangeAnimalLocationBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        barHider = this.activity as BarHider
        barHider.hideBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chooseLocationToolbar.setNavigationOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }
        setupRecyclerView()
        this.adapter = GroupListItemAdapter(viewLifecycleOwner)
        binding.locationRecyclerView.adapter = adapter
        chooseAnimalGroupVM.loadData(recordingStateVM.recordingMaster!!)
        chooseAnimalGroupVM.locationListItems!!.observe(
            viewLifecycleOwner,
            this::onLocationListUpdated
        )
        chooseAnimalGroupVM.observeNavigationRequests(viewLifecycleOwner) {
            if (it.hasNotBeenHandled()) {
                it.setHandled()
                val controller = Navigation.findNavController(view)
                if (it.isNavigateBackCommand) {
                    controller.popBackStack()
                } else {
                    controller.navigate(it.navDirection)
                }
            }
        }
    }

    private fun onLocationListUpdated(items: List<GroupListItem>) {
        adapter.dataset = items
    }

    private fun setupRecyclerView() {
        val layoutManager = binding.locationRecyclerView.layoutManager as LinearLayoutManager?
        if (layoutManager != null) {
            val dividerDecoration = getDefaultListSpaceItem(this.requireContext())
            binding.locationRecyclerView.addItemDecoration(dividerDecoration)
        } else {
            Timber.tag(ChooseAnimalCategoryBranchDialog.TAG)
                .w("Somehow the layoutmanager is still null")
        }
    }

    override fun onPause() {
        super.onPause()
        val barHider = this.activity as BarHider
        barHider.showBar()
    }


}