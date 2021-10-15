package com.example.gconnect.ui.MyProfile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.gconnect.R
import com.example.gconnect.UserEditProfileActivity
import com.example.gconnect.databinding.FragmentMyProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class MyProfileFragment : Fragment() {

    private lateinit var headlineMP : TextView
    private lateinit var fullNameMP : TextView
    private lateinit var userNameMP : TextView
    private lateinit var emailMP : TextView
    private lateinit var ageMP : TextView
    private lateinit var highestQualificationMP : TextView
    private lateinit var skillsMP : TextView
    private lateinit var addressMP : TextView
    private lateinit var contactMP : TextView
    private lateinit var editButton : ImageView
    private lateinit var profileImage : ImageView
    lateinit var image : String

    private lateinit var database: FirebaseDatabase
    private lateinit var authenticationMP : FirebaseAuth

    private lateinit var myProfileViewModel: MyProfileViewModel
    private var _binding: FragmentMyProfileBinding?=null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        headlineMP = view.findViewById(R.id.headlineMP)
        fullNameMP = view.findViewById(R.id.fullNameMP)
        userNameMP = view.findViewById(R.id.userNameMP)
        emailMP = view.findViewById(R.id.emailMP)
        ageMP = view.findViewById(R.id.ageMP)
        highestQualificationMP = view.findViewById(R.id.highestQualificationMP)
        skillsMP = view.findViewById(R.id.skillsMP)
        addressMP = view.findViewById(R.id.addressMP)
        contactMP = view.findViewById(R.id.contactMP)
        editButton = view.findViewById(R.id.edit_profile_button)
        profileImage = view.findViewById(R.id.imageview_profile_picture)
        
        
        editButton.setOnClickListener {
            val i = Intent(activity, UserEditProfileActivity::class.java)
            startActivity(i)
        }
        

        getUserDetails()
    }

    private fun getUserDetails() {
        database = FirebaseDatabase.getInstance()
        authenticationMP = FirebaseAuth.getInstance()

        val userRef = database.getReference("Users").child("Individual").child(authenticationMP.currentUser?.uid.toString())

        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                image = snapshot.child("image").getValue().toString()
                Picasso.get().load(image).into(profileImage)
                val headline = snapshot.child("headline").getValue()
                headlineMP.text = "$headline"
                val fullName = snapshot.child("fullName").getValue()
                fullNameMP.text = "$fullName"
                val userName = snapshot.child("userEmail").getValue()
                userNameMP.setText("$userName")
                val email = snapshot.child("userEmail").getValue()
                emailMP.setText("$email")
                val age = snapshot.child("age").getValue()
                ageMP.setText("$age")
                val contact = snapshot.child("contact").getValue()
                contactMP.setText("$contact")
                val addresses = snapshot.child("addresses").getValue()
                addressMP.setText("$addresses")
                val skills = snapshot.child("skills").getValue()
                skillsMP.setText("$skills")
                val education = snapshot.child("education").getValue()
                highestQualificationMP.setText("$education")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myProfileViewModel = ViewModelProvider(this).get(MyProfileViewModel::class.java)

        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textPost
//        myProfileViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}