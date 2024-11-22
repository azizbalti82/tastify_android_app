package com.balti.tastify.others

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balti.tastify.R
import com.balti.tastify.adapters.shoppingListItemsAdapter
import com.balti.tastify.storage.fb
import com.balti.tastify.bottom_sheet.add_shopping_item
import com.balti.tastify.data
import com.balti.tastify.databinding.ActivityShoppingListBinding

class shoppingListActivity : AppCompatActivity() {
    private lateinit var bind: ActivityShoppingListBinding
    var result:MutableList<data.ShoppingListItem> = ArrayList()
    lateinit var adapter: shoppingListItemsAdapter
    lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityShoppingListBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //get list name from intent
        id = intent.getStringExtra("id")  ?:""
        val name = intent.getStringExtra("name")

        if(id.isBlank()){
            Toast.makeText(this, "invalid list", Toast.LENGTH_SHORT).show()
            return
        }

        //set ui
        if(name==null){
            this.finish()
        }else{
            bind.name.text = name
        }

        //get items
        loadItems()

        //listeners
        bind.cancel.setOnClickListener {
            this.finish()
        }
        bind.addItems.setOnClickListener {
            val adapter = add_shopping_item(id,bind.empty,bind.recyclerView,adapter,result)
            adapter.show(this.supportFragmentManager , "add_shopping_item")
        }
    }

    fun loadItems(){
        //setup recycler view: ---------------------------------------------------------------------
        val recyclerView = bind.root.findViewById<RecyclerView>(R.id.recyclerView)

        //Set up the LinearLayoutManager
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        fb.getShoppingListItems(id){ items_from_firestore->
            //Set up the Adapter
            adapter = shoppingListItemsAdapter(result,id)
            recyclerView.adapter = adapter

            if(items_from_firestore.isEmpty()){
                bind.recyclerView.visibility = View.GONE
                bind.empty.visibility = View.VISIBLE
            }else{
                bind.recyclerView.visibility = View.VISIBLE
                bind.empty.visibility = View.GONE

                result.clear()
                result.addAll(items_from_firestore)
                adapter.notifyDataSetChanged()
            }

        }
    }
}