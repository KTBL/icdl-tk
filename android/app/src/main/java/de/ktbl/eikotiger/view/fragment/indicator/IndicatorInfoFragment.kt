package de.ktbl.eikotiger.view.fragment.indicator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import de.ktbl.android.sharedlibrary.view.fragment.AbstractBaseFragment
import de.ktbl.eikotiger.R
import de.ktbl.eikotiger.databinding.FragmentIndicatorInformationBinding
import de.ktbl.eikotiger.di.Injectable
import de.ktbl.eikotiger.view.viewmodel.RecordingStateVM
import de.ktbl.eikotiger.view.viewmodel.indicator.IndicatorInfoVM
import timber.log.Timber
import javax.inject.Inject

enum class IndicatorInfoFragmentModes {
    /**
     * Showing only textual elements of this view.
     * This omits the button to show indicator options
     */
    TEXT_ONLY,

    /**
     * Showing the full view.
     */
    FULL_VIEW
}

/**
 * This Fragment provides an UI to show textual information of an given Indicator.
 * This view can basically run in two modes. See [IndicatorInfoFragmentModes]
 * for further information.
 * This view is intended to be used within the Main-Navigation-Graph to provide
 * details of an indicator chosen within the [IndicatorListFragment] as well
 * as within the Recording-Navigation-Graph to provide information about the currently
 * active indicator.
 */
class IndicatorInfoFragment : AbstractBaseFragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentIndicatorInformationBinding
    override val navHostId: Int
        get() = R.id.main_nav_host_fragment

    private val recordingStateVM: RecordingStateVM by activityViewModels(this::viewModelFactory)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = getArgs(IndicatorInfoFragmentArgs::fromBundle,
                           savedInstanceState)
        if (args == null) {
            Snackbar.make(view,
                          this.getString(R.string.simple_error_msg),
                          Snackbar.LENGTH_LONG)
            Timber.tag(TAG)
                    .wtf("Somehow IndicatorInfoFragment has been called without any argument!")
            return
        }
        if (IndicatorInfoFragmentModes.TEXT_ONLY == args.mode) {
            binding.btnShowClassOptions.visibility = View.GONE
        }
        val vm = ViewModelProvider(this, this.viewModelFactory).get(IndicatorInfoVM::class.java)
        this.binding.vm = vm
        //Why is mock true?
        if (args.indicatorID > 0 || args.mock) {
            vm.loadData(args.indicatorID, args.mock)
        } else {
            Timber.tag(TAG).d("Provided id %s is not a valid id", args.indicatorID)
        }
        vm.indicatorICDL
                .observe(viewLifecycleOwner) {
                    Timber.tag(TAG).d("Refresh Indicator Descriptions for ${it.name}")
                    this.binding.indicatorICDL = it
                    this.setBarTitle(it.name)
                }

        vm.observeNavigationRequests(viewLifecycleOwner, this::onNavigationRequest)
        recordingStateVM.observeNavigationRequests(viewLifecycleOwner, this::onNavigationRequest)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceBundle: Bundle?): View {
        Timber.tag(TAG)
                .d("Creating %s view",
                   this.javaClass
                           .simpleName)
        binding = FragmentIndicatorInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private val TAG = IndicatorInfoFragment::class.java.simpleName
    }
}