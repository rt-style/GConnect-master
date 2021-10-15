package com.example.gconnect.OrgProfile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.gconnect.OrgEditProfileActivity
import com.example.gconnect.R
import com.example.gconnect.databinding.FragmentMyProfileBinding
import com.example.gconnect.databinding.FragmentOrgProfileBinding
import com.example.gconnect.ui.MyProfile.MyProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class OrgProfileFragment : Fragment() {

    private lateinit var orgHeadlineMP : TextView
    private lateinit var orgNameMP : TextView
    private lateinit var orgEmailMP : TextView
    private lateinit var orgAddressMP : TextView
    private lateinit var orgEditButton : ImageView
    private lateinit var orgProfileImage : ImageView
    lateinit var orgImage : String

    private lateinit var database: FirebaseDatabase
    private lateinit var authenticationMP : FirebaseAuth

    private lateinit var myProfileViewModel: MyProfileViewModel
    private var _binding: FragmentOrgProfileBinding?=null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myProfileViewModel = ViewModelProvider(this).get(MyProfileViewModel::class.java)

        _binding = FragmentOrgProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orgHeadlineMP = view.findViewById(R.id.org_headlineMP)
        orgNameMP = view.findViewById(R.id.orgNameMP)
        orgAddressMP = view.findViewById(R.id.orgAddressMP)
        orgEmailMP = view.findViewById(R.id.orgEmailMP)
        orgEditButton = view.findViewById(R.id.org_edit_profile_button)
        orgProfileImage = view.findViewById(R.id.imageview_org_profile_picture)

        orgEditButton.setOnClickListener{
            val i = Intent(activity, OrgEditProfileActivity::class.java)
            startActivity(i)
        }

        getOrgDetails()
    }



    private fun getOrgDetails(){
        database = FirebaseDatabase.getInstance()
        authenticationMP = FirebaseAuth.getInstance()

        val orgRef = database.getReference("Users").child("Organization").child(authenticationMP.currentUser?.uid.toString())

        orgRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                orgImage = snapshot.child("image").getValue().toString()
                Picasso.get().load(orgImage).into(orgProfileImage)

                val headline = snapshot.child("headline").getValue()
                orgHeadlineMP.text = "$headline"
                val userName = snapshot.child("userName").getValue()
                orgNameMP.setText("$userName")
                val addresses = snapshot.child("address").getValue()
                orgAddressMP.setText("$addresses")
                val email = snapshot.child("userEmail").getValue()
                orgEmailMP.setText("$email")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }









}




















