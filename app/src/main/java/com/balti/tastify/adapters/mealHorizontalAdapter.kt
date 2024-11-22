package com.balti.tastify.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.balti.tastify.R
import com.balti.tastify.api.Meal
import com.balti.tastify.data
import com.balti.tastify.recipe
import com.balti.tastify.storage.fb
import com.google.android.material.imageview.ShapeableImageView


class mealHorizontalAdapter(
    private var list: MutableList<Meal>,
) : RecyclerView.Adapter<mealHorizontalAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.strMeal
        holder.country.text = item.strArea
        holder.category.text = item.strCategory
        //load image
        data.loadImageIntoImageView(item.strMealThumb,holder.image)

        //listeners
        holder.container.setOnClickListener {
            //save meal object to show it later when the meal page is open
            data.selected_meal = item
            //if nothing is selected, the on click opend tha items page
            val intent = Intent(holder.itemView.context, recipe::class.java)
            holder.itemView.context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ShapeableImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.title)
        val country: TextView = itemView.findViewById(R.id.country)
        val category: TextView = itemView.findViewById(R.id.category)
        val container: CardView = itemView.findViewById(R.id.container)
    }
}