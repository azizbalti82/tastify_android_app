package com.balti.tastify.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.balti.tastify.R
import com.balti.tastify.others.searchResult


class recentSearchesAdapter(
    private var list: MutableList<String>,
) : RecyclerView.Adapter<recentSearchesAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recent_search, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: recentSearchesAdapter.MyViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item

        //listeners
        holder.container.setOnClickListener {
            //send input to the search result activity
            val intent = Intent(holder.itemView.context, searchResult::class.java)
            intent.putExtra("input",item)
            holder.itemView.context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val container: LinearLayout = itemView.findViewById(R.id.container)
    }
}