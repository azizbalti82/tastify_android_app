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


class mealVerticalAdapter(
    private var list: MutableList<Meal>,
    empty: Any?,
    recyclerView: Any?,
) : RecyclerView.Adapter<mealVerticalAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal_vertical, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: mealVerticalAdapter.MyViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.strMeal
        //load image
        data.loadImageIntoImageView(item.strMealThumb,holder.image)

        //setup saved icon
        fb.isSaved(item.idMeal){saved->
            if(saved){
                holder.save.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.save_filled))
            }else{
                holder.save.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.save_not_filled))
            }
        }

        //listeners
        holder.container.setOnClickListener {
            //save meal object to show it later when the meal page is open
            data.selected_meal = item
            //if nothing is selected, the on click opend tha items page
            val intent = Intent(holder.itemView.context, recipe::class.java)
            holder.itemView.context.startActivity(intent)
        }

        holder.save.setOnClickListener {
            fb.isSaved(item.idMeal){saved->
                if(saved){
                    //unsave
                    holder.save.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.save_not_filled))
                    fb.deleteSaved(item.idMeal){}
                }else{
                    //save
                    holder.save.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.save_filled))
                    fb.addSaved(item){}
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ShapeableImageView = itemView.findViewById(R.id.image)
        val save: ImageView = itemView.findViewById(R.id.save)
        val title: TextView = itemView.findViewById(R.id.text)
        val container: CardView = itemView.findViewById(R.id.container)
    }
}