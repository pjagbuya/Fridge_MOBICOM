package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.R.id.container_activity_main_fragment
import com.mobdeve.agbuya.hallar.hong.fridge.container.ContainerActivityEditFragment
import com.mobdeve.agbuya.hallar.hong.fridge.container.ContainerActivityFragment

class ContainerActivityEdit  : AppCompatActivity() {

    private lateinit var  containerActivityFragment : ContainerActivityEditFragment
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        containerActivityFragment = ContainerActivityEditFragment()
        setContentView(R.layout.main)
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.container_activity_edit_fragment, containerActivityFragment)
            commit()
        }



    }



}