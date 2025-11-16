package com.example.itemshare.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.itemshare.R
import com.example.itemshare.ListingItem
import coil.load
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MyRecyclerViewAdapter(private var listingList: List<ListingItem>):
        RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>()
        {

            fun updateData(newList: List<ListingItem>) {
                listingList = newList
                notifyDataSetChanged()
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): MyViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.listing_card,parent,false
                )
                return MyViewHolder(view)
            }

            override fun onBindViewHolder(
                holder:MyViewHolder,
                position: Int
            ) {
                val listingItem = listingList[position]

                holder.listingName.text = listingItem.listingName
                holder.listingSummary.text = listingItem.listingSummary

                if (listingItem.listingPic != null) {
                    when (val pic = listingItem.listingPic) {
                        is Int -> {
                            holder.listingPic.setImageResource(pic)
                        }
                        is String -> {
                            holder.listingPic.load(pic) {
                                placeholder(R.drawable.listingimage)
                                error(android.R.drawable.ic_menu_gallery)
                            }
                        }
                    }
                    holder.listingPic.visibility = View.VISIBLE
                } else {
                    // Clear the image and hide the view if there is no picture
                    holder.listingPic.setImageDrawable(null)
                    holder.listingPic.visibility = View.GONE
                }

                holder.borrowButton.setOnClickListener {
                    val db = Firebase.firestore
                    val itemRef = db.collection("itemsAvail").document(listingItem.id)
                    itemRef.get().addOnSuccessListener { document ->
                        if (document != null) {
                            val item = document.toObject(ListingItem::class.java)
                            if (item != null) {
                                db.collection("itemsBorrowed").document(listingItem.id).set(item)
                                    .addOnSuccessListener { 
                                        itemRef.delete()
                                    }
                            }
                        }
                    }
                }
            }

            override fun getItemCount(): Int {
                return listingList.size
            }

            class MyViewHolder(itemView: View):
                    RecyclerView.ViewHolder(itemView)
                    {
                        val listingName: TextView = itemView.findViewById(R.id.listingName)
                        val listingSummary: TextView = itemView.findViewById(R.id.listingSummary)
                        val listingPic: ImageView = itemView.findViewById(R.id.listingPic)
                        val borrowButton: Button = itemView.findViewById(R.id.borrowButton)
                    }
        }