package com.example.itemshare


import com.google.firebase.firestore.DocumentId

data class NotificationRequest(
    @DocumentId val id: String = "",
    val from: String = "",
    val to: String? = null,
    val itemName: String? = null
)