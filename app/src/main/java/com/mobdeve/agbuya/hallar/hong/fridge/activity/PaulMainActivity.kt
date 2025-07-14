package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.R.id.container_activity_main_fragment
import com.mobdeve.agbuya.hallar.hong.fridge.container.ContainerActivityFragment

class PaulMainActivity : AppCompatActivity() {

    private lateinit var  containerActivityFragment : ContainerActivityFragment
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)


        containerActivityFragment = ContainerActivityFragment()
        supportFragmentManager.beginTransaction().apply{
            replace(container_activity_main_fragment, containerActivityFragment)
            commit()
        }


    }



}