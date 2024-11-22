package com.balti.tastify.api

data class MealResponse(
    val meals: List<Meal>?
)
data class Meal(
    var idMeal: String = "",
    val strMeal: String = "",
    val strMealThumb: String = "",
    val strInstructions: String = "",
    val strCategory: String = "",
    val strArea: String = "",
    val strTags: String = "",
    val strYoutube: String = "",
    val strSource: String = "",
    val strIngredient1: String = "",
    val strIngredient2: String = "",
    val strIngredient3: String = "",
    val strIngredient4: String = "",
    val strIngredient5: String = "",
    val strIngredient6: String = "",
    val strIngredient7: String = "",
    val strIngredient8: String = "",
    val strIngredient9: String = "",
    val strIngredient10: String = "",
    val strIngredient11: String = "",
    val strIngredient12: String = "",
    val strIngredient13: String = "",
    val strIngredient14: String = "",
    val strIngredient15: String = "",
    val strIngredient16: String = "",
    val strIngredient17: String = "",
    val strIngredient18: String = "",
    val strIngredient19: String = "",
    val strIngredient20: String = "",
    val strMeasure1: String = "",
    val strMeasure2: String = "",
    val strMeasure3: String = "",
    val strMeasure4: String = "",
    val strMeasure5: String = "",
    val strMeasure6: String = "",
    val strMeasure7: String = "",
    val strMeasure8: String = "",
    val strMeasure9: String = "",
    val strMeasure10: String = "",
    val strMeasure11: String = "",
    val strMeasure12: String = "",
    val strMeasure13: String = "",
    val strMeasure14: String = "",
    val strMeasure15: String = "",
    val strMeasure16: String = "",
    val strMeasure17: String = "",
    val strMeasure18: String = "",
    val strMeasure19: String = "",
    val strMeasure20: String = ""
)



data class CategoryResponse(
    val categories: List<Category>?
)

data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)

data class AreaResponse(
    val meals: List<Area>?
)

data class Area(
    val strArea: String
)

data class IngredientResponse(
    val meals: List<Ingredient>?
)

data class Ingredient(
    val strIngredient: String
)

data class MealTypeResponse(
    val meals: List<MealType>?
)

data class MealType(
    val strCategory: String
)
