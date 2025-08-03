package com.mobdeve.agbuya.hallar.hong.fridge.firestoreHelper

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreHelper (private val context: Context) {

    private val db = FirebaseFirestore.getInstance()
    fun <T> syncToFirestore(
        collectionName: String,
        documentId: String?,
        data: T,
        successMessage: String = "synced $data to firestore",
        failureMessage: String = "failed to sync $data"
    ) {
        //add time stamp of when data is synced
        val dataMap = dataToMap(data)
        dataMap["dataUpdatedAt"] = FieldValue.serverTimestamp()

        if (data != null) {
            if (documentId != null) {
                db.collection(collectionName)
                    .document(documentId)
                    .set(dataMap).addOnSuccessListener {
                        Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "$failureMessage: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun <T> dataToMap(data: T): MutableMap<String, Any?> {
        val json = com.google.gson.Gson().toJson(data)
        return com.google.gson.Gson().fromJson(json, Map::class.java) as MutableMap<String, Any?>
    }

}