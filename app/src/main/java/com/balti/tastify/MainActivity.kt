package com.balti.tastify

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.balti.tastify.api.ApiService
import com.balti.tastify.api.Instance
import com.balti.tastify.storage.shared
import com.balti.tastify.bottom_sheet.login
import com.balti.tastify.databinding.ActivityGetStartedBinding
import com.balti.tastify.databinding.ActivityMainBinding
import com.balti.tastify.login.SignIn
import com.balti.tastify.others.bookMarksActivity
import com.balti.tastify.others.editProfileActivity
import com.balti.tastify.sections.Home
import com.balti.tastify.sections.Profile
import com.balti.tastify.sections.Search
import com.balti.tastify.sections.Shopping
import com.google.firebase.auth.FirebaseAuth
import android.content.res.Configuration
import androidx.core.content.ContextCompat
import com.balti.tastify.storage.fb
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.balti.tastify.databinding.ActivityOfflineBinding
import com.balti.tastify.sections.Offline


class MainActivity : AppCompatActivity() {
    private lateinit var bind_main: ActivityMainBinding
    private lateinit var bind_get_started: ActivityGetStartedBinding
    private lateinit var bind_offline: ActivityOfflineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind_main = ActivityMainBinding.inflate(layoutInflater)
        bind_get_started = ActivityGetStartedBinding.inflate(layoutInflater)
        bind_offline = ActivityOfflineBinding.inflate(layoutInflater)

        //initialize retrofit instance
        data.apiService = Instance.retrofit.create(ApiService::class.java)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser?.uid

        if (currentUser!=null) { //main app screen -------------------------------------------------
            setTheme(R.style.tastify)
            setContentView(bind_main.root)

            //load user data
            fb.getUserInfo(this,fb.getUID()){
                data.user = it

                //add bottom navigation
                addBottomNavListener()

                //load recent searches
                shared.get_all_recents(this)

                if (savedInstanceState == null) {
                    // Default to Home fragment
                    if(isOnline(this)){
                        replaceFrag(Home(bind_main.bottomNav),"Home")
                    }else{
                        replaceFrag(Offline(bind_main.bottomNav),"")
                    }
                }
            }


            //listeners for main app:
            bind_main.bookmarks.setOnClickListener {
                val intent = Intent(this, bookMarksActivity::class.java)
                startActivity(intent)
            }
            bind_main.editProfile.setOnClickListener {
                val intent = Intent(this, editProfileActivity::class.java)
                startActivity(intent)
            }
        } else { // login screen -------------------------------------------------------------------
            setTheme(R.style.tastify)
            setContentView(bind_get_started.root)
            if(isDarkModeEnabled()){
                bind_get_started.image.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.chef_vector))
            }

            // sign in
            bind_get_started.signIn.setOnClickListener {
                val intent = Intent(this, SignIn::class.java)
                startActivity(intent)
            }

            // log in
            bind_get_started.logIn.setOnClickListener {
                val adapter = login()
                adapter.show(supportFragmentManager , "login_fragment")
            }
        }
    } //--------------------------------------------------------------------------------------------

    fun addBottomNavListener(){
        bind_main.bottomNav.setOnItemSelectedListener{ item ->
            //hide selection items , we dont need them until we do
            bind_main.deleteSelected.visibility = View.GONE
            bind_main.discardSelection.visibility = View.GONE
            bind_main.sectionTitle.visibility = View.VISIBLE
            bind_main.addShoppingList.visibility = View.VISIBLE
            //select fragment
            when(item.itemId){
                R.id.pageHome -> {
                    if(isOnline(this)){
                        replaceFrag(Home(bind_main.bottomNav),"Home")
                    }else{
                        replaceFrag(Offline(bind_main.bottomNav),"")
                    }

                    true
                }

                R.id.pageSearch -> {
                    replaceFrag(Search(),"Search")
                    true
                }

                R.id.pageShoppingList -> {
                    replaceFrag(
                        Shopping(
                            bind_main.addShoppingList,
                            bind_main.deleteSelected,
                            bind_main.discardSelectionButton,
                            bind_main.discardSelection,
                            bind_main.sectionTitle
                        ),"Shopping list")
                    true
                }

                R.id.pageProfile -> {
                    replaceFrag(Profile(),"Profile")
                    true
                }
                else -> false
            }
        }
    }
    fun replaceFrag(fragment: Fragment,section_title:String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()

        //hide all the options
        bind_main.shoppingListOptions.visibility = View.GONE
        bind_main.profileOptions.visibility = View.GONE
        bind_main.homeOptions.visibility = View.GONE
        bind_main.sectionTitle.visibility = View.VISIBLE
        //show the options of the current page
        bind_main.sectionTitle.text = section_title
        if(section_title.lowercase()=="home"){
            bind_main.homeOptions.visibility = View.VISIBLE
            //bind_main.sectionTitle.visibility = View.GONE
        }else if(section_title.lowercase()=="profile"){
            bind_main.profileOptions.visibility = View.VISIBLE
        }else if(section_title.lowercase()=="shopping list"){
            bind_main.shoppingListOptions.visibility = View.VISIBLE
        }
    }
    fun isDarkModeEnabled(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}
