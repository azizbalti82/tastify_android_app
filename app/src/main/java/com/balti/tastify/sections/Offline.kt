package com.balti.tastify.sections

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.balti.tastify.R
import com.balti.tastify.databinding.FragmentOfflineBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class Offline(var bottomNav: BottomNavigationView) : Fragment() {
    lateinit var b: FragmentOfflineBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentOfflineBinding.inflate(inflater, container, false)

        b.login.setOnClickListener {
            if(isOnline(requireContext())){
                bottomNav.selectedItemId = R.id.pageHome
            }else{
                Toast.makeText(requireContext(), "please turn on your wifi first", Toast.LENGTH_SHORT).show()
            }
        }


        return b.root
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}
