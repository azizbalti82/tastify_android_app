package com.balti.tastify

import android.content.Context
import android.widget.ImageView
import com.balti.tastify.api.ApiService
import com.balti.tastify.api.Meal
import com.balti.tastify.storage.fb
import com.squareup.picasso.Picasso

class data {
    companion object {
        // variables -------------------------------------------------------------------------------
        lateinit var apiService: ApiService
        var user = User(
            name = "",
            email = "",
            avatar = null,
            preferredCuisines = ArrayList(),
            dietaryPreferences = ArrayList(),
            cookingSkillLevel = "",
            country = ""
        )

        //recent searches
        var recent_searches: MutableList<String> = ArrayList()
        var recent_searches_string = ""

        //selected meal object
        lateinit var selected_meal: Meal
        //for more meals page
        lateinit var more_meals: MutableList<Meal>


        //functions --------------------------------------------------------------------------------
        fun loadImageIntoImageView(imageUrl: String, imageView: ImageView) {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.color.colorSurface)  // Placeholder while loading
                .error(R.drawable.info)        // Error image if loading fails
                .into(imageView)
        }

        fun getAvatarLocal(avatar: String): Int {
            if (avatar == "1") {
                return (R.drawable.avatar_1)
            } else if (avatar == "2") {
                return (R.drawable.avatar_2)
            } else if (avatar == "3") {
                return (R.drawable.avatar_3)
            } else if (avatar == "4") {
                return (R.drawable.avatar_4)
            } else if (avatar == "5") {
                return (R.drawable.avatar_5)
            } else if (avatar == "6") {
                return (R.drawable.avatar_6)
            } else if (avatar == "7") {
                return (R.drawable.avatar_7)
            } else if (avatar == "8") {
                return (R.drawable.avatar_8)
            } else {
                return (0)
            }
        }

        fun getAvatar(c: Context, callback: (avatarId: Int) -> Unit) {
            fb.getUserInfo(c, fb.getUID()) {
                var avatar = it.avatar
                if (avatar == "1") {
                    callback(R.drawable.avatar_1)
                } else if (avatar == "2") {
                    callback(R.drawable.avatar_2)
                } else if (avatar == "3") {
                    callback(R.drawable.avatar_3)
                } else if (avatar == "4") {
                    callback(R.drawable.avatar_4)
                } else if (avatar == "5") {
                    callback(R.drawable.avatar_5)
                } else if (avatar == "6") {
                    callback(R.drawable.avatar_6)
                } else if (avatar == "7") {
                    callback(R.drawable.avatar_7)
                } else if (avatar == "8") {
                    callback(R.drawable.avatar_8)
                } else {
                    callback(0)
                }
            }
        }
    }


    data class User(
        var name: String = "",
        var email: String = "",
        val avatar: String? = null, // Optional, URL or file path to the profile picture
        val preferredCuisines: MutableList<String> = ArrayList(), // e.g., ["Italian", "Indian"]
        val dietaryPreferences: MutableList<String> = ArrayList(), // e.g., ["Vegetarian", "Gluten-Free"]
        var cookingSkillLevel: String = "", // e.g., "Beginner", "Intermediate", "Advanced"
        var country:String=""
    )

    data class ShoppingListItem(
        var id: String = "",
        var text: String = "",
        var checked: Boolean = false,
    )

    data class ShoppingList(
        var id: String = "",
        var name: String = "",
    )

    data class Ingredient(
        val name: String,
        val mesure: String,
    )

    data class SavedMeal(
        var id: String = "",
        val name: String = "",
        val image: String = "",
    )
}

