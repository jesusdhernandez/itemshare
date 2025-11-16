package com.example.itemshare.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.itemshare.R
import com.example.itemshare.ListingItem

class MyRecyclerViewAdapter(private val listingList: List<ListingItem>):
        RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>()
        {
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

                if(listingItem.listingPic != null)
                    holder.listingPic.setImageResource(listingItem.listingPic!!)
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
                    }
        }