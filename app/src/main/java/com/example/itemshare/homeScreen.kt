package com.example.itemshare

import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itemshare.adapters.MyRecyclerViewAdapter

@Composable
fun homeScreen(modifier: Modifier = Modifier) {
    val listingList = remember {
        listOf(
            ListingItem(
                "A Jason Todd",
                "Looking for a kid to be Robin",
                R.drawable.listingimage
            )
        )
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.home_listing, null, false)
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = MyRecyclerViewAdapter(listingList)
            }
            view
        }
    )
}
