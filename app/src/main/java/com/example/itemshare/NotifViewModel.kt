package com.example.itemshare

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotifViewModel : ViewModel() {

    private val _notificationRequest = MutableLiveData<Event<ListingItem>>()

    val notificationRequest: LiveData<Event<ListingItem>> = _notificationRequest

    fun onMessageButtonClicked(listingItem: ListingItem) {
        _notificationRequest.value = Event(listingItem)
    }
}
