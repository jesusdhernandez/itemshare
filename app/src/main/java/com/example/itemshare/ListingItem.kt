package com.example.itemshare

import com.google.firebase.firestore.DocumentId

data class ListingItem(
    @DocumentId val id: String = "",
    var listingName: String? = null,
    var listingSummary: String? = null,
    var listingPic: Any? = null,
    var owner: String? = null // aka email
)
