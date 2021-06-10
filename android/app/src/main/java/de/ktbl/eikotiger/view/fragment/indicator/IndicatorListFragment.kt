package de.ktbl.eikotiger.view.fragment.indicator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.fragment.BaseRecyclerFragment
import de.ktbl.eikotiger.R
import de.ktbl.eikotiger.databinding.FragmentIndicatorListBinding
import de.ktbl.eikotiger.di.Injectable
import de.ktbl.eikotiger.view.adapter.shared.IndicatorListItemAdapter
import de.ktbl.eikotiger.view.viewmodel.indicator.IndicatorListItem
import de.ktbl.eikotiger.view.viewmodel.indicator.IndicatorListVM
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class IndicatorListFragment : BaseRecyclerFragment(), Injectable {
    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    private lateinit var binding: FragmentIndicatorListBinding
    private var recyclerView: RecyclerView? = null
    private var recyclerViewAdapter: IndicatorListItemAdapter? = null
    override val navHostId: Int
        get() = R.id.main_nav_host_fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        Timber.tag(TAG)
                .d("Creating %s view",
                   this.javaClass
                           .simpleName)
        //Initialize Binding
        binding = FragmentIndicatorListBinding.inflate(inflater, container, false)
        //set variables in Binding
        return binding.root
        //return inflater.inflate(R.layout.fragment_indicator_list, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val branchID: Long
        var args: IndicatorListFragmentArgs? = null
        if (savedInstanceState != null) {
            args = IndicatorListFragmentArgs.fromBundle(savedInstanceState)
            Timber.tag(TAG)
                    .d("Loading savedInstanceState")
        } else if (arguments != null) {
            args = IndicatorListFragmentArgs.fromBundle(this.requireArguments())
        }
        if (args != null) {
            branchID = args.branchID
        } else {
            Timber.tag(TAG)
                    .e("No fragment args and no savedInstanceState present. Cannot provide indicators without section information!")
            val illegalArgumentException = IllegalArgumentException(
                    "No fragment args and no savedInstanceState present")
            Timber.tag(TAG)
                    .wtf(illegalArgumentException,
                         "No fragment args and no savedInstanceState present. Cannot provide indicators without section information!")
            throw illegalArgumentException
        }
        val vm = ViewModelProvider(this, viewModelFactory!!)
                .get(IndicatorListVM::class.java)
        binding.vm = vm
        val lifecycleOwner = viewLifecycleOwner
        binding.lifecycleOwner = lifecycleOwner
        vm.observeNavigationRequests(lifecycleOwner, { navigationCommand: NavigationCommand? ->
            onNavigationRequest(
                    navigationCommand!!)
        })
        recyclerView = binding.indicatorListRecycler
        recyclerViewAdapter = IndicatorListItemAdapter(ArrayList(),
                                                       viewLifecycleOwner)
        recyclerView!!.adapter = recyclerViewAdapter
        setupRecyclerView(recyclerView, null)



        Timber.tag(TAG)
                .d("Indicatorllist view for branch with ID = $branchID")
        vm.setBranchID(branchID)

        //changed this to owner, see above - sg
        vm.getIndicatorListItems()
                .observe(viewLifecycleOwner)
                { updatedList: List<IndicatorListItem> ->
                    onRecyclerContentUpdated(updatedList)
                }
    }

    private fun onRecyclerContentUpdated(updatedList: List<IndicatorListItem>) {
        if (recyclerViewAdapter != null) {
            for (entry in recyclerViewAdapter!!.dataset) {
                entry.removeNavigationObservers(viewLifecycleOwner)
            }
            for (entry in updatedList) {
                entry.observeNavigationRequests(viewLifecycleOwner,
                                                { navigationCommand: NavigationCommand? ->
                                                    onNavigationRequest(
                                                            navigationCommand!!)
                                                })
            }
            while (recyclerView!!.itemDecorationCount > 0) {
                recyclerView!!.getItemDecorationAt(0)
                recyclerView!!.removeItemDecorationAt(0)
            }
            recyclerViewAdapter!!.dataset = updatedList
        }
    }

    companion object {
        private val TAG = IndicatorListFragment::class.java.simpleName
    }
}