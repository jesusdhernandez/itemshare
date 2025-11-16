package com.example.itemshare

import android.util.Log
import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itemshare.adapters.MyRecyclerViewAdapter
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

@Composable
fun homeScreen(modifier: Modifier = Modifier) {
    var itemList by remember { mutableStateOf<List<ListingItem>>(emptyList()) }
    val itemCollection = Firebase.firestore.collection("itemsAvail")

    val staticItem = remember {
        ListingItem(
            id = "static_id",
            listingName = "A Jason Todd",
            listingSummary = "Looking for a kid to be Robin",
            listingPic = R.drawable.listingimage
        )
    }

    // Listen for real-time updates from Firestore
    DisposableEffect(Unit) {
        val listenerRegistration = itemCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.w("homeScreen", "Listen failed.", exception)
                // On error, just show the static item
                itemList = listOf(staticItem)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val firestoreItems = snapshot.documents.mapNotNull { it.toObject<ListingItem>() }
                // Combine the static item with the items from Firestore
                itemList = listOf(staticItem) + firestoreItems
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
            val view = LayoutInflater.from(context).inflate(R.layout.home_listing, null, false)
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(context)
            // Initialize adapter with an empty list
            recyclerView.adapter = MyRecyclerViewAdapter(emptyList())
            view
        },
        update = { view ->
            // Update the adapter with the new list when it changes
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
            (recyclerView.adapter as? MyRecyclerViewAdapter)?.updateData(itemList)
        }
    )
}
