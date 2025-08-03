package com.mobdeve.agbuya.hallar.hong.fridge


import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Log.d("MyApplication", "Application is running")
    }
}