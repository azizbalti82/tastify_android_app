package com.balti.tastify.bottom_sheet

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.balti.tastify.MainActivity
import com.balti.tastify.databinding.BottomSheetLoginEmailBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth

class login_email : BottomSheetDialogFragment() {
    lateinit var b: BottomSheetLoginEmailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = BottomSheetLoginEmailBinding.inflate(inflater, container, false)

        b.forgotPassword.setOnClickListener {
            b.progressBarForgotPassword.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                // Code to execute after 3sec
                b.progressBarForgotPassword.visibility = View.GONE
                Toast.makeText(this.requireContext(), "password recovery link sent to your email", Toast.LENGTH_LONG).show()
            }, 3000)
        }

        b.login.setOnClickListener {
            //1st start the loading progress
            b.progressBarLogIn.visibility = View.VISIBLE

            //get form input
            val email = b.email.editableText.toString()
            val password = b.password.editableText.toString()

            //test if the email and password are syntactically valid
            if(email=="" || password==""){
                Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_LONG).show()
                //hide the loading progress
                b.progressBarLogIn.visibility = View.GONE
            }else{
                //setup firebase auth -----------------------------------------------
                val auth = FirebaseAuth.getInstance()

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // User logged in successfully
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finishAffinity() // Restart the app to open the main screen
                    } else {
                        Log.e("error", "Login failed: ${it.exception.toString()}")
                        Toast.makeText(requireContext(), it.exception?.message, Toast.LENGTH_SHORT).show()
                        b.progressBarLogIn.visibility = View.GONE
                    }
                }
            }
        }

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Make the BottomSheetDialogFragment full-screen
        val bottomSheet = dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED // par defaut tet7al expended
    }
}