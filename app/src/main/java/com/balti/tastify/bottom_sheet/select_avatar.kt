package com.balti.tastify.bottom_sheet

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.balti.tastify.data
import com.balti.tastify.databinding.BottomSheetSelectAvatarBinding
import com.balti.tastify.storage.fb
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.imageview.ShapeableImageView

class select_avatar(var avatar: ShapeableImageView,var c: Context) : BottomSheetDialogFragment() {
    lateinit var b: BottomSheetSelectAvatarBinding

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        //load avatar
        data.getAvatar(c){
            if(it!=0){
                avatar.setImageDrawable(ContextCompat.getDrawable(c, it))
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = BottomSheetSelectAvatarBinding.inflate(inflater, container, false)

        b.avatar1.setOnClickListener {
            fb.updateUserInfo(mapOf ("avatar" to "1"),requireContext())
            dismiss()
        }
        b.avatar2.setOnClickListener {
            fb.updateUserInfo(mapOf ("avatar" to "2"),requireContext())
            dismiss()
        }
        b.avatar3.setOnClickListener {
            fb.updateUserInfo(mapOf ("avatar" to "3"),requireContext())
            dismiss()
        }
        b.avatar4.setOnClickListener {
            fb.updateUserInfo(mapOf ("avatar" to "4"),requireContext())
            dismiss()
        }
        b.avatar5.setOnClickListener {
            fb.updateUserInfo(mapOf ("avatar" to "5"),requireContext())
            dismiss()
        }
        b.avatar6.setOnClickListener {
            fb.updateUserInfo(mapOf ("avatar" to "6"),requireContext())
            dismiss()
        }
        b.avatar7.setOnClickListener {
            fb.updateUserInfo(mapOf ("avatar" to "7"),requireContext())
            dismiss()
        }
        b.avatar8.setOnClickListener {
            fb.updateUserInfo(mapOf ("avatar" to "8"),requireContext())
            dismiss()
        }



        return b.root
    }
}