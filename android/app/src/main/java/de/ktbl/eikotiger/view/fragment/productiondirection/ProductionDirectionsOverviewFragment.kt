package de.ktbl.eikotiger.view.fragment.productiondirection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.adapter.selection.DeletionOnlyActionModeCallback
import de.ktbl.android.sharedlibrary.view.fragment.BaseRecyclerFragment
import de.ktbl.eikotiger.R
import de.ktbl.eikotiger.databinding.FragmentDirectionsListBinding
import de.ktbl.eikotiger.di.Injectable
import de.ktbl.eikotiger.view.adapter.main.ProductionDirectionListAdapter
import de.ktbl.eikotiger.view.getDefaultListSpaceItem
import de.ktbl.eikotiger.view.viewmodel.productiondirection.DirAddingModeVM
import de.ktbl.eikotiger.view.viewmodel.productiondirection.DirListModeVM
import de.ktbl.eikotiger.view.viewmodel.productiondirection.DirSelectionModeVM
import de.ktbl.eikotiger.view.viewmodel.productiondirection.ProductionDirectionListItem
import de.ktbl.eikotiger.view.viewmodel.recording.manager.RecordingEnvironment
import timber.log.Timber
import xyz.sangcomz.stickytimelineview.RecyclerSectionItemDecoration.SectionCallback
import xyz.sangcomz.stickytimelineview.TimeLineRecyclerView
import xyz.sangcomz.stickytimelineview.model.SectionInfo
import java.util.*
import javax.inject.Inject

enum class ProductionDirectionsOverviewFragmentMode {
    /**
     * Used in case a single prod.dir.instance has to be selected for further processing.
     * This is for example the case while setting up recording. The selected prod.dir.instance
     * is saved to a shared viewmodel and upon selection a navigation to the last view
     * (top of the backstack) is started.
     */
    SELECTION_MODE,

    /**
     * Used to add new prod.dir.instances based on ICDL components currently available.
     */
    ADDING_MODE,

    /**
     * Lists all prod.dir.instances and upon selection it's details are shown.
     */
    LIST_MODE,
}

class ProductionDirectionsOverviewFragment : BaseRecyclerFragment(), Injectable {


