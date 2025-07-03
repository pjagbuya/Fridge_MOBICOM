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
    var bitmap: Bitmap
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


    constructor(bitmap: Bitmap) {
        this.bitmap = bitmap
    }

    // Constructor 2: Load Bitmap from resource ID using Context
    constructor(context: Context, resourceId: Int) {
        this.bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
    }



    // Modern Android Version use below
    constructor(context: Context, uriToPath:String){
        val uri = Uri.parse("android.resource://${context.packageName}/${uriToPath}")


        // For modern versions android 10+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            bitmap = ImageDecoder.decodeBitmap(source)
        }
        // For older versions below android 10
        else {
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }

    }



    fun loadToImage(imageView : ImageView){

        Glide.with(imageView)
            .load(bitmap)
            .into(imageView)
    }

    fun loadToIcon(context: Context, imageView : ImageView){
        Glide.with(context)
            .load(bitmap)
            .circleCrop()
            .into(imageView)
    }
}