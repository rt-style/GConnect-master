package com.example.gconnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


data class Company(val id : String? =null, val userName: String? = null, val userEmail: String? = null,
                   val headline: String? = null, val address: String? = null,
                   val type: String = "Organization", val image : String = "https://firebasestorage.googleapis.com/v0/b/gconnect-f4b35.appspot.com/o/images%2F2021_10_14_12_54_11?alt=media&token=2b7b45c2-549e-4f09-bb25-ada0c12e0100")


class login_organization : Fragment() {

    lateinit var OrgNameEditText : EditText
    lateinit var OrgEmailEditText: EditText
    lateinit var OrgPasswordEditText: EditText
    lateinit var OrgSignUpButton: Button
    lateinit var databaseOrg: FirebaseDatabase
    lateinit var authenticationOrg: FirebaseAuth
    private var count: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration_organization, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authenticationOrg = FirebaseAuth.getInstance()
        databaseOrg = FirebaseDatabase.getInstance()

        OrgNameEditText = view.findViewById(R.id.editNameOrg)
        OrgEmailEditText = view.findViewById(R.id.editEmailOrg)
        OrgPasswordEditText = view.findViewById(R.id.editPassOrg)
        OrgSignUpButton = view.findViewById(R.id.orgSignUpB)

        OrgSignUpButton.setOnClickListener {
            addOrganization()
        }
    }

    private fun addOrganization() {
        val orgName = OrgNameEditText.text.toString()
        val orgEmail = OrgEmailEditText.text.toString()
        val orgPassword = OrgPasswordEditText.text.toString()

        val orgDatabase = databaseOrg.getReference("Users").child("Organization")

        when {
            orgName.isEmpty() -> OrgNameEditText.setError("Name Missing!")
            orgEmail.isEmpty() -> OrgEmailEditText.setError("Email Missing!")
            orgPassword.isEmpty() -> OrgPasswordEditText.setError("Password Missing!")
            else -> {
                authenticationOrg.createUserWithEmailAndPassword(orgEmail, orgPassword).addOnCompleteListener { task ->

                    if(task.isSuccessful) {
                        val currOrg = authenticationOrg.currentUser
                        currOrg?.sendEmailVerification()?.addOnSuccessListener {
                            Toast.makeText(activity, "Email send successfully", Toast.LENGTH_SHORT).show()
                            val Oid = authenticationOrg.currentUser?.uid
                            val individualOrg = Company(authenticationOrg.currentUser?.uid,orgName, orgEmail, "","")
                            orgDatabase.child(individualOrg.id.toString()).setValue(individualOrg)
                            Log.d("RegistrationActivity","$orgEmail $orgPassword $Oid")
                            val i = Intent(activity,LoginActivity::class.java)
                            startActivity(i)
                        }
                            ?.addOnFailureListener { e ->
                                Toast.makeText(activity, "Email not send", Toast.LENGTH_SHORT).show()
                            }
                    }

                }
            }
        }

    }
}