package com.balti.tastify.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.balti.tastify.R
import com.balti.tastify.api.Category
import com.balti.tastify.data
import com.balti.tastify.others.shoppingListActivity
import com.google.android.material.imageview.ShapeableImageView


class categoriesHorizontalAdapter(
    private var list: MutableList<Category>,
) : RecyclerView.Adapter<categoriesHorizontalAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: categoriesHorizontalAdapter.MyViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.strCategory
        data.loadImageIntoImageView(item.strCategoryThumb,holder.image)

        //listeners
        holder.container.setOnClickListener {
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ShapeableImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.title)
        val container: CardView = itemView.findViewById(R.id.container)
    }
}