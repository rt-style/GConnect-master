package com.example.gconnect

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CompanyLoginFragment : Fragment() {
    lateinit var companyForgetTextView: TextView

    lateinit var companyEmailEditText: EditText
    lateinit var companyPasswordEditText: EditText
    lateinit var companySignUpButton: Button

    lateinit var companyAuth: FirebaseAuth
    lateinit var companyDb: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        companyEmailEditText = view.findViewById(R.id.email_companyE)
        companyPasswordEditText = view.findViewById(R.id.password_companyE)
        companySignUpButton = view.findViewById(R.id.login_companyB)
        companyForgetTextView = view.findViewById(R.id.forgot_companyT)

        companyAuth = FirebaseAuth.getInstance()
        companyDb = FirebaseDatabase.getInstance()

        companySignUpButton.setOnClickListener {
            val orgEmail = companyEmailEditText.text.toString()
            val orgPassword = companyPasswordEditText.text.toString()


            when {
                orgEmail.isEmpty() -> companyEmailEditText.setError("Email Missing!")
                orgPassword.isEmpty() -> companyPasswordEditText.setError("Password Missing!")
                else -> {
                    companyAuth.signInWithEmailAndPassword(orgEmail,orgPassword).addOnCompleteListener{task ->

                        if (task.isSuccessful) {
                            val orgRef = companyDb.getReference("Users").child("Organization").child(companyAuth.currentUser?.uid.toString())
                            orgRef.addValueEventListener(object: ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if(companyAuth.currentUser!!.isEmailVerified && snapshot.child("type").getValue() == "Organization"){
                                        val i = Intent(activity,OrganizationMainPageActivity::class.java)
                                        startActivity(i)
                                    }
                                    else{
                                        companyAuth.signOut()
                                        if (snapshot.child("type").getValue() == "Organization") {
                                            Toast.makeText(
                                                activity,
                                                "Please verify from mail first",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                activity,
                                                "Use Individual Login",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })

                        }
                        else {
                            Toast.makeText(activity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }


        }

        companyForgetTextView.setOnClickListener {
//            Toast.makeText(activity, "Forgot password for company", Toast.LENGTH_SHORT).show()

            val resetMail = EditText(it.context)
            val passwordResetDialog = AlertDialog.Builder(it.context)
            passwordResetDialog.setTitle("Reset Password")
            passwordResetDialog.setMessage("Enter your email to received reset link")
            passwordResetDialog.setView(resetMail)
            passwordResetDialog.setPositiveButton("Yes") {_,_ ->
                val mail = resetMail.text.toString()
                if(mail.isEmpty()){
                    Toast.makeText(activity, "Please enter your email address", Toast.LENGTH_SHORT).show()
                }
                else {
                    companyAuth.sendPasswordResetEmail(mail).addOnSuccessListener {
                        Toast.makeText(activity, "Resent link sent to your email", Toast.LENGTH_SHORT).show()
                    }
                        .addOnFailureListener { e ->
                            Toast.makeText(activity, "Something went wrong! Link not sent", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            passwordResetDialog.setNegativeButton("No") {_,_ ->
                //back
            }
            passwordResetDialog.create().show()
        }

    }

    }