    companion object {
        private val TAG = ProductionDirectionsOverviewFragment::class.java.simpleName
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentDirectionsListBinding
    private lateinit var recyclerView: TimeLineRecyclerView
    private lateinit var recyclerViewAdapter: ProductionDirectionListAdapter
    override val navHostId: Int
        get() = R.id.main_nav_host_fragment
    lateinit var activeMode: ProductionDirectionsOverviewFragmentMode

    /**
     * This ViewModel is loaded lazy, and is only queried in case this fragment is set to
     * SELECTION_MODE
     */
    private val selectionModeSharedViewModel by activityViewModels<RecordingEnvironment>(this::viewModelFactory)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        Timber.tag(TAG)
                .d("Creating %s view",
                   this.javaClass
                           .simpleName)
        //Initialize Binding
        binding = FragmentDirectionsListBinding.inflate(inflater, container, false)
        return binding.root

        //return inflater.inflate(R.layout.fragment_directions_list, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setBarTitle(R.string.animal_catgory_pl)
        Timber.tag(TAG)
                .d("View created, starting setup ...")
        val args = getArgs({ bundle: Bundle? ->
                               ProductionDirectionsOverviewFragmentArgs.fromBundle(
                                       bundle!!)
                           },
                           savedInstanceState)
        activeMode = args?.mode ?: ProductionDirectionsOverviewFragmentMode.LIST_MODE

        //FragmentDirectionsListBinding binding = FragmentDirectionsListBinding.bind(rootView);
        val vm = when (activeMode) {
            ProductionDirectionsOverviewFragmentMode.ADDING_MODE -> {
                ViewModelProvider(this, viewModelFactory).get(DirAddingModeVM::class.java)
            }
            ProductionDirectionsOverviewFragmentMode.LIST_MODE   -> {
                ViewModelProvider(this, viewModelFactory).get(DirListModeVM::class.java)
            }
            else                                                 -> {
                ViewModelProvider(this, viewModelFactory).get(DirSelectionModeVM::class.java)
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = vm
        vm.isMock = args?.mock ?: false

        //this should obeserve request from fab but where do we observe item click callbacks?
        vm.observeNavigationRequests(this, { navigationCommand: NavigationCommand? ->
            onNavigationRequest(
                    navigationCommand!!)
        })
        recyclerView = binding.prodDirListRecycler
        setupRecyclerView(recyclerView, getDefaultListSpaceItem(this.requireContext()))
        recyclerViewAdapter = ProductionDirectionListAdapter(ArrayList(), viewLifecycleOwner)
        recyclerView.adapter = recyclerViewAdapter

        //changed this to owner, see below - sg
        //This is caused by the owner being requested by fragment instead of activity, the java sample code on android github is still broken
        //It's fixed in sdk however: https://medium.com/@BladeCoder/architecture-components-pitfalls-part-1-9300dd969808 - sg
        vm.prodDirListItems.observe(viewLifecycleOwner,
                                    { updatedList: List<ProductionDirectionListItem> ->
                                        onRecyclerContentUpdated(updatedList)
                                    })

        // Only in case this view is not adding_mode we want to enable the selectiontracker.
        if (activeMode == ProductionDirectionsOverviewFragmentMode.LIST_MODE) {
            setupSelectionTracker(
                recyclerView,
                recyclerViewAdapter,
                R.plurals.proddir_count_selected,
                "prod-dir-selection",
                ProdDirDeletionAMCallback()
            )
        }

        if (activeMode == ProductionDirectionsOverviewFragmentMode.SELECTION_MODE) {
            val dirSelectionModeVM = vm as DirSelectionModeVM
            dirSelectionModeVM.recordingEnvironment = selectionModeSharedViewModel
        }
    }

    private fun onRecyclerContentUpdated(updatedList: List<ProductionDirectionListItem>) {
        for (entry in recyclerViewAdapter.dataset) {
            entry.removeNavigationObservers(viewLifecycleOwner)
        }
        for (entry in updatedList) {
            entry.observeNavigationRequests(viewLifecycleOwner,
                                            { navigationCommand: NavigationCommand? ->
                                                onNavigationRequest(
                                                        navigationCommand!!)
                                            })
        }
        while (recyclerView.itemDecorationCount > 0) {
            recyclerView.getItemDecorationAt(0)
            recyclerView.removeItemDecorationAt(0)
        }
        recyclerViewAdapter.dataset = updatedList
        recyclerView.addItemDecoration(getSectionCallback(updatedList))
    }

    private fun getSectionCallback(
            prodDirListItems: List<ProductionDirectionListItem>): SectionCallback {
        return object : SectionCallback {
            /**This determines the headertext after the dot
             *
             * @param position
             * @return the header text if a new category starts
             */
            override fun getSectionHeader(position: Int): SectionInfo? {
                return if (!prodDirListItems.isEmpty() && position >= 0 && position < prodDirListItems.size) {
                    val itemCategory = prodDirListItems[position]
                            .category
                    SectionInfo(itemCategory!!, null, null)
                } else {
                    null
                }
            }

            /**Test if this is start of a new section
             * This will only draw nice sections if list is sorted by category
             *
             * @param position
             * @return true if category changed between the items
             */
            override fun isSection(position: Int): Boolean {
                return if (!prodDirListItems.isEmpty() && position > 0 && position < prodDirListItems.size) {
                    val itemCategory = prodDirListItems[position]
                            .category
                    val previousItemCategory = prodDirListItems[position - 1]
                            .category
                    itemCategory != previousItemCategory
                } else {
                    position == 0
                }
            }
        }
    }

    private inner class ProdDirDeletionAMCallback : DeletionOnlyActionModeCallback() {
        override fun deleteSelectedItems() {
            if (activeMode != ProductionDirectionsOverviewFragmentMode.LIST_MODE) {
                Timber.tag(TAG).wtf("Deletion should only be able in LIST_MODE!")
                return
            }
            val toDelete = ArrayList<ProductionDirectionListItem>()
            for (l in selectionTracker.selection) {
                if (l != null) {
                    val pos = l.toInt()
                    val item = recyclerViewAdapter
                            .dataset[pos]
                    toDelete.add(item)
                }
            }
            val vm = binding.vm as? DirListModeVM?
            vm?.deleteProdDirInstances(toDelete)
            selectionTracker.clearSelection()
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            selectionObserver.clearActionMode()
        }
    }

}