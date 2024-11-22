package com.balti.tastify.sections

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balti.tastify.R
import com.balti.tastify.adapters.recentSearchesAdapter
import com.balti.tastify.storage.shared
import com.balti.tastify.data
import com.balti.tastify.databinding.FragmentSearchBinding
import com.balti.tastify.others.searchResult

class Search : Fragment() {
    lateinit var b: FragmentSearchBinding

    override fun onStart() {
        super.onStart()
        //update ui
        if(data.recent_searches.isNotEmpty()){
            b.recentSearchesTitle.visibility = View.VISIBLE
            setup_recent_searches()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentSearchBinding.inflate(inflater, container, false)

        b.searchButton.setOnClickListener {
            //get input
            val input = b.searchBar.text.toString()
            if (input==""){
                Toast.makeText(requireContext(), "write something first", Toast.LENGTH_SHORT).show()
            }else{
                //send input to the search result activity
                val intent = Intent(requireContext(), searchResult::class.java)
                intent.putExtra("input",input)
                startActivity(intent)
            }

            //dont fotget to save search input to recent searches
            shared.add_recent(requireContext(),input)
            //reload list
            shared.get_all_recents(requireContext())
        }

        return b.root
    }

    fun setup_recent_searches(){
        //setup recycler view: ---------------------------------------------------------------------
        val recyclerView = b.root.findViewById<RecyclerView>(R.id.recyclerView)

        //Set up the LinearLayoutManager
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        //Set up the Adapter
        val adapter = recentSearchesAdapter(
            data.recent_searches.filter {
                !it.isBlank()
            }.toMutableList()
        )

        recyclerView.adapter = adapter
    }
}
