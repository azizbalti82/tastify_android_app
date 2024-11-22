package com.balti.tastify.others

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balti.tastify.R
import com.balti.tastify.adapters.mealVerticalAdapter
import com.balti.tastify.api.Meal
import com.balti.tastify.databinding.ActivityBookMarksBinding
import com.balti.tastify.storage.fb

class bookMarksActivity : AppCompatActivity() {
    private lateinit var bind: ActivityBookMarksBinding
    var result:MutableList<Meal> = ArrayList()
    lateinit var adapter: mealVerticalAdapter

    override fun onStart() {
        super.onStart()
        loadItems()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityBookMarksBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //loadItems()

        //listeners
        bind.cancel.setOnClickListener {
            this.finish()
        }
    }

    fun loadItems(){
        //setup recycler view: ---------------------------------------------------------------------
        val recyclerView = bind.root.findViewById<RecyclerView>(R.id.recyclerView)

        //Set up the LinearLayoutManager
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        fb.getAllSaved{ saved->
            //Set up the Adapter
            adapter = mealVerticalAdapter(result,bind.empty,bind.recyclerView)
            recyclerView.adapter = adapter

            if(saved.isEmpty()){
                bind.recyclerView.visibility = View.GONE
                bind.empty.visibility = View.VISIBLE
            }else{
                bind.recyclerView.visibility = View.VISIBLE
                bind.empty.visibility = View.GONE

                result.clear()
                result.addAll(saved)
                adapter.notifyDataSetChanged()
            }

        }
    }
}