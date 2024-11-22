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


class ingredientAdapter(
    private var list: MutableList<data.Ingredient>,
) : RecyclerView.Adapter<ingredientAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ingredientAdapter.MyViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item.name
        holder.mesure.text = item.mesure


        //listeners
        holder.container.setOnClickListener {
            //if nothing is selected, the on click opend tha items page
//            val intent = Intent(holder.itemView.context, shoppingListActivity::class.java)
//            holder.itemView.context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val image: ShapeableImageView = itemView.findViewById(R.id.image)
        val name: TextView = itemView.findViewById(R.id.name)
        val mesure: TextView = itemView.findViewById(R.id.mesure)
        val container: CardView = itemView.findViewById(R.id.container)
    }
}