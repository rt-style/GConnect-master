package com.example.gconnect

import android.app.AlertDialog
import android.content.Intent
import android.icu.lang.UCharacter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class UserLoginFragment : Fragment() {

    lateinit var individualForgotTextView: TextView

    lateinit var userEmailEditText: EditText

    lateinit var userPasswordEditText: EditText

    lateinit var userButtonLogin: Button

    lateinit var userDb: FirebaseDatabase
    lateinit var userAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        individualForgotTextView = view.findViewById(R.id.forgot_individualT)
        userEmailEditText = view.findViewById(R.id.email_individualE)
        userPasswordEditText = view.findViewById(R.id.password_individualE)
        userButtonLogin = view.findViewById(R.id.login_individualB)

        userAuth = FirebaseAuth.getInstance()
        userDb = FirebaseDatabase.getInstance()

        userButtonLogin.setOnClickListener {
            val userEmail = userEmailEditText.text.toString()
            val userPassword = userPasswordEditText.text.toString()

            when {
                userEmail.isEmpty() -> userEmailEditText.setError("Email Missing!")
                userPassword.isEmpty() -> userPasswordEditText.setError("Password Missing!")
                else -> {
                    userAuth.signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userRef = userDb.getReference("Users").child("Individual")
                                    .child(userAuth.currentUser?.uid.toString())
                                userRef.addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (userAuth.currentUser!!.isEmailVerified && snapshot.child(
                                                "type"
                                            ).getValue() == "Individual"
                                        ) {
                                            val i =
                                                Intent(activity, UserMainPageActivity::class.java)
                                            startActivity(i)
                                        } else {
                                            userAuth.signOut()
                                            if (snapshot.child("type").getValue() == "Individual") {
                                                Toast.makeText(
                                                    activity,
                                                    "Please verify from mail first",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                Toast.makeText(
                                                    activity,
                                                    "Use Organization Login",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }


                                })


                            } else {
                                Toast.makeText(activity, "Invalid credentials", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                }
            }
        }
        individualForgotTextView.setOnClickListener {
//            Toast.makeText(activity, "Forgot password for individual", Toast.LENGTH_SHORT).show()

            val resetMail = EditText(it.context)
            val passwordResetDialog = AlertDialog.Builder(it.context)
            passwordResetDialog.setTitle("Reset Password")
            passwordResetDialog.setMessage("Enter your email to received reset link")
            passwordResetDialog.setView(resetMail)
            passwordResetDialog.setPositiveButton("Yes") { _, _ ->
                val mail = resetMail.text.toString()
                if (mail.isEmpty()) {
                    Toast.makeText(activity, "Please enter your email address", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    userAuth.sendPasswordResetEmail(mail).addOnSuccessListener {
                        Toast.makeText(
                            activity,
                            "Resent link sent to your email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                activity,
                                "Something went wrong! Link not sent",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
            passwordResetDialog.setNegativeButton("No") { _, _ ->
                //back
            }
            passwordResetDialog.create().show()


        }

    }
}