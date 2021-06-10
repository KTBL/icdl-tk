package de.ktbl.eikotiger.view.fragment.recording

import de.ktbl.eikotiger.view.viewmodel.indicator.IndicatorListItem

class RecordingOverviewIndicatorListItem : IndicatorListItem() {

    var onClickCallback: ((indicatorId: Long) -> Unit)? = null

    override fun showDetails() {
        if (!clickEnabled) {
            return
        }
        onClickCallback?.invoke(this.id)
    }
}