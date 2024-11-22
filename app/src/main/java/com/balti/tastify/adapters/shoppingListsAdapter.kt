package com.balti.tastify.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.balti.tastify.R
import com.balti.tastify.storage.fb
import com.balti.tastify.data
import com.balti.tastify.others.shoppingListActivity


class shoppingListsAdapter(
    private var list: MutableList<data.ShoppingList>,
    private var delete: ImageView,
    private var close_btn: ImageView,
    private var close_btn_container: LinearLayout,
    private var title: TextView,
    private var add: ImageView,
    private var empty: TextView,
    private var recyclerView: RecyclerView,
) : RecyclerView.Adapter<shoppingListsAdapter.MyViewHolder>() {
    var selected_items:MutableList<String> = ArrayList() //save id of selected items to delete later
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shopping_list, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: shoppingListsAdapter.MyViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.name
        fb.getShoppingListItems(item.id){
            holder.items_count.text = "${it.size} items"
        }
        holder.items_count.text = "0 items"

        //by default set all the unselected lists ui to grey
        val color = ContextCompat.getColor(holder.itemView.context, R.color.colorCard)
        holder.container.setCardBackgroundColor(color)

        //listeners
        holder.container.setOnClickListener {
            if(selected_items.isEmpty()){
                //if nothing is selected, the on click opend tha items page
                val intent = Intent(holder.itemView.context, shoppingListActivity::class.java)
                intent.putExtra("name",item.name)
                intent.putExtra("id",item.id)

                holder.itemView.context.startActivity(intent)
            }else{
                select_item(item.id,holder)
            }
        }

        holder.container.setOnLongClickListener {
            if(selected_items.isEmpty()){
                select_item(item.id,holder)
            }
            return@setOnLongClickListener true
        }

        close_btn.setOnClickListener {
            selected_items.clear()

            //update list:
            this.notifyDataSetChanged()

            //update ui
            update_app_bar()
        }

        delete.setOnClickListener {
            //delete selected items
            for(selected_id in selected_items){
                //delete from firestore
                fb.deleteShoppingList(selected_id){}
                //delete from local list
                list.removeIf {
                    it.id == selected_id
                }
            }
            // Clear selected items after deletion
            selected_items.clear()

            //update list:
            this.notifyDataSetChanged()

            //update ui
            update_app_bar()
            if(list.isEmpty()){
                recyclerView.visibility = View.GONE
                empty.visibility = View.VISIBLE
            }else{
                recyclerView.visibility = View.VISIBLE
                empty.visibility = View.GONE
            }

        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val items_count: TextView = itemView.findViewById(R.id.items_count)
        val container: CardView = itemView.findViewById(R.id.container)
    }

    fun select_item(id:String, holder: MyViewHolder) {
        if(selected_items.contains(id)){
            //unselect current item
            selected_items.remove(id)
            //now change the design of the current item (because its selected)
            val color = ContextCompat.getColor(holder.itemView.context, R.color.colorCard)
            holder.container.setCardBackgroundColor(color)
        }else{
            //select current item
            selected_items.add(id)
            //now change the design of the current item (because its selected)
            val color = ContextCompat.getColor(holder.itemView.context, R.color.colorPrimaryCard)
            holder.container.setCardBackgroundColor(color)
        }


        //verify if nothing is selected and set ui
        update_app_bar()
    }

    private fun update_app_bar() {
        if(selected_items.isEmpty()){
            //nothing selected , hide delete,close_container, and show title in appbar
            delete.visibility = View.GONE
            close_btn_container.visibility = View.GONE
            title.visibility = View.VISIBLE
            add.visibility = View.VISIBLE
        }else{
            delete.visibility = View.VISIBLE
            close_btn_container.visibility = View.VISIBLE
            title.visibility = View.GONE
            add.visibility = View.GONE
        }
    }

}