package com.balti.tastify

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balti.tastify.adapters.mealVerticalAdapter
import com.balti.tastify.api.Meal
import com.balti.tastify.databinding.ActivityBookMarksBinding
import com.balti.tastify.databinding.ActivityMoreBinding
import com.balti.tastify.storage.fb

class more : AppCompatActivity() {
    private lateinit var bind: ActivityMoreBinding

    override fun onResume() {
        super.onResume()
        loadItems()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMoreBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val title = intent.getStringExtra("title") ?:""
        bind.sectionTitle.text = title
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
        val adapter = mealVerticalAdapter(data.more_meals,null,null)
        recyclerView.adapter = adapter
    }
}