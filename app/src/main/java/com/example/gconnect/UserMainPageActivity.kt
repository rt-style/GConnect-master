package com.example.gconnect

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.gconnect.databinding.ActivityUserMainPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.nav_header_user_main_page.view.*

class UserMainPageActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityUserMainPageBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarUserMainPage.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_user_main_page)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_list, R.id.nav_home_page,  R.id.nav_jobs, R.id.nav_post, R.id.nav_MYProfile
                    ,R.id.nav_notification, R.id.nav_connection
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val currentUser = auth.currentUser?.uid

        val ref = db.getReference("Users").child("Individual").child("$currentUser")

        ref.addValueEventListener(object: ValueEventListener{
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {


//                var fullName1 :String? = null
//                fullName1 = snapshot.child("fullName").getValue().toString()
//                binding.navView.fName_Nav_textView1.text ="$fullName1"
//
////               var email :String? =null
////               email = snapshot.child("userEmail").getValue().toString()
//                binding.navView.email_Nav_textView.text ="Gconnect@gmail.com"

                val fullName = snapshot.child("fullName").getValue()
                if(fullName == ""){
                    val i = Intent(this@UserMainPageActivity,UserEditProfileActivity::class.java)
                    startActivity(i)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.user_main_page, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.action_logout -> {
                // auth sign out
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
                auth.signOut()
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
            }
        }

        return super.onOptionsItemSelected(item)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_user_main_page)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}