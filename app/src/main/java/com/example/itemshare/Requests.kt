package com.example.itemshare

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

@Composable
fun Requests(userEmail: String, modifier: Modifier = Modifier)
{
    val database = Firebase.firestore
    val storage = Firebase.storage

    Column(modifier) {

        var itemName by remember { mutableStateOf("") }
        var itemSummary by remember { mutableStateOf("") }
        var imageUri by remember { mutableStateOf<Uri?>(null) }

        val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent())
        { uri: Uri? ->
            imageUri = uri
        }

        Row {
            OutlinedTextField(
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("Enter item name") },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Row {
            OutlinedTextField(
                value = itemSummary,
                onValueChange = { itemSummary = it },
                label = { Text("Enter item description") },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Row {
            Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text("Select Image")
            }
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Selected image",
                    modifier = Modifier.size(100.dp)
                )
            }
        }
        Row {
            Button(
                onClick = {
                    val currentImageUri = imageUri
                    if (currentImageUri == null) {
                        // No image, just add the item data to Firestore
                        val itemData = mapOf(
                            "listingName" to itemName,
                            "listingSummary" to itemSummary,
                            "owner" to userEmail
                        )
                        database.collection("itemsAvail").add(itemData)
                            .addOnSuccessListener {
                                Log.d("Requests", "Item added successfully")
                                // Clear fields
                                itemName = ""
                                itemSummary = ""
                                imageUri = null
                            }
                            .addOnFailureListener { e ->
                                Log.w("Requests", "Error adding item", e)
                            }
                    } else {
                        // Image is present, upload it first
                        val storageRef = storage.reference
                        val imageRef = storageRef.child("images/${currentImageUri.lastPathSegment}")
                        val uploadTask = imageRef.putFile(currentImageUri)

                        uploadTask.continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let { throw it }
                            }
                            imageRef.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val downloadUri = task.result
                                val itemData = mapOf(
                                    "listingName" to itemName,
                                    "listingSummary" to itemSummary,
                                    "owner" to userEmail,
                                    "listingPic" to downloadUri.toString()
                                )
                                database.collection("itemsAvail").add(itemData)
                                    .addOnSuccessListener {
                                        Log.d("Requests", "Item with image added successfully")
                                        // Clear fields
                                        itemName = ""
                                        itemSummary = ""
                                        imageUri = null
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("Requests", "Error adding item with image", e)
                                    }
                            } else {
                                Log.w("Requests", "Image upload failed.", task.exception)
                            }
                        }
                    }
                },
                modifier = Modifier.padding(8.dp, 8.dp),
                enabled = itemName.isNotEmpty()
            ) { Text("Add Item") }
        }

        Row(modifier) { HorizontalDivider() }

        Row {
            Text("something else")
        }
    }
}
