package com.example.itemshare

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun Requests(userEmail: String, modifier: Modifier = Modifier)
{
    val database = Firebase.firestore

    Column(modifier) {

        var itemName by remember { mutableStateOf("") }

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
            Button(onClick = {
                database.collection("itemsAvail")
                    .add(mapOf(
                    "item" to itemName,
                    "owner" to userEmail))
                    .addOnSuccessListener { documentReference ->
                        Log.d("Requests", "${userEmail} added ${itemName} to the collection")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Requests", "Error adding item: ${itemName} (${userEmail})", e)
                    }
                itemName = ""

            }, Modifier.padding(8.dp, 8.dp), enabled = itemName.isNotEmpty())
            { Text("Add Item") }
        }

        Row(modifier) { HorizontalDivider() }

        Row {
            Text("something else")
        }
    }
}
