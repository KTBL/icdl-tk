package de.ktbl.eikotiger.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import de.ktbl.android.sharedlibrary.view.activity.BarHider
import de.ktbl.eikotiger.R
import de.ktbl.eikotiger.databinding.DialogImageFullscreenBinding
import de.ktbl.eikotiger.di.Injectable
import de.ktbl.eikotiger.view.viewmodel.ImageDialogVM
import javax.inject.Inject

class FullscreenImageDialog : DialogFragment(), Injectable {
    companion object {
        val TAG = FullscreenImageDialog::class.java.simpleName
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var toolbar: Toolbar
    private lateinit var root: View
    private lateinit var binding: DialogImageFullscreenBinding
    private lateinit var vm: ImageDialogVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        this.binding = DialogImageFullscreenBinding.inflate(inflater, container, false)
        this.root = binding.root
        this.toolbar = this.root.findViewById(R.id.imageDialogToolbar)
        val barhider = this.activity as BarHider
        barhider.hideBar()
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return this.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.toolbar.setNavigationOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }
        val imagePath = this.requireArguments().get("imagePath") as String
        this.vm = ViewModelProvider(this, this.viewModelFactory).get(ImageDialogVM::class.java)
        this.vm.imagePath.value = imagePath
        this.binding.lifecycleOwner = viewLifecycleOwner
        this.binding.vm = vm
    }

    override fun onPause() {
        super.onPause()
        val barhider = this.activity as BarHider
        barhider.showBar()
    }

}