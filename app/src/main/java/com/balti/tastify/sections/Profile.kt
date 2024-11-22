package com.balti.tastify.sections

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.balti.tastify.MainActivity
import com.balti.tastify.R
import com.balti.tastify.storage.fb
import com.balti.tastify.bottom_sheet.setting_language_select
import com.balti.tastify.data
import com.balti.tastify.databinding.FragmentProfileBinding
import com.balti.tastify.others.myRecipes

class Profile : Fragment() {
    lateinit var b: FragmentProfileBinding
    override fun onStart() {
        super.onStart()
        //load avatar
        data.getAvatar(requireContext()){
            if(it!=0){
                b.avatar.setImageDrawable(ContextCompat.getDrawable(requireContext(), it))
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentProfileBinding.inflate(inflater, container, false)

        //setup ui
        fb.realtime_getUserInfo(requireContext()){ data->
            b.name.text = data.name
            b.email.text = data.email
        }


        //listeners
        b.lo.setOnClickListener {
            fb.logout()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finishAffinity() // Restart the app to open the main screen
        }

        b.settingsLanguage.setOnClickListener {
            val adapter = setting_language_select()
            adapter.show(childFragmentManager , "language")
        }
        b.settingsShare.setOnClickListener {
            Toast.makeText(requireContext(), "This feature is not available yet", Toast.LENGTH_SHORT).show()
        }
        b.settingsRate.setOnClickListener {
            Toast.makeText(requireContext(), "This feature is not available yet", Toast.LENGTH_SHORT).show()
        }
        b.settingsAbout.setOnClickListener {
            Toast.makeText(requireContext(), "This feature is not available yet", Toast.LENGTH_SHORT).show()
        }
        b.settingsMyRecipes.setOnClickListener {
            val intent = Intent(requireContext(), myRecipes::class.java)
            startActivity(intent)
        }


        return b.root
    }
}