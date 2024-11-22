package com.balti.tastify.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.balti.tastify.R
import com.balti.tastify.data
import com.balti.tastify.others.shoppingListActivity
import com.google.android.material.imageview.ShapeableImageView


class tagsAdapter(
    private var list: MutableList<String>,
) : RecyclerView.Adapter<tagsAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tags, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: tagsAdapter.MyViewHolder, position: Int) {
        val item = list[position]
        holder.tag.text = item
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tag: TextView = itemView.findViewById(R.id.tag)
    }
}