package com.example.gconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    lateinit var joinUsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        joinUsTextView = findViewById(R.id.alreadyhaveaccount)

        joinUsTextView.setOnClickListener {
            val intent = Intent(this,RegistrationActivity::class.java)
            startActivity(intent)
//            Toast.makeText(this, "Onclick getting executed", Toast.LENGTH_SHORT).show()
        }

        val transaction = supportFragmentManager.beginTransaction()
        val frag = UserLoginFragment()
        transaction.replace(R.id.login_framelayout, frag)
        transaction.commit()
    }



    fun buttonClick(view: View) {
        when(view.id) {
            R.id.individualbutton -> {
                val individualFrag = UserLoginFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.login_framelayout, individualFrag)
                    .addToBackStack(null)
                    .commit()
            }
            R.id.organisationbutton -> {
                val organisationFrag = CompanyLoginFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.login_framelayout, organisationFrag)
                    .addToBackStack(null)

                    .commit()

            }
        }
    }
}