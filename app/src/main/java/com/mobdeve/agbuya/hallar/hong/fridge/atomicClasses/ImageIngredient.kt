package com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.MediaStore
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.ImageBitmap
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import kotlinx.parcelize.Parcelize
import java.io.ByteArrayOutputStream

class ImageIngredient(
   private var uri: Uri,
   private var name: String,
   private var context: Context
) {




    fun loadImageView(imageView: ImageView) {
        val iv_context = imageView.context
        if (uri != null) {
            val resId = context.resources.getIdentifier(name, "drawable", context.packageName)
            var context_uri : Uri
            if(resId != 0){
                 context_uri = "android.resource://${iv_context.packageName}/${resId}".toUri()

            }else{
                context_uri = uri
            }
            //imageView.setImageURI(uri) - works but happens in the same thread and might delay especially when image is not located near
            /*
            * Can handle the ff uris:
            * -android.resource://
            * -file://
            * -content://
            * -http(s)://
            */
            Glide.with(iv_context)
                .load(context_uri)
                .into(imageView)
        }
    }

    // Convert URI to Bitmap
    fun getBitmap(): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri!!)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Convert Bitmap to ByteArray (Blob) for storage
    fun getBlob(): ByteArray? {
        val bitmap = getBitmap() ?: return null
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

}

