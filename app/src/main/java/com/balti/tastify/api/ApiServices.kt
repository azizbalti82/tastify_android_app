package com.balti.tastify.api

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // Search meals by name
    @GET("search.php")
    fun searchMeals(@Query("s") searchTerm: String): Call<MealResponse>

    // Get a random meal
    @GET("random.php")
    fun getRandomMeal(): Call<MealResponse>

    @GET("lookup.php")
    fun getMealById(@Query("i") id: String): Call<MealResponse>


    // Search meals by category
    @GET("filter.php")
    fun getMealsByCategory(@Query("c") category: String): Call<MealResponse>

    // Search meals by area (country)
    @GET("filter.php")
    fun getMealsByArea(@Query("a") area: String): Call<MealResponse>
    
    // Get a list of all available categories
    @GET("categories.php")
    fun getCategories(): Call<CategoryResponse>

    // Get a list of all available areas (countries)
    @GET("list.php?a=list")
    fun getAreas(): Call<AreaResponse>

    // Get a list of all ingredients
    @GET("list.php?i=list")
    fun getIngredients(): Call<IngredientResponse>


    // Get meals based on a specific letter (A-Z)
    @GET("search.php")
    fun searchMealsByLetter(@Query("f") letter: String): Call<MealResponse>
}
