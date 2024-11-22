package com.balti.tastify.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.balti.tastify.databinding.BottomSheetLoginBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class login : BottomSheetDialogFragment() {
    lateinit var b: BottomSheetLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = BottomSheetLoginBinding.inflate(inflater, container, false)

        b.email.setOnClickListener {
            val adapter = login_email()
            adapter.show(childFragmentManager , "login_email_fragment")
        }

        b.google.setOnClickListener {
            Toast.makeText(requireContext(), "This feature is not available", Toast.LENGTH_SHORT).show()
        }

        return b.root
    }
}