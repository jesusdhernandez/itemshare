package com.example.itemshare

import com.google.firebase.firestore.DocumentId

data class messageInfo(
    @DocumentId val id: String = "",
    var messagerName: String? = null,
    var message: String? = null
)
