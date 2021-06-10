package de.ktbl.eikotiger.view.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.fragment.AbstractBaseFragment
import de.ktbl.eikotiger.R
import de.ktbl.eikotiger.databinding.FragmentBusinessInfoBinding
import de.ktbl.eikotiger.di.Injectable
import de.ktbl.eikotiger.view.viewmodel.settings.BusinessInformationSettingsVM
import timber.log.Timber
import javax.inject.Inject

class BusinessInfoFragment : AbstractBaseFragment(), Injectable {
    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null
    private lateinit var binding: FragmentBusinessInfoBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        Timber.tag(TAG)
                .d("Creating %s view",
                   this.javaClass
                           .simpleName)
        binding = FragmentBusinessInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag(TAG)
                .d("Creating view model")
        val vm = ViewModelProvider(this, viewModelFactory!!)
                .get(BusinessInformationSettingsVM::class.java)
        binding.vm = vm
        val lifecycleOwner = viewLifecycleOwner
        binding.lifecycleOwner = lifecycleOwner
        binding.vm = vm
        //The reason this works without parameter is that the id is constant and the table entry hardcoded
        //if loading this fails - everything is broken
        vm.loadDataFromDB()
        vm.observeNavigationRequests(this, { navigationCommand: NavigationCommand? ->
            onNavigationRequest(
                    navigationCommand!!)
        })
    }

    override fun onPause() {
        super.onPause()
        /**
         * At this point, if the user hasn't saved its data we simply save it - even without
         * asking the user. Data loss is estimated worse than saving unused data.
         */
        binding.vm?.saveDataToDB()
    }

    override val navHostId: Int
        get() = R.id.nav_host_fragment_settings

    companion object {
        val TAG = BusinessInfoFragment::class.java.simpleName
    }


}