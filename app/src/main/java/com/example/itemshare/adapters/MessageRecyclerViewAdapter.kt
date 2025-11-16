package com.example.itemshare.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.itemshare.R
import com.example.itemshare.messageInfo

class MessageRecyclerViewAdapter(private var listingList: List<messageInfo>):
        RecyclerView.Adapter<MessageRecyclerViewAdapter.MyViewHolder>()
        {

            fun updateData(newList: List<messageInfo>) {
                listingList = newList
                notifyDataSetChanged()
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): MyViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.message_card,parent,false
                )
                return MyViewHolder(view)
            }

            override fun onBindViewHolder(
                holder:MyViewHolder,
                position: Int
            ) {
                val messageInfo = listingList[position]

                holder.messagerName.text = messageInfo.messagerName
                holder.message.text = messageInfo.message
            }

            override fun getItemCount(): Int {
                return listingList.size
            }

            class MyViewHolder(messageView: View):
                    RecyclerView.ViewHolder(messageView)
                    {
                        val messagerName: TextView = messageView.findViewById(R.id.messagerName)
                        val message: TextView = messageView.findViewById(R.id.message)
                    }
        }