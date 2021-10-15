package com.example.gconnect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.gconnect.ui.Notification.ConnectedMembers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_to_see_connected_users_profile.*

class ToSeeConnectedUsersProfile : AppCompatActivity() {

    private lateinit var headlineConnectedUser: TextView
    private lateinit var fullNameConnectedUser: TextView
    private lateinit var userNameConnectedUser: TextView
    private lateinit var emailConnectedUser: TextView
    private lateinit var ageConnectedUser: TextView
    private lateinit var skillsConnectedUser: TextView
    private lateinit var highestQConnectedUser: TextView
    private lateinit var addressConnectedUser: TextView
    private lateinit var contactConnectedUser: TextView
    private lateinit var profileImageConnectedUser: ImageView
    lateinit var imageConnectedUser : String
    private lateinit var connObj : ConnectedMembers


    private lateinit var database: FirebaseDatabase
    private lateinit var authenticationMP : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_see_connected_users_profile)

        connObj = intent.getParcelableExtra("objConnectedMembers")!!

        headlineConnectedUser = findViewById(R.id.headlineConnectedUser)
        fullNameConnectedUser = findViewById(R.id.fullNameConnectedUser)
        userNameConnectedUser = findViewById(R.id.fullNameConnectedUser)
        emailConnectedUser = findViewById(R.id.emailConnectedUser)
        ageConnectedUser = findViewById(R.id.ageConnectedUser)
        skillsConnectedUser = findViewById(R.id.skillsConnectedUser)
        highestQConnectedUser = findViewById(R.id.highestQualificationConnectedUser)
        addressConnectedUser = findViewById(R.id.addressConnectedUser)
        contactConnectedUser = findViewById(R.id.contactConnectedUser)
        profileImageConnectedUser = findViewById(R.id.imageview_ConnectedUser_profile_picture)

        getConnectedUserDetails()
    }

    private fun getConnectedUserDetails() {
        database = FirebaseDatabase.getInstance()
        authenticationMP = FirebaseAuth.getInstance()

        val connectedUserRef = database.getReference("Users").child("Individual")
            .child(connObj.fromUid.toString())

        connectedUserRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                imageConnectedUser = snapshot.child("image").getValue().toString()
                Picasso.get().load(imageConnectedUser).into(profileImageConnectedUser)
                val headline = snapshot.child("headline").getValue()
                headlineConnectedUser.text = "$headline"
                val fullName = snapshot.child("fullName").getValue()
                fullNameConnectedUser.text = "$fullName"
                val userName = snapshot.child("userEmail").getValue()
                userNameConnectedUser.setText("$userName")
                val email = snapshot.child("userEmail").getValue()
                emailConnectedUser.setText("$email")
                val age = snapshot.child("age").getValue()
                ageConnectedUser.setText("$age")
                val contact = snapshot.child("contact").getValue()
                contactConnectedUser.setText("$contact")
                val addresses = snapshot.child("addresses").getValue()
                addressConnectedUser.setText("$addresses")
                val skills = snapshot.child("skills").getValue()
                skillsConnectedUser.setText("$skills")
                val education = snapshot.child("education").getValue()
                highestQualificationConnectedUser.setText("$education")
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
}