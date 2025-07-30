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
import com.mobdeve.agbuya.hallar.hong.fridge.R
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.io.ByteArrayOutputStream

@Serializable
@Parcelize
class ImageRaw(private val imageBytes: ByteArray) : Parcelable {

    // create ImageRaw from a Bitmap
    constructor(bitmap: Bitmap) : this(
        ByteArrayOutputStream().apply {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, this)
        }.toByteArray()
    )

    companion object {
        fun getDefaultBitmap(context: Context): Bitmap {
            return BitmapFactory.decodeResource(context.resources, R.mipmap.chef_hat_icon)
        }

        fun extractBitmap(context: Context, uri: Uri): Bitmap? {
            return try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun extractBitmap(byteArray: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    }

    // Load into ImageView
    fun loadImageView(imageView: ImageView) {
        imageView.setImageBitmap(getBitmap())
    }

    // Return the Bitmap directly (from imageBytes)
    fun getBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    // Return ByteArray for storage
    fun getBlob(): ByteArray {
        return imageBytes
    }
}
