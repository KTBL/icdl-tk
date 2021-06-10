package de.ktbl.eikotiger.view.fragment.recording.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import de.ktbl.android.sharedlibrary.view.activity.BarHider
import de.ktbl.eikotiger.databinding.DialogChooseBranchBinding
import de.ktbl.eikotiger.di.Injectable
import de.ktbl.eikotiger.view.adapter.recording.SimpleAnimalCategoryBranchAdapter
import de.ktbl.eikotiger.view.getDefaultListSpaceItem
import de.ktbl.eikotiger.view.viewmodel.productiondirection.SimpleAnimalCategoryBranchListItem
import de.ktbl.eikotiger.view.viewmodel.recording.manager.RecordingEnvironment
import de.ktbl.eikotiger.view.viewmodel.recording.setup.ChooseAnimalBranchDialogVM
import timber.log.Timber
import javax.inject.Inject

class ChooseAnimalCategoryBranchDialog : DialogFragment(), Injectable {
    companion object {
        val TAG = ChooseAnimalCategoryBranchDialog::class.java.simpleName
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var toolbar: Toolbar
    private lateinit var root: View
    private lateinit var binding: DialogChooseBranchBinding
    private val recordingEnvironment: RecordingEnvironment by activityViewModels(this::viewModelFactory)
    private val chooseAnimalBranchDialogVM: ChooseAnimalBranchDialogVM by viewModels { this.viewModelFactory }
    private lateinit var branchListAdapter: SimpleAnimalCategoryBranchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        this.binding = DialogChooseBranchBinding.inflate(inflater)
        this.root = binding.root
        this.toolbar = binding.chooseBranchToolbar
        val barHider = this.activity as BarHider
        barHider.hideBar()
        return this.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener { Navigation.findNavController(view).popBackStack() }
        binding.lifecycleOwner = viewLifecycleOwner
        setupRecyclerView()
        this.branchListAdapter = SimpleAnimalCategoryBranchAdapter(viewLifecycleOwner)
        binding.branchRecyclerView.adapter = branchListAdapter
        this.chooseAnimalBranchDialogVM.branchItems.observe(this, this::onBranchListUpdate)
        this.recordingEnvironment.animalCategoryInstanceID.observe(this) {
            chooseAnimalBranchDialogVM.instanceID.value = it
        }
    }

    private fun onBranchListUpdate(items: List<SimpleAnimalCategoryBranchListItem>) {
        // First, just to be sure, remove callback-listener of old list-items,
        // event though these elements should never be clicked.
        for (element in branchListAdapter.dataset) {
            element.callback = null
        }

        for (element in items) {
            element.callback = this::onBranchListItemSelected
        }
        branchListAdapter.dataset = items
    }

    private fun onBranchListItemSelected(item: SimpleAnimalCategoryBranchListItem) {
        recordingEnvironment.currentStockId.value = item.stockID
        Navigation.findNavController(root).popBackStack()
    }

    private fun setupRecyclerView() {
        val layoutManager = binding.branchRecyclerView.layoutManager as LinearLayoutManager?
        if (layoutManager != null) {
            val dividerDecoration = getDefaultListSpaceItem(this.requireContext())
            binding.branchRecyclerView.addItemDecoration(dividerDecoration)
        } else {
            Timber.tag(TAG).w("Somehow the layoutmanager is still null")
        }
    }

    override fun onPause() {
        super.onPause()
        val barHider = this.activity as BarHider
        barHider.showBar()
    }


}