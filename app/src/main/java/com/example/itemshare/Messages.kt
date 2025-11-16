package com.example.itemshare

import android.util.Log
import android.view.LayoutInflater
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itemshare.adapters.MessageRecyclerViewAdapter
import com.example.itemshare.adapters.MyRecyclerViewAdapter
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

@Composable
fun Messages(modifier: Modifier = Modifier.fillMaxSize())
{
    var messagesList by remember { mutableStateOf<List<messageInfo>>(emptyList()) }
    val messagesCollection = Firebase.firestore.collection("messages")

    val staticItem = remember {
        messageInfo(
            id = "static_id",
            messagerName = "zachary smellicious",
            message = "fackkkkkk you",
        )
    }

    // Listen for real-time updates from Firestore
    DisposableEffect(Unit) {
        val listenerRegistration = messagesCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.w("homeScreen", "Listen failed.", exception)
                // On error, just show the static item
                messagesList = listOf(staticItem)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val firestoreItems = snapshot.documents.mapNotNull { it.toObject<messageInfo>() }
                // Combine the static item with the items from Firestore
                messagesList = listOf(staticItem) + firestoreItems
            }
        }

        // When the composable is disposed, remove the listener
        onDispose {
            listenerRegistration.remove()
        }
    }

    // AndroidView to host the RecyclerView
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.message_listing, null, false)
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(context)
            // Initialize adapter with an empty list
            recyclerView.adapter = MessageRecyclerViewAdapter(emptyList())
            view
        },
        update = { view ->
            // Update the adapter with the new list when it changes
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
            (recyclerView.adapter as? MessageRecyclerViewAdapter)?.updateData(messagesList)
        }
    )

}