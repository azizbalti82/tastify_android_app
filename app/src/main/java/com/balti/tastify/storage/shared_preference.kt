package com.balti.tastify.storage

import android.content.Context
import android.content.SharedPreferences
import com.balti.tastify.data

class shared{
    companion object{
        fun save(context: Context, key: String, value: Boolean) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        fun get(context: Context,key: String, defaultValue: Boolean = true): Boolean {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(key, defaultValue)
        }


        fun add_recent(context: Context, value: String) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("recent", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            var new_recents=""
            if(data.recent_searches.isEmpty()){
                //this is the first recent
                new_recents = value
            }else{
                //add this recent to others
                new_recents = value+"-"+data.recent_searches_string
            }

            data.recent_searches_string = new_recents
            editor.putString("recent_searches", new_recents)
            editor.apply()
        }

        fun get_all_recents(context: Context,defaultValue: String = "") {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("recent", Context.MODE_PRIVATE)
            val recents =  sharedPreferences.getString("recent_searches", defaultValue) ?:""


            if(recents.isNotBlank()){
                //now save them in the data class
                data.recent_searches_string = recents
                data.recent_searches = recents.split("-").toMutableList()
            }
        }
    }
}