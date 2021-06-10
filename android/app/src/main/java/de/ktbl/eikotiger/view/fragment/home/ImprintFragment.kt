package de.ktbl.eikotiger.view.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.ktbl.android.sharedlibrary.view.fragment.AbstractBaseFragment
import de.ktbl.eikotiger.R
import de.ktbl.eikotiger.databinding.FragmentImprintBinding

class ImprintFragment : AbstractBaseFragment() {
    override val navHostId: Int
        get() = R.id.main_nav_host_fragment

    companion object {
        val TAG = ImprintFragment::class.java
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentImprintBinding.inflate(inflater, container, false).root
    }

}