package com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageUri(
    val uriString: String? = null,
    val resId: Int? = null
) : Parcelable {

    fun isUri(): Boolean = uriString != null

    fun isResource(): Boolean = resId != null

    fun getUri(context: Context): Uri? {
        return when {
            uriString != null -> Uri.parse(uriString)
            resId != null -> Uri.parse("android.resource://${context.packageName}/$resId")
            else -> null
        }
    }
}