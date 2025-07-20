package com.mobdeve.agbuya.hallar.hong.fridge.domain

import android.os.Parcel
import android.os.Parcelable

class RecipeModel(
    var id: Int,
    var name: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<RecipeModel> {
        override fun createFromParcel(parcel: Parcel): RecipeModel = RecipeModel(parcel)
        override fun newArray(size: Int): Array<RecipeModel?> = arrayOfNulls(size)
    }
}
