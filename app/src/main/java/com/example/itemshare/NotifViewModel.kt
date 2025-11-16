// NotificationViewModel.kt
package com.example.itemshare

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationViewModel : ViewModel() {

    // This is private because only the ViewModel should be able to change it.
    private val _notificationRequest = MutableLiveData<Event<ListingItem>>()

    // This is public, so other classes (like messages.kt) can observe it for changes.
    val notificationRequest: LiveData<Event<ListingItem>> = _notificationRequest

    /**
     * Called by the RecyclerView adapter when a button is clicked.
     */
    fun onMessageButtonClicked(listingItem: ListingItem) {
        // Post a new event to the LiveData.
        _notificationRequest.value = Event(listingItem)
    }
}
    