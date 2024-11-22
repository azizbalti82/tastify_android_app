package com.balti.tastify.bottom_sheet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balti.tastify.R
import com.balti.tastify.adapters.instructionsAdapter
import com.balti.tastify.databinding.BottomSheetStartCoockingBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class start_coocking(var instructions: String, var youtube: String,) : BottomSheetDialogFragment() {
    lateinit var b: BottomSheetStartCoockingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = BottomSheetStartCoockingBinding.inflate(inflater, container, false)
        //setup ui
        if(youtube==""){
            b.open.visibility = View.GONE
            b.unavailable.visibility = View.VISIBLE
        }else{
            b.google.setOnClickListener {
                //open youtube
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtube))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Optional: To ensure it starts a new task
                intent.setPackage("com.google.android.youtube") // Ensure it opens in the YouTube app
                startActivity(intent)
            }
        }

        //load instructions
        Log.d("sign_in_question", instructions)
        val list:MutableList<String> = ArrayList()
        for (i in instructions.split(".").toMutableList()){
            if(i.isNotBlank() && !i.isDigitsOnly()){
                list.add(i.trim())
            }
        }
        loadItems(list)

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Make the BottomSheetDialogFragment full-screen
        val bottomSheet = dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED // par defaut tet7al expended
    }
    fun loadItems(list: List<String>) {
        //setup recycler view: ---------------------------------------------------------------------
        val recyclerView = b.root.findViewById<RecyclerView>(R.id.recyclerView)

        //Set up the LinearLayoutManager
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        //Set up the Adapter
        val adapter = instructionsAdapter(list.toMutableList())
        recyclerView.adapter = adapter
    }
}