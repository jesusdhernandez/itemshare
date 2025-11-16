package com.example.itemshare

import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
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
import com.example.itemshare.adapters.BorrowedItemsAdapter
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

@Composable
fun ListBorrowed(modifier: Modifier = Modifier) {
    var borrowedList by remember { mutableStateOf<List<ListingItem>>(emptyList()) }
    val borrowedCollection = Firebase.firestore.collection("itemsBorrowed")

    DisposableEffect(Unit) {
        val listenerRegistration = borrowedCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.w("ListBorrowed", "Listen failed.", exception)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                borrowedList = snapshot.documents.mapNotNull { it.toObject<ListingItem>() }
            }
        }

        onDispose {
            listenerRegistration.remove()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = {
            val view = LayoutInflater.from(it).inflate(R.layout.home_listing, null, false)
            val titleTextView = view.findViewById<TextView>(R.id.welcome_text)
            titleTextView.text = "Outbound Items"

            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(it)
            recyclerView.adapter = BorrowedItemsAdapter(borrowedList)
            view
        },
        update = {
            val recyclerView = it.findViewById<RecyclerView>(R.id.recyclerView)
            (recyclerView.adapter as? BorrowedItemsAdapter)?.updateData(borrowedList)
        }
    )
}