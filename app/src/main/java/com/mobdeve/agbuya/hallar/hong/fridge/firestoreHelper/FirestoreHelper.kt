package com.mobdeve.agbuya.hallar.hong.fridge.firestoreHelper

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.agbuya.hallar.hong.fridge.converters.toFirestoreContainer
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity

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
    fun updateContainer(
        container: ContainerEntity,
        successMessage: String = "Container updated!",
        failureMessage: String = "Failed to update container"
    ) {
        val docId = container.containerId.toString()
        val dataMap = dataToMap(container.toFirestoreContainer())
        dataMap["dataUpdatedAt"] = FieldValue.serverTimestamp()

        db.collection("containers")
            .document(docId)
            .update(dataMap)  // <─ Only update fields that changed
            .addOnSuccessListener {
                Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "$failureMessage: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun syncContainerList(
        containers: List<ContainerEntity>,
        onComplete: (() -> Unit)? = null
    ) {
        val batch = db.batch()
        val collectionRef = db.collection("containers")

        containers.forEach { container ->
            val firestoreContainer = container.toFirestoreContainer()
            val dataMap = dataToMap(firestoreContainer)
            dataMap["dataUpdatedAt"] = FieldValue.serverTimestamp()

            val docRef = collectionRef.document(container.containerId.toString())
            batch.set(docRef, dataMap)
        }

        batch.commit()
            .addOnSuccessListener {
                Toast.makeText(context, "Synced containers", Toast.LENGTH_SHORT).show()
                onComplete?.invoke()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to sync containers: ${e.message}", Toast.LENGTH_SHORT).show()
                onComplete?.invoke()
            }
    }

    fun deleteFromFirestore(
        collectionName: String,
        documentId: String,
        successMessage: String = "Document deleted successfully", // Consider if Toast is desired for delete
        failureMessage: String = "Failed to delete document"      // Consider if Toast is desired for delete
    ) {
        val TAG = "FirestoreHelper" // Or use a companion object constant

        if (documentId.isEmpty()) {
            Log.w(TAG, "deleteFromFirestore called with empty documentId for collection $collectionName")
            // Optionally show a Toast or handle the error
            // Toast.makeText(context, "Cannot delete: Invalid document ID", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection(collectionName)
            .document(documentId)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "Successfully deleted document: $documentId from collection: $collectionName")
                // Show success Toast if desired
                Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to delete document: $documentId from collection: $collectionName", exception)
                // Show failure Toast if desired
                Toast.makeText(context, "$failureMessage: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun <T> dataToMap(data: T): MutableMap<String, Any?> {
        val json = com.google.gson.Gson().toJson(data)
        return com.google.gson.Gson().fromJson(json, Map::class.java) as MutableMap<String, Any?>
    }

}