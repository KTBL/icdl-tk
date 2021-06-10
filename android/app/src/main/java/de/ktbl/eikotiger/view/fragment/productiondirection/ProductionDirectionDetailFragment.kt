package de.ktbl.eikotiger.view.fragment.productiondirection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.fragment.BaseRecyclerFragment
import de.ktbl.eikotiger.R
import de.ktbl.eikotiger.databinding.FragmentProdDirDetailsBinding
import de.ktbl.eikotiger.di.Injectable
import de.ktbl.eikotiger.view.adapter.main.AnimalCategoryBranchListAdapter
import de.ktbl.eikotiger.view.getDefaultListSpaceItem
import de.ktbl.eikotiger.view.viewmodel.productiondirection.ProductionDirectionDetailsVM
import de.ktbl.eikotiger.view.viewmodel.productiondirection.ProductionDirectionSectionVM
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ProductionDirectionDetailFragment : BaseRecyclerFragment(), Injectable {
    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null
    private var binding: FragmentProdDirDetailsBinding? = null
    private var sectionRecyclerView: RecyclerView? = null
    private var sectionListAdapter: AnimalCategoryBranchListAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        Timber.tag(TAG)
                .d("Creating %s view",
                   this.javaClass
                           .simpleName)
        binding = FragmentProdDirDetailsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag(TAG)
                .d("Setting up ViewModel and RecyclerView.")
        val viewModel = ViewModelProvider(this, viewModelFactory!!)
                .get(ProductionDirectionDetailsVM::class.java)
        binding!!.vm = viewModel
        viewModel.observeNavigationRequests(this, { navigationCommand: NavigationCommand? ->
            onNavigationRequest(
                    navigationCommand!!)
        })
        sectionRecyclerView = binding!!.sectionlist
        setupRecyclerView(sectionRecyclerView,
                          getDefaultListSpaceItem(requireContext()))
        sectionListAdapter = AnimalCategoryBranchListAdapter(ArrayList(), viewLifecycleOwner)
        sectionRecyclerView!!.adapter = sectionListAdapter
        val lifecycleOwner = viewLifecycleOwner
        viewModel.sectionVMList.observe(lifecycleOwner) { updatedDataset: List<ProductionDirectionSectionVM>? ->
            onSectionListChanged(updatedDataset)
        }
        Timber.tag(TAG)
                .d("Received Bundle %s", arguments)
        val args = getArgs({ bundle: Bundle? ->
                               ProductionDirectionDetailFragmentArgs.fromBundle(
                                       bundle!!)
                           },
                           savedInstanceState)
        val id: Long
        val isCreation: Boolean
        if (args != null) {
            id = args.id
            isCreation = args.create
            if (isCreation) {
                // id is an AnimalCategory ID and the Instance must be created first
                viewModel.createAndLoadInstance(id)
            } else {
                // id is an Instance ID. The Instance must be simply loaded
                viewModel.loadInstance(id)
            }
        } else {
            Timber.tag(TAG).wtf("Somehow args is null, therefore we have nothing to load!")
        }
        binding!!.lifecycleOwner = lifecycleOwner
        viewModel.instanceId.observe(lifecycleOwner) {
            if (it != null) {
                val bundle = Bundle()
                bundle.putLong("prodDirID", it)
                bundle.putBoolean("create", false)
                arguments = bundle
            }
        }
    }

    private fun onSectionListChanged(updatedDataset: List<ProductionDirectionSectionVM>?) {
        Timber.tag(TAG)
                .v("Section List changed")
        for (vm in sectionListAdapter!!.dataset) {
            vm.removeNavigationObservers(this)
        }
        for (vm in updatedDataset!!) {
            vm.observeNavigationRequests(this, { navigationCommand: NavigationCommand? ->
                onNavigationRequest(
                        navigationCommand!!)
            })
        }
        sectionListAdapter!!.dataset = updatedDataset
    }

    override val navHostId: Int
        get() = R.id.main_nav_host_fragment

    /**
     * Store the current userinput on pause
     */
    override fun onPause() {
        super.onPause()
        binding!!.vm?.save()
    }

    companion object {
        val TAG = ProductionDirectionDetailFragment::class.java.simpleName
    }
}