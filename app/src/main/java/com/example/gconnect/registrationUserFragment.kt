package com.example.gconnect
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


data class Individual(val id : String? =null, val userName: String? = null, val userEmail: String? = null,
                      val headline: String? = null, val fullName: String? = null,
                      val age: Int = 0, val contact: Int = 0, val addresses: String? = null,
                      val education: String? = null, val skills: String? = null, val type: String = "Individual",
                      val image : String? = "https://firebasestorage.googleapis.com/v0/b/gconnect-f4b35.appspot.com/o/images%2F2021_10_13_18_01_59?alt=media&token=0dcc10b6-b7c0-430f-9c2f-cbeb92af2e20")


class login_user : Fragment() {


    lateinit var userNameEditText : EditText
    lateinit var userEmailEditText: EditText
    lateinit var userPasswordEditText: EditText
    lateinit var userSignUpButton: Button
    lateinit var databaseUser: FirebaseDatabase
    lateinit var authenticationUser: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authenticationUser = FirebaseAuth.getInstance()
        databaseUser = FirebaseDatabase.getInstance()

        userNameEditText = view.findViewById(R.id.editNameUser)
        userEmailEditText = view.findViewById(R.id.editEmailUser)
        userPasswordEditText = view.findViewById(R.id.editPassUser)
        userSignUpButton = view.findViewById(R.id.userSignUpB)

        userSignUpButton.setOnClickListener{
            addUser()
        }
    }
    private fun addUser() {

            val userName = userNameEditText.text.toString()
            val userEmail = userEmailEditText.text.toString()
            val userPassword = userPasswordEditText.text.toString()

            val databaseRef = databaseUser.getReference("Users").child("Individual")

            when {
                userName.isEmpty() -> userNameEditText.setError("Username Missing!")
                userEmail.isEmpty() -> userEmailEditText.setError("Email Missing!")
                userPassword.isEmpty() -> userPasswordEditText.setError("Password Missing!")
                else -> {
                    authenticationUser.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener {task ->
                        if (task.isSuccessful) {
                            val currUser = authenticationUser.currentUser
                            currUser?.sendEmailVerification()?.addOnSuccessListener {
                                val Uid = authenticationUser.currentUser?.uid
                                val individualUser = Individual(Uid,userName,userEmail,"","",0,0,"","","")
                                databaseRef.child(individualUser.id.toString()).setValue(individualUser)
                                Log.d("RegistrationActivity","$userEmail $userPassword $Uid")
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