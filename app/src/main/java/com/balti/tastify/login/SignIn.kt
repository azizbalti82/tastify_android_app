package com.balti.tastify.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.balti.tastify.R
import com.balti.tastify.adapters.SignInQuestionsAdapter
import com.balti.tastify.data
import com.balti.tastify.databinding.ActivitySignInBinding

class SignIn : AppCompatActivity() {
    private lateinit var bind: ActivitySignInBinding
    override fun onStop() {
        super.onStop()
        data.user = data.User()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //create 3 titles, 3 details, 3 images
        val titles = listOf("Select your favorite cuisines", "Any specific diet ?", "What is your cooking Skill Level ?","")
        val icons = listOf(R.drawable.country, R.drawable.health,R.drawable.skills ,R.drawable.profile)
        val items_positions = listOf(0,1,2,3) //used to determine the position of the current item inside the adapter
        //get the viewpager
        val viewpager = bind.myViewPager2

        //create an object from the adapter
        val adapter = SignInQuestionsAdapter(titles, icons, items_positions,this.supportFragmentManager)

        //set the adapter for viewpager
        viewpager.adapter = adapter

        // Add page change listener to listen to page scrolling
        viewpager.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // This will be triggered whenever the page is changed
                Log.d("sign_in_question", "Current page position: $position")
                bind.progress.progress = position+1

                if(position==3){
                    bind.prev.visibility = View.VISIBLE
                    bind.next.visibility = View.GONE
                }else if(position==0){
                    bind.prev.visibility = View.GONE
                    bind.next.visibility = View.VISIBLE
                }else{
                    bind.next.visibility = View.VISIBLE
                    bind.prev.visibility = View.VISIBLE
                }
            }
        })


        //listeners
        bind.cancel.setOnClickListener {
            this.finish()
        }

        bind.next.setOnClickListener {
            // Move to the next page
            val nextPage = viewpager.currentItem + 1
            if (nextPage < adapter.itemCount) {
                viewpager.currentItem = nextPage
            }
        }

        bind.prev.setOnClickListener {
            // Move to the previous page
            val prevPage = viewpager.currentItem - 1
            if (prevPage >= 0) {
                viewpager.currentItem = prevPage
            }
        }



    }
}