package de.ktbl.eikotiger.view.fragment.productiondirection.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.fragment.AbstractBaseFragment
import de.ktbl.eikotiger.R
import de.ktbl.eikotiger.databinding.FragmentGroupDetailsBinding
import de.ktbl.eikotiger.di.Injectable
import de.ktbl.eikotiger.view.viewmodel.productiondirection.group.GroupDetailVM
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class GroupDetailFragment : AbstractBaseFragment(), Injectable {

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    private lateinit var binding: FragmentGroupDetailsBinding
    override val navHostId: Int
        get() = R.id.main_nav_host_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setBarTitle(R.string.baydetails)
        Timber.tag(TAG)
                .d("Creating view model")
        val vm = ViewModelProvider(this, viewModelFactory!!).get(
                GroupDetailVM::class.java)
        var groupId: Long? = null
        var sectionId: Long? = null
        if (arguments != null) {
            groupId = GroupDetailFragmentArgs.fromBundle(requireArguments())
                    .groupId
            sectionId = GroupDetailFragmentArgs.fromBundle(requireArguments())
                    .sectionId
        }
        val lifecycleOwner = viewLifecycleOwner
        binding.lifecycleOwner = lifecycleOwner
        binding.vm = vm
        //Here we need two parameters as they can't be inferred from one another apart from some special cases
        vm.observeNavigationRequests(lifecycleOwner, { navigationCommand: NavigationCommand? ->
            onNavigationRequest(
                    navigationCommand!!)
        })
        if (groupId == -1L) {
            vm.createAndLoadLocation(sectionId!!)
        } else {
            vm.loadLocation(groupId!!, sectionId!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceBundle: Bundle?): View {
        Timber.tag(TAG)
                .d("Creating %s view",
                   this.javaClass
                           .simpleName)
        binding = FragmentGroupDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Store the current user input on pause
     */
    override fun onPause() {
        super.onPause()
        if (this::binding.isInitialized) {
            // in case nothing has been initialized, there is
            // nothing to save
            viewLifecycleOwner.lifecycleScope.launch {
                binding.vm?.saveLocation()
            }
        }
    }

    companion object {
        val TAG = GroupDetailFragment::class.java.simpleName
    }
}