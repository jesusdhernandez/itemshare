package com.example.itemshare

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

@Composable
fun Homepage(modifier: Modifier) {
    var items by remember { mutableStateOf<List<Item>>(emptyList()) }
    val itemCollection = Firebase.firestore.collection("itemsAvail")

    // LaunchedEffect to fetch data from Firestore when the composable is first displayed
    LaunchedEffect(Unit) {
        itemCollection.get()
            .addOnSuccessListener { result ->
                // Map the documents to a list of Item objects
                items = result.documents.mapNotNull { document ->
                    // The toObject extension function converts the document to your data class
                    document.toObject<Item>()
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                Log.w("Homepage", "Error getting documents.", exception)
            }
    }

    // LazyVerticalGrid displays items in a grid
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp), // Each item will have a minimum width of 128.dp
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp), // Spacing between columns
        verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing between rows
    ) {
        items(items) { item ->
            ItemCard(item = item)
        }
    }
}

@Composable
fun ItemCard(item: Item) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp) // Padding inside the card
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = item.item)
        }
    }
}

data class Item(
    @DocumentId val id: String = "",
    val item: String = "Unknown item"
)
