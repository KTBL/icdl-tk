package de.ktbl.eikotiger.view

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.VerticalSpaceItemDecoration
import de.ktbl.android.sharedlibrary.view.fragment.NavigationRequestHandler
import de.ktbl.eikotiger.MainNavGraphDirections
import de.ktbl.eikotiger.R


/**
 * @param context Android application context
 * @return a new instance of VerticalSpaceItemDecoration using `R.dimen.space_list_items`
 */
fun getDefaultListSpaceItem(context: Context): VerticalSpaceItemDecoration {
    val verticalListSpace = context.resources
            .getDimensionPixelOffset(R.dimen.space_list_items)
    return VerticalSpaceItemDecoration(verticalListSpace)
}

fun noRecordingActiveQuit(root: View, onNavigationRequest: NavigationRequestHandler) {
    Snackbar.make(root,
                  "Aktuell läuft keine Erfassung.",
                  Snackbar.LENGTH_LONG)
            .show()

    val action = MainNavGraphDirections.actionGlobalHomeFragment()
    onNavigationRequest(NavigationCommand(action))
}
