package com.balti.tastify.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.balti.tastify.databinding.BottomSheetSettingsLanguageSelectBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class setting_language_select : BottomSheetDialogFragment() {
    lateinit var b: BottomSheetSettingsLanguageSelectBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = BottomSheetSettingsLanguageSelectBinding.inflate(inflater, container, false)



        return b.root
    }
}