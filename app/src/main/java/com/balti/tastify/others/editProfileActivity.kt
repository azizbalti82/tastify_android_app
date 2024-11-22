package com.balti.tastify.others

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.balti.tastify.bottom_sheet.select_avatar
import com.balti.tastify.data
import com.balti.tastify.storage.fb
import com.balti.tastify.databinding.ActivityEditProfileBinding

class editProfileActivity : AppCompatActivity() {
    private lateinit var bind: ActivityEditProfileBinding

    override fun onStart() {
        super.onStart()
        //load avatar
        data.getAvatar(this){
            if(it!=0){
                bind.avatar.setImageDrawable(ContextCompat.getDrawable(this, it))
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //set ui:
        fb.realtime_getUserInfo(this){ data->
            bind.name.setText(data.name)
        }

        //listeners
        bind.cancel.setOnClickListener {
            this.finish()
        }
        bind.save.setOnClickListener {
            fb.updateUserInfo(
                mapOf("name" to bind.name.text.toString()),
                context = this
            )
            this.finish()
        }
        bind.changeImage.setOnClickListener {
            val adapter = select_avatar(bind.avatar,this)
            adapter.show(this.supportFragmentManager , "select_avatar")
        }
    }
}