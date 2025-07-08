package com.mobdeve.agbuya.hallar.hong.fridge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Container
import com.mobdeve.agbuya.hallar.hong.fridge.container.ContainerActivityFragment
import com.mobdeve.agbuya.hallar.hong.fridge.container.ContainerDataHelper

class PaulMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)
//        if(savedInstanceState== null){
//            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ContainerActivityFragment())
//                .commit()
//        }


    }
}

