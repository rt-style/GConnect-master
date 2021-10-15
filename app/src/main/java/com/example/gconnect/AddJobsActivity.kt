package com.example.gconnect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.gconnect.ui.HomePage.JobPost

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddJobsActivity : AppCompatActivity() {

    lateinit var db : FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var companyName : EditText
    lateinit var jobTitle : EditText
    lateinit var jobDescription : EditText
    lateinit var location : EditText
    lateinit var vacancy :EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_jobs)

        companyName = findViewById(R.id.job_company_nameE)
        jobTitle = findViewById(R.id.job_title_nameE)
        jobDescription = findViewById(R.id.job_description_nameE)
        location = findViewById(R.id.job_locationE)
        vacancy = findViewById(R.id.job_vacancyE)
        auth = FirebaseAuth.getInstance()
    }

    fun AddJobClick(view: View) {
        val cName = companyName.text.toString()
        val jobTitle = jobTitle.text.toString()
        val jobDes = jobDescription.text.toString()
        val loc = location.text.toString()
        val vacancy = vacancy.text.toString()

        if(cName.isNotEmpty() && jobTitle.isNotEmpty() && jobDes.isNotEmpty() && loc.isNotEmpty() && vacancy.isNotEmpty()){


            db = FirebaseDatabase.getInstance()
            val stdref =db.getReference("PostJob")
//            stdref.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    count = snapshot.childrenCount
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            })
            val key = stdref.push().key
            val tho = JobPost(key,cName,jobTitle,jobDes,loc,vacancy.toInt(),auth.currentUser?.uid)
            stdref.child(tho.id.toString()).setValue(tho)

        }
        else{
            Toast.makeText(this,"Enter all field", Toast.LENGTH_LONG).show()
        }
        finish()
    }


}