package de.ktbl.eikotiger.view.viewmodel.productiondirection.group

import de.ktbl.android.sharedlibrary.view.adapter.Clickable
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.data.mastermodel.location.Location
import javax.inject.Inject

typealias LocationClickCallback = (Location) -> Unit

class GroupListItem @Inject constructor() : AbstractBaseViewModel(), Clickable {

    lateinit var location: Location
    private var clickEnabled = true
    private lateinit var callback: LocationClickCallback


    constructor(location: Location, callback: LocationClickCallback) : this() {
        this.location = location
        this.callback = callback
    }

    override fun enableClick(enabled: Boolean) {
        clickEnabled = enabled
    }

    fun onClick() {
        if (!clickEnabled) {
            return
        }
        this.callback(location)
    }

    val id: Long
        get() = location.id

    companion object {
        private val TAG = GroupListItem::class.java.simpleName
    }
}