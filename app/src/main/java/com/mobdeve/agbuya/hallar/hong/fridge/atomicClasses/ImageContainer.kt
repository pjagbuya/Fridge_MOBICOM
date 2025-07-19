package com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Parcelable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import kotlinx.android.parcel.Parcelize

@Parcelize
class ImageContainer(
    @DrawableRes
    private var resId : Int,
    private var colorId : Int
) : Parcelable {



    fun getResId(): Int {
        return resId
    }

    fun getColorId(): Int {
        return colorId
    }
    companion object{


        // Static methods

        // Fast load to image, separate thread
        fun LOAD_IMAGE(context: Context, imageView : ImageView, uriToImage: String){
            val uri = Uri.parse("android.resource://${context.packageName}/${uriToImage}")
            //imageView.setImageURI(uri) - works but happens in the same thread and might delay especially when image is not located near
            /*
            * Can handle the ff uris:
            * -android.resource://
            * -file://
            * -content://
            * -http(s)://
            */
            Glide.with(context)
                .load(uri)
                .into(imageView)
        }
        // Default same thread load image, suitable for quick accessible urls
        fun LOAD_IMAGE(context: Context, imageView : ImageView,  resourceId: Int){
            val temp : Bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
            imageView.setImageBitmap(temp)
        }


        fun LOAD_ICON(context: Context, imageView : ImageView, uriToImage: String){
            val uri = Uri.parse("android.resource://${context.packageName}/${uriToImage}")
            //imageView.setImageURI(uri) - works but happens in the same thread and might delay especially when image is not located near
            /*
            * Can handle the ff uris:
            * -android.resource://
            * -file://
            * -content://
            * -http(s)://
            */
            Glide.with(context)
                .load(uri)
                .circleCrop()
                .into(imageView)
        }
    }




    // Default same thread load image, suitable for quick accessible urls
    fun loadImageView(imageView : ImageView){
        val context = imageView.context
        imageView.setImageResource(this.resId)
        imageView.setColorFilter(this.colorId, PorterDuff.Mode.SRC_IN)
    }




    fun swapImageContainer(newId : Int){
        this.resId = newId
    }


}