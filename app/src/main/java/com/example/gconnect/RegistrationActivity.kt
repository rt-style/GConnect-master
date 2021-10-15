package com.example.gconnect

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView


class RegistrationActivity : AppCompatActivity() {

    lateinit var switch : Switch

    lateinit var alreadyhaveaccount : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        supportActionBar?.hide()

        switch = findViewById(R.id.switch1)
        val transaction = supportFragmentManager.beginTransaction()
        val frag = login_user()

        transaction.replace(R.id.registrationContainer, frag)
        transaction.commit()
        switch.isChecked = false
        switch.setOnClickListener {
            if(switch.isChecked){
                val transaction = supportFragmentManager.beginTransaction()
                val frag = login_organization()

                transaction.replace(R.id.registrationContainer, frag)
                //transaction.addToBackStack(null)
                transaction.commit()

            }else{
                val transaction = supportFragmentManager.beginTransaction()
                val frag = login_user()

                transaction.replace(R.id.registrationContainer, frag)
                //transaction.addToBackStack(null)
                transaction.commit()
            }
        }
        alreadyhaveaccount = findViewById(R.id.alreadyhaveaccount)

        alreadyhaveaccount.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)

        }




    }
}