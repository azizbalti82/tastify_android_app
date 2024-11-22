package com.balti.tastify.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.balti.tastify.R
import com.balti.tastify.storage.fb
import com.balti.tastify.data


class shoppingListItemsAdapter(
    private var list: MutableList<data.ShoppingListItem>,
    private var parent_id:String
) : RecyclerView.Adapter<shoppingListItemsAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shopping_list_item, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.text.text = item.text

        //set checked items
        holder.checkBox.isChecked = item.checked //if checked in firestore, check it here


        //by default set all the unselected lists ui to grey
        val color = ContextCompat.getColor(holder.itemView.context, R.color.colorCard)
        holder.container.setCardBackgroundColor(color)

        //listeners
        holder.container.setOnClickListener {
            holder.checkBox.isChecked = !holder.checkBox.isChecked
            //update in firestore
            fb.updateShoppingListItem(
                listId = parent_id,
                itemId = item.id,
                data = mapOf("checked" to holder.checkBox.isChecked),
                context = holder.itemView.context,
            )
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.text)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
        val container: CardView = itemView.findViewById(R.id.container)
    }
}