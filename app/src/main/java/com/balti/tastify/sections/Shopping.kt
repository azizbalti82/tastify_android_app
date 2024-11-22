package com.balti.tastify.sections

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balti.tastify.R
import com.balti.tastify.adapters.shoppingListsAdapter
import com.balti.tastify.storage.fb
import com.balti.tastify.data
import com.balti.tastify.databinding.FragmentShoppingBinding

class Shopping(var add:ImageView,var delete:ImageView,var close_btn:ImageView,var close_btn_container:LinearLayout,var title:TextView) : Fragment() {
    lateinit var b: FragmentShoppingBinding
    var result:MutableList<data.ShoppingList> = ArrayList()
    lateinit var adapter: shoppingListsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentShoppingBinding.inflate(inflater, container, false)

        //lists:
        loadLists()

        //listeners --------------------------------------------------------------------------------
        add.setOnClickListener {
            showCustomDialog(inflater)
        }

        return b.root
    }

    fun loadLists(){
        //setup recycler view: ---------------------------------------------------------------------
        val recyclerView = b.root.findViewById<RecyclerView>(R.id.recyclerView)

        //Set up the LinearLayoutManager
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        fb.getShoppingLists{list_from_firestore->
            result.addAll(list_from_firestore)
            //Set up the Adapter
            adapter = shoppingListsAdapter(result,delete,close_btn,close_btn_container,title,add,b.empty,b.recyclerView)
            recyclerView.adapter = adapter

            if(list_from_firestore.isEmpty()){
                b.recyclerView.visibility = View.GONE
                b.empty.visibility = View.VISIBLE
            }else{
                b.recyclerView.visibility = View.VISIBLE
                b.empty.visibility = View.GONE

                fb.getShoppingLists {
                    result.clear()
                    result.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            }

        }
    }
    fun showCustomDialog(inflater: LayoutInflater) {
        val dialogView: View = inflater.inflate(R.layout.alert_add_shopping_list, null)
        // Build the AlertDialog
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        // Create the AlertDialog
        val customDialog: AlertDialog = builder.create()
        // We changed bg to transparent to show up the corners in clean way
        customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // Set up any interactions or functionalities
        val create = dialogView.findViewById<Button>(R.id.create)
        val cancel = dialogView.findViewById<Button>(R.id.cancel)
        cancel.setOnClickListener {
            // Dismiss the dialog
            customDialog.dismiss()
        }
        create.setOnClickListener {
            val input = dialogView.findViewById<EditText>(R.id.input).text.toString()
            if (input.isNotBlank()) {
                b.empty.visibility = View.GONE
                b.recyclerView.visibility = View.VISIBLE

                val newItem = data.ShoppingList(name = input, id = "")
                fb.addShoppingList(newItem, requireContext())
                result.add(0, newItem) // Add the new item to the top of the list
                adapter.notifyItemInserted(0) // Notify adapter about the new item
                customDialog.dismiss() // Dismiss the dialog
            } else {
                Toast.makeText(requireContext(), "write something first", Toast.LENGTH_SHORT).show()
            }
        }

        // Show the dialog
        customDialog.show()
    }
}