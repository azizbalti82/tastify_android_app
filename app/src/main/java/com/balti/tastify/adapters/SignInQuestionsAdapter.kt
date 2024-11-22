package com.balti.tastify.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.balti.tastify.MainActivity
import com.balti.tastify.R
import com.balti.tastify.api.Area
import com.balti.tastify.api.AreaResponse
import com.balti.tastify.api.Meal
import com.balti.tastify.bottom_sheet.select_avatar
import com.balti.tastify.storage.fb
import com.balti.tastify.data
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInQuestionsAdapter(var title:List<String>, var icons: List<Int>,var items_positions:List<Int>,private val fragmentManager: FragmentManager) :
    RecyclerView.Adapter<SignInQuestionsAdapter.Pager2ViewHolder>() {
        var selected_avatar = ""
        //declaring views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pager2ViewHolder {
        //here we connect the viewHolder with our layout (the one we want to show everytime we swipe)
        //we created it in the previous step
        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sign_in_questions,parent,false))
    }
    override fun getItemCount(): Int {
        return title.size
    }
    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        val item = items_positions[position]

        api_call_countries{ countries->
            Log.d("sign_in_question", countries.toString())
            // Create an ArrayAdapter
            val adapter = ArrayAdapter(
                holder.itemView.context, // Context
                R.layout.item_countries_spinner, // Layout for the dropdown items
                countries// List of items
            )

            // Set the dropdown layout style
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Attach the adapter to the Spinner
            holder.country.adapter = adapter
        }


        //setup the right options for the right questions ------------------------------------------
        //1) hide everything
        holder.options_cuisines.visibility = View.GONE
        holder.options_alergics.visibility = View.GONE
        holder.options_skills.visibility = View.GONE
        holder.options_personal.visibility = View.GONE
        //2) show the top title by default
        holder.itemTitle.visibility = View.VISIBLE
        //3) now show the right option
        if(item == 0){
            holder.options_cuisines.visibility = View.VISIBLE
        }else if(item == 1){
            holder.options_alergics.visibility = View.VISIBLE
        }else if(item == 2){
            holder.options_skills.visibility = View.VISIBLE
        }else if(item == 3){
            holder.options_personal.visibility = View.VISIBLE
            //also hide the top icon
            holder.itemTitle.visibility = View.GONE
        }


        // setup all the options of the options ----------------------------------------------------
        setSelectOptionsLogic(holder.options_cuisines,holder.itemView.context,holder.itemView)
        setSelectOptionsLogic(holder.options_alergics,holder.itemView.context,holder.itemView)
        setSelectOptionsLogic(holder.options_skills,holder.itemView.context,holder.itemView)

        // other listeners -------------------------------------------------------------------------
        holder.itemTitle.text = title[position]
        holder.itemIcon.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context,icons[position]))
        holder.signIn.setOnClickListener {
            //get email and password from input
            val name = holder.name.text.toString()
            val email = holder.email.text.toString()
            val password = holder.password.text.toString()
            val country = holder.country.selectedItem.toString()

            //save data to user object to be uploaded when creeting account later
            data.user.email = email
            data.user.name = name
            data.user.country = country


            if(name==""){
                Toast.makeText(holder.itemView.context, "Name is required", Toast.LENGTH_SHORT).show()
            } else if(email==""){
                Toast.makeText(holder.itemView.context, "Email is required", Toast.LENGTH_SHORT).show()
            } else if(password.length < 6){
                Toast.makeText(holder.itemView.context, "Password is required", Toast.LENGTH_SHORT).show()
            }else{
                val auth = FirebaseAuth.getInstance()
                try {
                    //sign in with the user object stored in the class 'data'
                    auth.createUserWithEmailAndPassword(data.user.email,password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            //you logged in lets save user data to the firestore
                            fb.setUserInfo(data.user,holder.itemView.context)
                            //now restart the app to open the main app screen
                            val context = holder.itemView.context as? Activity
                            context?.let {
                                val intent = Intent(it, MainActivity::class.java)
                                it.startActivity(intent)
                                it.finishAffinity() // This will close the current activity stack
                            }
                        } else {
                            Log.e("error", "onCreate: " + it.exception.toString(),)
                            Toast.makeText(holder.itemView.context, it.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }catch (e:Exception){
                    Toast.makeText(holder.itemView.context, "Error accused", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setSelectOptionsLogic(option: LinearLayout,c: Context,itemView:View) {
        //select/unselect items inside options and update the 'user' object inside 'data'
        for (i in 0 until option.childCount) {
            val childView = option.getChildAt(i)
            // You can now perform actions on each child view, e.g., check its type or visibility
            if (childView is CardView) {
                childView.setOnClickListener {
                    //variables to analayse clicked item
                    var clicked_value = ""
                    var category = ""

                    //extract text:
                    var id = childView.id
                    if(id == itemView.findViewById<View?>(R.id.cuisine_asian).id){
                        clicked_value = "asian"
                        category = "cuisine"
                    }else if(id == itemView.findViewById<View?>(R.id.cuisine_european).id){
                        clicked_value = "euro"
                        category = "cuisine"
                    }else if(id == itemView.findViewById<View?>(R.id.cuisine_american).id){
                        clicked_value = "american"
                        category = "cuisine"
                    }else if(id == itemView.findViewById<View?>(R.id.cuisine_middle_eastern).id){
                        clicked_value = "middle eastern"
                        category = "cuisine"
                    }else if(id == itemView.findViewById<View?>(R.id.cuisine_african).id){
                        clicked_value = "african"
                        category = "cuisine"
                    }else if(id == itemView.findViewById<View?>(R.id.cuisine_oceania).id){
                        clicked_value = "oceania"
                        category = "cuisine"
                    }else if(id == itemView.findViewById<View?>(R.id.diet_low_carb).id){
                        clicked_value = "low_carb"
                        category = "diet"
                    }else if(id == itemView.findViewById<View?>(R.id.diet_low_sugar).id){
                        clicked_value = "low_sugar"
                        category = "diet"
                    }else if(id == itemView.findViewById<View?>(R.id.diet_low_fat).id){
                        clicked_value = "low_fat"
                        category = "diet"
                    }else if(id == itemView.findViewById<View?>(R.id.diet_high_protein).id){
                        clicked_value = "high_protein"
                        category = "diet"
                    }else if(id == itemView.findViewById<View?>(R.id.diet_vegan).id){
                        clicked_value = "vegan"
                        category = "diet"
                    }else if(id == itemView.findViewById<View?>(R.id.diet_keto).id){
                        clicked_value = "keto"
                        category = "diet"
                    }else if(id == itemView.findViewById<View?>(R.id.skill_beginner).id){
                        clicked_value = "beginner"
                        category = "skill"
                    }else if(id == itemView.findViewById<View?>(R.id.skill_intermediate).id){
                        clicked_value = "intermediate"
                        category = "skill"
                    }else if(id == itemView.findViewById<View?>(R.id.skill_advanced).id){
                        clicked_value = "advanced"
                        category = "skill"
                    }

                    //now update both list and ui
                    if(category == "skill"){
                        //we need onle one item selected, so lets get back all the items to default background
                        val background = ContextCompat.getDrawable(c, R.drawable.unselected_card)
                        itemView.findViewById<View?>(R.id.skill_beginner).setBackgroundDrawable(background)
                        itemView.findViewById<View?>(R.id.skill_intermediate).setBackgroundDrawable(background)
                        itemView.findViewById<View?>(R.id.skill_advanced).setBackgroundDrawable(background)

                        //check if its already selected
                        if(data.user.cookingSkillLevel == clicked_value){
                            //unselect: remove from list + change colors
                            //remove from list
                            data.user.cookingSkillLevel=""
                        }else {
                            val background = ContextCompat.getDrawable(c, R.drawable.selected_card)
                            childView.setBackgroundDrawable(background)
                            data.user.cookingSkillLevel=clicked_value
                        }
                    } else if (category == "cuisine"){
                        //check if its already selected
                        if(data.user.preferredCuisines.contains(clicked_value)){
                            //unselect: remove from list + change colors
                            val background = ContextCompat.getDrawable(c, R.drawable.unselected_card)
                            childView.setBackgroundDrawable(background)

                            //remove from list
                            data.user.preferredCuisines.remove(clicked_value)
                        }else {
                            val background = ContextCompat.getDrawable(c, R.drawable.selected_card)
                            childView.setBackgroundDrawable(background)
                            data.user.preferredCuisines.add(clicked_value)
                        }
                    } else if (category == "diet"){
                        //check if its already selected
                        if(data.user.dietaryPreferences.contains(clicked_value)){
                            //unselect: remove from list + change colors
                            val background = ContextCompat.getDrawable(c, R.drawable.unselected_card)
                            childView.setBackgroundDrawable(background)

                            //remove from list
                            data.user.dietaryPreferences.remove(clicked_value)
                        }else {
                            val background = ContextCompat.getDrawable(c, R.drawable.selected_card)
                            childView.setBackgroundDrawable(background)

                            data.user.dietaryPreferences.add(clicked_value)
                        }
                    }



                    Log.d("sign_in_question", clicked_value+" "+category)
                }
            }
        }
    }
    private fun api_call_countries (callback: (list: List<String>) -> Unit){
        val call = data.apiService.getAreas()
        call.enqueue(object : Callback<AreaResponse> {
            override fun onResponse(call: Call<AreaResponse>, response: Response<AreaResponse>) {
                if (response.isSuccessful) {
                    val areas = response.body()?.meals ?: listOf()
                    // Use the list of areas
                    val result:MutableList<String> = ArrayList()
                    for(a in areas){
                        result.add(a.strArea)
                    }
                    callback(result)
                } else {
                    Log.e("Error", "Failed to fetch areas: ${response.code()}")
                    callback(ArrayList())
                }
            }

            override fun onFailure(call: Call<AreaResponse>, t: Throwable) {
                Log.e("Error", "API call failed", t)
                callback(ArrayList())
            }
        })
    }


    inner class Pager2ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val itemTitle:TextView = itemView.findViewById(R.id.title)
        val itemIcon: ImageView = itemView.findViewById(R.id.icon)
        val itemIconContainer: FrameLayout = itemView.findViewById(R.id.icon_container)
        val signIn: CardView = itemView.findViewById(R.id.signIn)


        //form
        val name: EditText = itemView.findViewById(R.id.name)
        val email: EditText = itemView.findViewById(R.id.email)
        val password: EditText = itemView.findViewById(R.id.password)
        val country: Spinner = itemView.findViewById(R.id.country)

        //options
        val options_cuisines: LinearLayout = itemView.findViewById(R.id.container_cuisines)
        val options_alergics: LinearLayout = itemView.findViewById(R.id.container_diets)
        val options_skills: LinearLayout = itemView.findViewById(R.id.container_cooking_skills)
        val options_personal: LinearLayout = itemView.findViewById(R.id.container_personal)
    }
}