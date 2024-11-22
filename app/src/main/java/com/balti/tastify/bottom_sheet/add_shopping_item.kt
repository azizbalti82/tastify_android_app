package com.balti.tastify.bottom_sheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.balti.tastify.adapters.shoppingListItemsAdapter
import com.balti.tastify.storage.fb
import com.balti.tastify.data
import com.balti.tastify.databinding.BottomSheetAddShoppingItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class add_shopping_item(
    var id: String?,
    private var empty: TextView,
    private var recyclerView: RecyclerView,
    private var adapter: shoppingListItemsAdapter,
    private var result: MutableList<data.ShoppingListItem>,
    ) : BottomSheetDialogFragment() {
    lateinit var b: BottomSheetAddShoppingItemBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = BottomSheetAddShoppingItemBinding.inflate(inflater, container, false)

        //listeners
        b.add.setOnClickListener {
            val input = b.text.text.toString()
            if(id==null){
                Toast.makeText(requireContext(), "invalid list", Toast.LENGTH_SHORT).show()
                dismiss()
            } else if(input.isBlank()){
                Toast.makeText(requireContext(), "write something first", Toast.LENGTH_SHORT).show()
            }else{
                val item = data.ShoppingListItem("",input)
                //add item
                fb.addShoppingListItem(id!!, item){}
                //update ui
                result.add(0,item)
                adapter.notifyItemInserted(0)

                if(result.isEmpty()){
                    recyclerView.visibility = View.GONE
                    empty.visibility = View.VISIBLE
                }else{
                    recyclerView.visibility = View.VISIBLE
                    empty.visibility = View.GONE
                }
                this.dismiss()
            }

        }
        return b.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
        return dialog
    }

}