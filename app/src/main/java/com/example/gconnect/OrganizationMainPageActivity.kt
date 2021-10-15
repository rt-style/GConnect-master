package com.example.gconnect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
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
import com.example.gconnect.databinding.ActivityOrganizationMainPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class OrganizationMainPageActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityOrganizationMainPageBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_organization_main_page)
        binding = ActivityOrganizationMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarOrganizationMainPage.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_organization_main_page)

        auth = FirebaseAuth.getInstance()


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_list, R.id.nav_home_page,  R.id.nav_jobs, R.id.nav_post, R.id.nav_orgProfile
                ,R.id.nav_notification, R.id.nav_connection
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.organization_main_page, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.action_organization_logout-> {
                // auth sign out
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
                auth.signOut()
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
            }
            R.id.action_add_jobs -> {
                Toast.makeText(this, "Add JObs Clicked", Toast.LENGTH_SHORT).show()
                val i = Intent(this, AddJobsActivity::class.java)
                startActivity(i)

            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_organization_main_page)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}





















