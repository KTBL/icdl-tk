package de.ktbl.eikotiger.view.fragment.indicator

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import de.ktbl.android.sharedlibrary.view.fragment.BaseRecyclerFragment
import de.ktbl.eikotiger.R
import de.ktbl.eikotiger.databinding.FragmentIndicatorOptionDetailsBinding
import de.ktbl.eikotiger.di.Injectable
import de.ktbl.eikotiger.view.adapter.shared.ImageItemAdapter
import de.ktbl.eikotiger.view.viewmodel.ImageItem
import de.ktbl.eikotiger.view.viewmodel.indicator.IndicatorOptionDetailsVM
import timber.log.Timber
import javax.inject.Inject

/**
 * Based on: https://developer.android.com/training/animation/zoom
 */
class IndicatorOptionDetailsFragment : BaseRecyclerFragment(), Injectable {

    companion object {
        private val TAG = IndicatorOptionDetailsFragment::class.java.simpleName
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var currentAnimator: Animator? = null
    private var shortAnimationDuration: Int = 0
    private lateinit var binding: FragmentIndicatorOptionDetailsBinding
    override val navHostId: Int
        get() = R.id.main_nav_host_fragment
    private lateinit var imageItemsListAdapter: ImageItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Timber.tag(TAG).d("Creating %s view", TAG)
        this.shortAnimationDuration = this.resources.getInteger(R.integer.config_shortAnimTime)
        this.binding = FragmentIndicatorOptionDetailsBinding.inflate(inflater, container, false)
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = getArgs(IndicatorOptionDetailsFragmentArgs::fromBundle, savedInstanceState)
        if (args == null) {
            Snackbar.make(view, "Ups.. da ist etwas schief gelaufen", Snackbar.LENGTH_LONG)
            Timber.tag(TAG).wtf("Somehow IndicatorOptionDetailsFragment has been called without args")
            return
        }

        this.imageItemsListAdapter = ImageItemAdapter(ArrayList(), viewLifecycleOwner)
        this.binding.optionDetailsImages.adapter = this.imageItemsListAdapter
        this.binding.lifecycleOwner = viewLifecycleOwner
        this.setupRecyclerView(this.binding.optionDetailsImages, null)

        val vm = ViewModelProvider(this,
                                   this.viewModelFactory).get(IndicatorOptionDetailsVM::class.java)
        this.binding.vm = vm
        vm.imageItemList.observe(this.viewLifecycleOwner,
                                 { updatedImagesList ->
                                     this.onImagesListUpdated(updatedImagesList)
                                 })

        vm.description.observe(this.viewLifecycleOwner, {
            if (it != null) {
                Timber.tag(TAG).d("Refresh Option Descriptions for %s",
                                  args.optionId)
                this.setBarTitle(it)

            } else {
                Timber.tag(TAG).d("No data for %s", args.optionId)
                this.setBarTitle("")
            }
        })

        if (args.optionId > 0 || args.mock) {
            vm.loadData(args.optionId, args.mock)
        } else {
            Timber.tag(TAG).d("Provided id %s is not a valid id",
                              args.optionId)
        }
    }

    private fun onImagesListUpdated(updatedImagesList: List<ImageItem>?) {
        Timber.tag(TAG).v("Received updated ImageItem list")
        if (updatedImagesList == null) {
            Timber.tag(TAG).i("onImagesListUpdated called with null list")
            return
        }
        for (oldVM in this.imageItemsListAdapter.dataset) {
            oldVM.removeNavigationObservers(this)
        }
        for (newVM in updatedImagesList) {
            newVM.observeNavigationRequests(this, { navCommand ->
                this.onNavigationRequest(navCommand)
            })


        }
        this.imageItemsListAdapter.dataset = updatedImagesList
    }


}