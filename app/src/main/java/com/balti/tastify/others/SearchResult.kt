package com.balti.tastify.others

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balti.tastify.R
import com.balti.tastify.adapters.mealVerticalAdapter
import com.balti.tastify.api.Meal
import com.balti.tastify.api.MealResponse
import com.balti.tastify.data
import com.balti.tastify.databinding.ActivitySearchResultBinding
import com.balti.tastify.storage.fb
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class searchResult : AppCompatActivity() {
    private lateinit var bind: ActivitySearchResultBinding
    var result:MutableList<Meal> = ArrayList()
    lateinit var adapter: mealVerticalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //get search input
        val input = intent.getStringExtra("input") ?: ""

        //listeners
        bind.cancel.setOnClickListener {
            this.finish()
        }

        loadLists(input)
    }

    fun loadLists(input:String){
        //setup recycler view: ---------------------------------------------------------------------
        val recyclerView = bind.root.findViewById<RecyclerView>(R.id.recyclerView)

        //Set up the LinearLayoutManager
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        //call api
        api_call_search(input){list->
            //update ui
            bind.sectionTitle.text = "${list.size} results"

            //initialise list
            adapter = mealVerticalAdapter(result,bind.empty,bind.recyclerView)
            recyclerView.adapter = adapter

            if(list.isEmpty()){
                bind.recyclerView.visibility = View.GONE
                bind.empty.visibility = View.VISIBLE
            }else{
                bind.recyclerView.visibility = View.VISIBLE
                bind.empty.visibility = View.GONE

                fb.getShoppingLists {
                    result.clear()
                    result.addAll(list)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
    fun api_call_search(input:String, callback: (List<Meal>) -> Unit){
        // Search for meals by name (e.g., "chicken")
        val call = data.apiService.searchMeals(input)

        call.enqueue(object : Callback<MealResponse> {
            override fun onResponse(call: Call<MealResponse>, response: Response<MealResponse>) {
                if (response.isSuccessful) {
                    val meals = response.body()?.meals

                    if(meals==null){
                        callback(listOf())
                    }else{
                        callback(meals)
                    }
                } else {
                    // Handle error if response is not successful
                    Log.e("error","Error: ${response.code()}")
                    callback(listOf())
                }
            }

            override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                // Handle failure (e.g., no network connection)
                t.printStackTrace()
                callback(listOf())
            }
        })

    }
}