package com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.MediaStore
import android.widget.ImageView
import com.bumptech.glide.Glide
import kotlinx.parcelize.Parcelize
import java.io.ByteArrayOutputStream
@Parcelize
class ImageRaw(private val bitmap: Bitmap): Parcelable {

    companion object{
        fun extractBitmap(context: Context, uri: Uri): Bitmap? {
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
        fun extractBitmap(byteArray: ByteArray): Bitmap{
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    }
    // Load into ImageView using Glide
    fun loadImageView(imageView: ImageView) {
        imageView.setImageBitmap(bitmap)
    }

    // Return the Bitmap directly (if needed)
    fun getBitmap(): Bitmap {
        return bitmap
    }

    // Convert Bitmap to ByteArray (Blob) for storage
    fun getBlob(): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
}