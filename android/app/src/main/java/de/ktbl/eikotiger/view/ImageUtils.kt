package de.ktbl.eikotiger.view

import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import timber.log.Timber

fun loadImageFromAsset(assetPath: String?, assetManager: AssetManager): Drawable? {
    if (assetPath.isNullOrBlank()) {
        return null
    }
    return try {
        assetManager.open(assetPath).use { iStream ->
            Drawable.createFromStream(iStream, assetPath)
        }
    } catch (exception: Exception) {
        Timber.tag("ImageUtils::loadImage").w(exception,
                                              "An exception has been thrown while loading a drawable from asset at $assetPath")
        null
    }
}