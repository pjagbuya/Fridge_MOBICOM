package com.mobdeve.agbuya.hallar.hong.fridge


import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        //firebase
        FirebaseApp.initializeApp(this)

        Log.d("MyApplication", "Application is running")
    }
}