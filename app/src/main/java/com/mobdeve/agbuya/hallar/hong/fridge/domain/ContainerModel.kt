package com.mobdeve.agbuya.hallar.hong.fridge.domain

import android.os.Build
import android.os.Parcelable
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageContainer
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
class ContainerModel(
    var containerId: Int,
    var name : String,
    var imageContainer: ImageContainer,
    var currCap : Int,
    var maxCap : Int,
    var timeStamp : String = getTimeStamp(),
) : Parcelable {
    companion object{
        fun getTimeStamp() : String {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a", Locale.ENGLISH)
                val now = LocalDateTime.now()
                val timeStamp = "Last updated: ${now.format(formatter)}"

                return timeStamp
            }else{

                val formatter = SimpleDateFormat("MMMM dd, yyyy 'at' hh:mm a", Locale.ENGLISH)
                val now = Date()
                val timeStamp = "Last updated: ${formatter.format(now)}"

                return timeStamp

            }

        }
    }
}