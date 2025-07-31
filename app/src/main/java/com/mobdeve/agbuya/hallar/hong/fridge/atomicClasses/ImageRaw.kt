package com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Parcelable
import android.widget.ImageView
import kotlinx.parcelize.Parcelize
import java.io.ByteArrayOutputStream

@Parcelize
class ImageRaw(private val blob: ByteArray) : Parcelable {

    // Secondary constructor from Bitmap
    constructor(bitmap: Bitmap) : this(compressBitmap(bitmap))

    fun getBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(blob, 0, blob.size)
    }

    fun loadImageView(imageView: ImageView) {
        imageView.setImageBitmap(getBitmap())
    }

    fun getBlob(): ByteArray {
        return blob
    }

    companion object {
        private fun compressBitmap(bitmap: Bitmap): ByteArray {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            return outputStream.toByteArray()
        }

        fun fromUri(context: Context, uri: Uri): ImageRaw? {
            return try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                BitmapFactory.decodeStream(inputStream, null, options)
                inputStream?.close()

                val scale = calculateInSampleSize(options, 512, 512)

                val inputStream2 = context.contentResolver.openInputStream(uri)
                val scaledOptions = BitmapFactory.Options().apply {
                    inSampleSize = scale
                }

                val bitmap = BitmapFactory.decodeStream(inputStream2, null, scaledOptions)
                inputStream2?.close()

                bitmap?.let { ImageRaw(it) }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            val (height, width) = options.run { outHeight to outWidth }
            var inSampleSize = 1
            if (height > reqHeight || width > reqWidth) {
                val halfHeight = height / 2
                val halfWidth = width / 2
                while ((halfHeight / inSampleSize) >= reqHeight &&
                    (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2
                }
            }
            return inSampleSize
        }
    }
}
