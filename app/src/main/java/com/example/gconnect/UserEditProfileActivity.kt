package com.example.gconnect

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.InetAddresses
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class UserEditProfileActivity : AppCompatActivity() {
    var filepath : Uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/gconnect-f4b35.appspot.com/o/images%2F2021_10_13_18_01_59?alt=media&token=0dcc10b6-b7c0-430f-9c2f-cbeb92af2e20")
    var image : Uri? = null
    lateinit var imgV : ImageView


    private lateinit var database : FirebaseDatabase
    private lateinit var authentication : FirebaseAuth

    private lateinit var headlineEditText : EditText
    private lateinit var fullNameEditText : EditText
    private lateinit var ageEditText: EditText
    private lateinit var contactEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var educationEditText: EditText
    private lateinit var skillsEditText: EditText
    private lateinit var saveChanges : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit_profile)

        imgV =findViewById(R.id.user_picture_editprofile)
        headlineEditText = findViewById(R.id.user_headline_Editprofile)
        fullNameEditText = findViewById(R.id.user_fname_editprofile)
        ageEditText = findViewById(R.id.user_age_editprofileE)
        contactEditText = findViewById(R.id.user_contact_editprofileE)
        addressEditText = findViewById(R.id.user_address_editprofileE)
        educationEditText = findViewById(R.id.user_educa_ediitprofileE)
        skillsEditText = findViewById(R.id.user_skills_editprofileE)
        saveChanges = findViewById(R.id.saveEditUserProfileB)

        database = FirebaseDatabase.getInstance()
        authentication = FirebaseAuth.getInstance()

        imgV.setOnClickListener{
            startFilechooser()
        }

        getUserListData()
    }

    fun saveChangesClick(view: View) {

        val headline = headlineEditText.text.toString()
        val fullName = fullNameEditText.text.toString()
        val age = ageEditText.text.toString()
        val contact = contactEditText.text.toString()
        val address = addressEditText.text.toString()
        val education = educationEditText.text.toString()
        val skills = skillsEditText.text.toString()

        val auth = authentication.currentUser?.uid

        val databaseRef = database.getReference("Users").child("Individual").child(auth.toString())

        uploadImage()

        when{
            headline.isEmpty() -> headlineEditText.setError("Field Missing")
            fullName.isEmpty() -> fullNameEditText.setError("Field Missing")
            age.isEmpty() -> ageEditText.setError("Field Missing")
            contact.isEmpty() -> contactEditText.setError("Field Missing")
            address.isEmpty() -> addressEditText.setError("Field Missing")
            education.isEmpty() -> educationEditText.setError("Field Missing")
            skills.isEmpty() -> skillsEditText.setError("Field Missing")
            else -> {
                databaseRef.child("headline").setValue(headline)
                databaseRef.child("fullName").setValue(fullName)
                databaseRef.child("age").setValue(age)
                databaseRef.child("contact").setValue(contact)
                databaseRef.child("addresses").setValue(address)
                databaseRef.child("education").setValue(education)
                databaseRef.child("skills").setValue(skills)
            }
        }
    }


    private fun uploadImage() {

        val pd = ProgressDialog(this)
        pd.setTitle("Uploading")
        pd.show()

        val auth = authentication.currentUser?.uid
        val databaseRef = database.getReference("Users").child("Individual").child(auth.toString())


        val formatter = java.text.SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val imageRef = FirebaseStorage.getInstance().reference.child("images/$fileName")
        imageRef.putFile(filepath!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {
                    image=it

                    database = FirebaseDatabase.getInstance()
                    val stdref = database.getReference("Image")
                    val imageid = image.toString()
                    databaseRef.child("image").setValue(imageid)

                }

                pd.dismiss()
                Toast.makeText(applicationContext,"File  Uploaded", Toast.LENGTH_LONG).show()

            }.addOnFailureListener{p0 ->
                //Toast.makeText(applicationContext,p0.message, Toast.LENGTH_LONG).show()

            }.addOnProgressListener { p0->
                var progress = (100.0 * p0.bytesTransferred)/ p0.totalByteCount
                pd.setMessage("Uploaded ${progress.toInt()}%")
            }

    }










    private fun startFilechooser() {

        val i = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(i,"Choose Picture"),111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==111 &&resultCode == Activity.RESULT_OK && data !=null){

            filepath = data.data!!
            var bitmap : Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filepath)
            imgV.setImageBitmap(bitmap)

//            filepath = data.data!!
//            var bitmap : Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filepath)
//
//
//            val bitmapDrawable = BitmapDrawable(bitmap)
//            imgV.setBackgroundDrawable(bitmapDrawable)
        }
    }


    ///setting the data in the edit text
    private fun getUserListData() {
        database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("Users").child("Individual").child(authentication.currentUser?.uid.toString())

        userRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val headline = snapshot.child("headline").getValue()
                headlineEditText.setText("$headline")
                val fullName = snapshot.child("fullName").getValue()
                fullNameEditText.setText("$fullName")
                val age = snapshot.child("age").getValue()
                ageEditText.setText("$age")
                val contact = snapshot.child("contact").getValue()
                contactEditText.setText("$contact")
                val addresses = snapshot.child("addresses").getValue()
                addressEditText.setText("$addresses")
                val skills = snapshot.child("skills").getValue()
                skillsEditText.setText("$skills")
                val education = snapshot.child("education").getValue()
                educationEditText.setText("$education")
                Log.d("UserEditProfile","$headline $fullName $age $contact $addresses $education $skills")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}