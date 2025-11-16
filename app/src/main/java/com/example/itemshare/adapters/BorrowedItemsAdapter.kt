package com.example.itemshare.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.itemshare.ListingItem
import com.example.itemshare.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class BorrowedItemsAdapter(private var borrowedList: List<ListingItem>) :
    RecyclerView.Adapter<BorrowedItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.borrowed_item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = borrowedList[position]
        holder.itemName.text = item.listingName
        holder.itemSummary.text = item.listingSummary

        holder.returnButton.setOnClickListener {
            val db = Firebase.firestore
            val itemRef = db.collection("itemsBorrowed").document(item.id)

            itemRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val borrowedItem = document.toObject(ListingItem::class.java)
                    if (borrowedItem != null) {
                        db.collection("itemsAvail").document(item.id).set(borrowedItem)
                            .addOnSuccessListener { 
                                itemRef.delete()
                            }
                    }
                }
            }
        }
    }

    override fun getItemCount() = borrowedList.size

    fun updateData(newBorrowedList: List<ListingItem>) {
        borrowedList = newBorrowedList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.listingName)
        val itemSummary: TextView = view.findViewById(R.id.listingSummary)
        val returnButton: Button = view.findViewById(R.id.returnButton)
    }
}