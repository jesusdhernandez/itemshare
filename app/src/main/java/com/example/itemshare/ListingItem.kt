package com.example.itemshare

class ListingItem
{
    var listingName: String? = null
    var listingSummary: String? = null
    var listingPic: Int? = null

    constructor(listingName:String, listingSummary:String, listingPic:Int)
    {
        this.listingName = listingName
        this.listingSummary = listingSummary
        this.listingPic = listingPic
    }
}