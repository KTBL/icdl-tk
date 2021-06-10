package de.ktbl.eikotiger.view.fragment.productiondirection.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.adapter.selection.DeletionOnlyActionModeCallback
import de.ktbl.android.sharedlibrary.view.fragment.BaseRecyclerFragment
import de.ktbl.eikotiger.R
import de.ktbl.eikotiger.databinding.FragmentGroupListBinding
import de.ktbl.eikotiger.di.Injectable
import de.ktbl.eikotiger.view.adapter.shared.GroupListItemAdapter
import de.ktbl.eikotiger.view.getDefaultListSpaceItem
import de.ktbl.eikotiger.view.viewmodel.productiondirection.group.GroupListItem
import de.ktbl.eikotiger.view.viewmodel.productiondirection.group.GroupListVM
import timber.log.Timber
import java.util.*
import javax.inject.Inject

open class GroupListFragment : BaseRecyclerFragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentGroupListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: GroupListItemAdapter
    private lateinit var groupListVM: GroupListVM
    override val navHostId: Int
        get() = R.id.main_nav_host_fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Timber.tag(TAG)
                .d("Creating %s view",
                   this.javaClass
                           .simpleName)
        binding = FragmentGroupListBinding.inflate(inflater,
                                                   container,
                                                   false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setBarTitle(R.string.baylist)
        var sectionId: Long? = null
        // TODO switch to getArgs() function
        if (savedInstanceState != null) {
            sectionId = GroupListFragmentArgs.fromBundle(savedInstanceState)
                    .sectionId
            Timber.tag(TAG)
                    .d("Loading savedInstanceState")
        } else {
            if (arguments != null) {
                sectionId = GroupListFragmentArgs.fromBundle(requireArguments())
                        .sectionId
                Timber.tag(TAG)
                        .v("Reading from Arguments %s", sectionId)
            } else {
                Timber.tag(TAG)
                        .e("No fragment args present. Cannot provide Groups without section information!")
            }
        }
        this.groupListVM = ViewModelProvider(this,
                                             this.viewModelFactory).get(GroupListVM::class.java)
        binding.vm = groupListVM
        binding.lifecycleOwner = viewLifecycleOwner
        groupListVM.observeNavigationRequests(viewLifecycleOwner,
                                              { navigationCommand: NavigationCommand? ->
                                                  onNavigationRequest(
                                                          navigationCommand!!)
                                              })
        recyclerView = binding.groupListRecycler
        setupRecyclerView(recyclerView,
                          getDefaultListSpaceItem(this.requireContext()))
        recyclerViewAdapter = GroupListItemAdapter(viewLifecycleOwner)
        recyclerView.adapter = recyclerViewAdapter
        setupSelectionTracker(recyclerView,
                              recyclerViewAdapter,
                              R.plurals.group_count_selected,
                              "group-list-selection",
                              DeletionAMCallback())
        //Specify the requested section (will Trigger Data Load in VM)
        if (sectionId != null) {
            Timber.tag(TAG)
                    .d("Group list view for section %s", sectionId)
            groupListVM.setSectionId(sectionId)
        }

        //changed this to owner, see above - sg
        groupListVM.groupListItems.observe(viewLifecycleOwner,
                                           { updatedList: List<GroupListItem> ->
                                               onRecyclerContentUpdated(
                                                       updatedList)
                                           })
    }

    private fun onRecyclerContentUpdated(updatedList: List<GroupListItem>) {
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
    }

    private inner class DeletionAMCallback : DeletionOnlyActionModeCallback() {
        override fun deleteSelectedItems() {
            val selectedItems = ArrayList<GroupListItem>()
            for (l in selectionTracker.selection) {
                if (l != null) {
                    val position = l.toInt()
                    val item = recyclerViewAdapter.dataset[position]
                    selectedItems.add(item)
                }
            }
            groupListVM.deleteGroups(selectedItems)
            selectionTracker.clearSelection()
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            selectionObserver.clearActionMode()
        }
    }

    companion object {
        private val TAG = GroupListFragment::class.java.simpleName
    }
}