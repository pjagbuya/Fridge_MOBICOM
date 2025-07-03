package com.mobdeve.agbuya.hallar.hong.fridge.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel

// connects the android app to the FireBase Realtime Database and retrieves the list of objects
class MainRepository {
//    private val db = FirebaseDatabase.getInstance()
    private val firebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    fun loadContainer(): LiveData<MutableList<ContainerModel>> { // updates UI reactively when data changes in FireBase
        val listData = MutableLiveData<MutableList<ContainerModel>>()
        val ref = firebaseDatabase.getReference("Containers") // must match the one in firebase

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ContainerModel>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(ContainerModel::class.java)
                    item?.let { list.add(it) }
                }
                listData.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Database error: ${error.message}")
            }
        })

        return listData
    }
}
