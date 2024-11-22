package com.balti.tastify

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balti.tastify.adapters.ingredientAdapter
import com.balti.tastify.adapters.tagsAdapter
import com.balti.tastify.api.Meal
import com.balti.tastify.api.MealResponse
import com.balti.tastify.bottom_sheet.start_coocking
import com.balti.tastify.databinding.ActivityRecipeBinding
import com.balti.tastify.storage.fb
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class recipe : AppCompatActivity() {
    private lateinit var bind: ActivityRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //set ui
        //get recipe if its not completely ready (like local recipes)
        if(data.selected_meal.strIngredient1==""){
            //the old meal don't have any ingredient so let reload it
            fetchMealById(data.selected_meal.idMeal){
                data.selected_meal = it ?: Meal()
                setUI()
            }
        }else{
            setUI()
        }


        //listeners
        bind.cancel.setOnClickListener {
            this.finish()
        }
        bind.goToSource.setOnClickListener {
            val src = data.selected_meal.strSource
            if(src.isEmpty()){
                Toast.makeText(this, "invalid source", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(src))
                startActivity(intent)
            }
        }

        bind.save.setOnClickListener {
            fb.isSaved(data.selected_meal.idMeal){saved->
                if(saved){
                    //unsave
                    bind.save.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.save_not_filled))
                    fb.deleteSaved(data.selected_meal.idMeal){}
                }else{
                    //save
                    bind.save.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.save_filled))
                    fb.addSaved(data.selected_meal){}
                }
            }
        }
        bind.start.setOnClickListener {
            val adapter = start_coocking(data.selected_meal.strInstructions,data.selected_meal.strYoutube)
            adapter.show(this.supportFragmentManager , "start_coocking")
        }
    }

    private fun setUI() {
        data.loadImageIntoImageView(data.selected_meal.strMealThumb,bind.image)
        bind.title.text = data.selected_meal.strMeal
        bind.cuisine.text = data.selected_meal.strArea
        bind.category.text = data.selected_meal.strCategory
        setup_tags()
        setup_ingredients()
        //setup saved icon
        fb.isSaved(data.selected_meal.idMeal){saved->
            if(saved){
                bind.save.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.save_filled))
            }else{
                bind.save.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.save_not_filled))
            }
        }
    }

    private fun setup_ingredients() {
        //setup recycler view: ---------------------------------------------------------------------
        val result = mutableListOf(
            data.Ingredient(
                data.selected_meal.strIngredient1,
                data.selected_meal.strMeasure1
            ),
            data.Ingredient(
                data.selected_meal.strIngredient2,
                data.selected_meal.strMeasure2
            ),
            data.Ingredient(
                data.selected_meal.strIngredient3,
                data.selected_meal.strMeasure3
            ),
            data.Ingredient(
                data.selected_meal.strIngredient4,
                data.selected_meal.strMeasure4
            ),
            data.Ingredient(
                data.selected_meal.strIngredient5,
                data.selected_meal.strMeasure5
            ),
            data.Ingredient(
                data.selected_meal.strIngredient6,
                data.selected_meal.strMeasure6
            ),
            data.Ingredient(
                data.selected_meal.strIngredient7,
                data.selected_meal.strMeasure7
            ),
            data.Ingredient(
                data.selected_meal.strIngredient8,
                data.selected_meal.strMeasure8
            ),
            data.Ingredient(
                data.selected_meal.strIngredient9,
                data.selected_meal.strMeasure9
            ),
            data.Ingredient(
                data.selected_meal.strIngredient10,
                data.selected_meal.strMeasure10
            ),
            data.Ingredient(
                data.selected_meal.strIngredient11,
                data.selected_meal.strMeasure11
            ),
            data.Ingredient(
                data.selected_meal.strIngredient12,
                data.selected_meal.strMeasure12
            ),
            data.Ingredient(
                data.selected_meal.strIngredient13,
                data.selected_meal.strMeasure13
            ),
            data.Ingredient(
                data.selected_meal.strIngredient14,
                data.selected_meal.strMeasure14
            ),
            data.Ingredient(
                data.selected_meal.strIngredient15,
                data.selected_meal.strMeasure15
            ),
            data.Ingredient(
                data.selected_meal.strIngredient16,
                data.selected_meal.strMeasure16
            ),
            data.Ingredient(
                data.selected_meal.strIngredient17,
                data.selected_meal.strMeasure17
            ),
            data.Ingredient(
                data.selected_meal.strIngredient18,
                data.selected_meal.strMeasure18
            ),
            data.Ingredient(
                data.selected_meal.strIngredient19,
                data.selected_meal.strMeasure19
            ),
            data.Ingredient(
                data.selected_meal.strIngredient20,
                data.selected_meal.strMeasure20
            ),

            )
        result.removeIf {
            it.name==""
        }

        val recyclerView = bind.root.findViewById<RecyclerView>(R.id.recyclerView)
        //Set up the LinearLayoutManager
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager

        //Set up the Adapter
        val adapter = ingredientAdapter(result)
        recyclerView.adapter = adapter

    }
    private fun setup_tags(){
        val result:MutableList<String> = ArrayList()
        try {
            if (data.selected_meal.strTags.isEmpty()) {
                bind.tagsTitle.visibility = View.GONE
            } else {
                result.addAll(data.selected_meal.strTags.split(","))
                if (result.isNotEmpty()) {
                    //setup recycler view: ---------------------------------------------------------------------
                    val recyclerView = bind.root.findViewById<RecyclerView>(R.id.recyclerView_tags)

                    //Set up the LinearLayoutManager
                    val layoutManager = LinearLayoutManager(this)
                    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                    recyclerView.layoutManager = layoutManager

                    //Set up the Adapter
                    val adapter = tagsAdapter(result)
                    recyclerView.adapter = adapter

                    bind.tagsTitle.visibility = View.VISIBLE
                } else {
                    bind.tagsTitle.visibility = View.GONE
                }
            }
        }catch (e:Exception){ }
        }
    fun fetchMealById(id: String, callback: (Meal?) -> Unit) {
        val call = data.apiService.getMealById(id)
        call.enqueue(object : Callback<MealResponse> {
            override fun onResponse(call: Call<MealResponse>, response: Response<MealResponse>) {
                if (response.isSuccessful) {
                    val meal = response.body()?.meals?.firstOrNull() // Fetch the first meal
                    callback(meal)
                } else {
                    Log.e("API_ERROR", "Error: ${response.code()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                t.printStackTrace()
                callback(null) // Pass null on failure
            }
        })
    }

}
