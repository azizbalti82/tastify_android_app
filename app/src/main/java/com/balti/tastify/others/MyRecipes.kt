package com.balti.tastify.others

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.balti.tastify.databinding.ActivityMyRecipesBinding

class myRecipes : AppCompatActivity() {
    private lateinit var bind: ActivityMyRecipesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMyRecipesBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //listeners
        bind.cancel.setOnClickListener {
            this.finish()
        }
    }
}