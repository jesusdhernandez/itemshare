package com.example.itemshare

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.itemshare.ui.theme.ItemShareTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

private const val CHANNEL_ID = "default_channel_id"

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
            } else {}
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestStoragePermission()

        setContent {
            ItemShareTheme {
                var userEmail by rememberSaveable { mutableStateOf("") }

                if (userEmail.isEmpty()) {
                    LoginScreen(onLoginSuccess = { email -> userEmail = email })
                } else {
                    NotificationListener(userEmail)
                    ItemShareApp(userEmail)
                }
            }
        }
    }

    @Composable
    fun NotificationListener(userEmail: String) {
        val db = Firebase.firestore
        val notificationsCollection = db.collection("notifications")

        DisposableEffect(userEmail) {
            val listener = notificationsCollection.whereEqualTo("to", userEmail)
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w("MainActivity", "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    for (doc in snapshots!!.documentChanges) {
                        if (doc.type == com.google.firebase.firestore.DocumentChange.Type.ADDED) {
                            val notification = doc.document.toObject<NotificationRequest>()
                            showNotification(notification)
                            doc.document.reference.delete()
                        }
                    }
                }
            onDispose {
                listener.remove()
            }
        }
    }

    private fun showNotification(notification: NotificationRequest) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("New Message about ${notification.itemName}")
            .setContentText("You have a new message from ${notification.from}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notification.id.hashCode(), builder.build())
    }

    private fun requestStoragePermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
            }
            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }
}

@Preview
@Composable
fun ItemShareApp(userEmail: String = "") {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { 
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) { 
        Scaffold(modifier = Modifier.fillMaxSize().padding(16.dp)) { innerPadding ->
            when(currentDestination){
                AppDestinations.REQUESTS -> Requests(userEmail = userEmail, modifier = Modifier.padding(innerPadding))
                AppDestinations.HOME -> homeScreen(userEmail = userEmail, modifier = Modifier.padding(innerPadding))
                AppDestinations.MESSAGES -> Messages(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    REQUESTS("Requests", Icons.AutoMirrored.Default.Send),
    MESSAGES("Messages", Icons.Default.Email);
}