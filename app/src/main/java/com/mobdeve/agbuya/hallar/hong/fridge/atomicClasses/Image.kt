package com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide

class Image {
    private var resId : Int = -1


    fun getResId(): Int {
        return resId
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



    // Constructor 2: Load Bitmap from resource ID using Context
    constructor(context: Context, resourceId: Int) {

        this.resId = resourceId
    }








}