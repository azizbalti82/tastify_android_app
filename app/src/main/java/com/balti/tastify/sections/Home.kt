package com.balti.tastify.sections

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balti.tastify.MainActivity
import com.balti.tastify.R
import com.balti.tastify.adapters.categoriesHorizontalAdapter
import com.balti.tastify.adapters.mealHorizontalAdapter
import com.balti.tastify.adapters.mealVerticalAdapter
import com.balti.tastify.api.Category
import com.balti.tastify.api.CategoryResponse
import com.balti.tastify.api.Meal
import com.balti.tastify.api.MealResponse
import com.balti.tastify.data
import com.balti.tastify.databinding.FragmentHomeBinding
import com.balti.tastify.more
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Home(var bottomNav: BottomNavigationView) : Fragment() {
    lateinit var b: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentHomeBinding.inflate(inflater, container, false)

        //setup ui
        b.country.text = data.user.country

        //when done is '0000' that means all 4 sections loaded
        var done=""
        //setup lists
        setup_categories(){
            done += "0"
            testLoadingComplete(done)
        }
        setup_mostliked(){
            done += "0"
            testLoadingComplete(done)
        }
        setup_recommendations(){
            done += "0"
            testLoadingComplete(done)
        }
        setup_local(){
            done += "0"
            testLoadingComplete(done)
        }



        //listeners
        b.searchBar.setOnClickListener {
             bottomNav.selectedItemId = R.id.pageSearch
        }

        return b.root
    }

    private fun testLoadingComplete(done:String) {
        if(done=="0000"){
            b.loading.visibility = View.GONE
            b.app.visibility = View.VISIBLE
        }
    }


    //setup lists
    fun setup_categories(callback: (String) -> Unit){
        //setup recycler view: ---------------------------------------------------------------------
        val recyclerView = b.root.findViewById<RecyclerView>(R.id.recyclerView)

        //Set up the LinearLayoutManager
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager

        api_call_categories{
            //Set up the Adapter
            val adapter = categoriesHorizontalAdapter(it.toMutableList())
            recyclerView.adapter = adapter

            callback("")
        }

    }
    fun setup_mostliked(callback: (String) -> Unit){
        //setup recycler view: ---------------------------------------------------------------------
        val recyclerView = b.root.findViewById<RecyclerView>(R.id.recyclerView_mostliked)

        //Set up the LinearLayoutManager
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager


        get_random_recipes(5){
            //Set up the Adapter
            val adapter = mealHorizontalAdapter(it.toMutableList())
            recyclerView.adapter = adapter

            callback("")
        }
    }
    fun setup_recommendations(callback: (String) -> Unit){
        //setup recycler view: ---------------------------------------------------------------------
        val recyclerView = b.root.findViewById<RecyclerView>(R.id.recyclerView_recommendations)

        //Set up the LinearLayoutManager
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager


        get_random_recipes(5){
            //Set up the Adapter
            val adapter = mealHorizontalAdapter(it.toMutableList())
            recyclerView.adapter = adapter

            callback("")
        }
    }
    fun setup_local(callback: (String) -> Unit){
        //setup recycler view: ---------------------------------------------------------------------
        val recyclerView = b.root.findViewById<RecyclerView>(R.id.recyclerView_local)

        //Set up the LinearLayoutManager
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager


        api_call_local(data.user.country){local_meals->
            //Set up the Adapter
            if(local_meals.isEmpty()){
                b.empty.visibility = View.VISIBLE
            }else if(local_meals.size<=3){
                val adapter = mealVerticalAdapter(local_meals.toMutableList(),null,null)
                recyclerView.adapter = adapter
            }else{
                //show 'more' button
                b.moreLocal.visibility = View.VISIBLE
                //make that more open an activity to show all the meals
                b.moreLocal.setOnClickListener {
                    //save meals for more page
                    data.more_meals = local_meals.toMutableList()
                    //open more page
                    val intent = Intent(requireContext(), more::class.java)
                    intent.putExtra("title","Local recipes")
                    requireContext().startActivity(intent)
                }

                val adapter = mealVerticalAdapter(local_meals.subList(0,3).toMutableList(),null,null)
                recyclerView.adapter = adapter
            }

            callback("")
        }
    }


    //api calls
    fun api_call_categories(callback: (List<Category>) -> Unit){
        val call = data.apiService.getCategories()

        call.enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(p0: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                if (response.isSuccessful) {
                    val categories = response.body()?.categories

                    if(categories==null){
                        callback(listOf())
                    }else{
                        callback(categories)
                    }
                } else {
                    // Handle error if response is not successful
                    Log.e("error","Error: ${response.code()}")
                    callback(listOf())
                }
            }

            override fun onFailure(p0: Call<CategoryResponse>, p1: Throwable) {
                // Handle failure (e.g., no network connection)
                p1.printStackTrace()
                callback(listOf())
            }
        })

    }
    fun api_call_local(area: String, callback: (List<Meal>) -> Unit) {
        val call = data.apiService.getMealsByArea(area)
        call.enqueue(object : Callback<MealResponse> {
            override fun onResponse(call: Call<MealResponse>, response: Response<MealResponse>) {
                if (response.isSuccessful) {
                    val meals = response.body()?.meals
                    callback(meals ?: emptyList()) // Pass the meals or an empty list
                } else {
                    Log.e("API_ERROR", "Error: ${response.code()}")
                    callback(emptyList())
                }
            }

            override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                t.printStackTrace()
                callback(emptyList()) // Pass an empty list on failure
            }
        })
    }
    fun api_call_random(callback: (List<Meal>) -> Unit){
        //get random 10 meals
        val call = data.apiService.getRandomMeal()
        call.enqueue(object : Callback<MealResponse> {
            override fun onResponse(p0: Call<MealResponse>, response: Response<MealResponse>) {
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
                    callback(ArrayList())
                }
            }

            override fun onFailure(p0: Call<MealResponse>, p1: Throwable) {
                // Handle failure (e.g., no network connection)
                p1.printStackTrace()
                callback(listOf())
            }
        })
    }
    fun get_random_recipes(n:Int,callback: (MutableList<Meal>) -> Unit){
        val result:MutableList<Meal> = ArrayList()
        for(i in 1..n){
            api_call_random{random->
                result.add(random[0])

                //see if all the n meals successfully received
                if(result.size==n){
                    callback(result)
                }
            }
        }

    }

}
