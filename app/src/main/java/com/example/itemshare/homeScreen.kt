package com.example.itemshare

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itemshare.adapters.MyRecyclerViewAdapter
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

private const val CHANNEL_ID = "default_channel_id"

@Composable
fun homeScreen(userEmail: String, modifier: Modifier = Modifier) {
    var itemList by remember { mutableStateOf<List<ListingItem>>(emptyList()) }
    val itemCollection = Firebase.firestore.collection("itemsAvail")

    val staticItem = remember {
        ListingItem(
            id = "static_id",
            listingName = "A Jason Todd",
            listingSummary = "Looking for a kid to be Robin",
            listingPic = R.drawable.listingimage,
            owner = "bwayne@wayneenterprises.com"
        )
    }
    val context = LocalContext.current

    // Permission request launcher
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("homeScreen", "Notification permission granted")
        } else {
            Log.d("homeScreen", "Notification permission denied")
        }
    }

    // nofi permission req here
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // THIS IS THE  notification channel
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    DisposableEffect(Unit) {
        val listenerRegistration = itemCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.w("homeScreen", "Listen failed.", exception)
                itemList = listOf(staticItem)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val firestoreItems = snapshot.documents.mapNotNull { it.toObject<ListingItem>() }
                itemList = listOf(staticItem) + firestoreItems
            }
        }

        // When the composable is disposed, remove the listener
        onDispose {
            listenerRegistration.remove()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.home_listing, null, false)
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = MyRecyclerViewAdapter(emptyList()) { listingItem ->
                val db = Firebase.firestore
                val notification = NotificationRequest(
                    from = userEmail,
                    to = listingItem.owner,
                    itemName = listingItem.listingName
                )
                db.collection("notifications").add(notification)
                    .addOnSuccessListener {
                        Log.d("homeScreen", "Notification request sent to ${listingItem.owner}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("homeScreen", "Error sending notification request", e)
                    }
            }
            view
        },
        update = { view ->
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
            (recyclerView.adapter as? MyRecyclerViewAdapter)?.updateData(itemList)
        }
    )

}
