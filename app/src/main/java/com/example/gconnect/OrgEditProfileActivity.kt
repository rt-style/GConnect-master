package com.example.gconnect

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_org_profile.*
import java.util.*

class OrgEditProfileActivity : AppCompatActivity() {

    var filepath : Uri =  Uri.parse("https://firebasestorage.googleapis.com/v0/b/gconnect-f4b35.appspot.com/o/images%2F2021_10_14_12_54_11?alt=media&token=2b7b45c2-549e-4f09-bb25-ada0c12e0100")
    var image : Uri? = null
    lateinit var imgV : ImageView

    private lateinit var database : FirebaseDatabase
    private lateinit var authentication : FirebaseAuth

    private lateinit var headlineEditText : EditText
    private lateinit var orgNameEditText : EditText
    private lateinit var orgEmailEditText: EditText
    private lateinit var orgAddressEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_org_edit_profile)

        imgV = findViewById(R.id.imageview_orgedit_profile_picture)
        headlineEditText = findViewById(R.id.org_edit_headlineMP)
        orgNameEditText = findViewById(R.id.orgEditNameMP)
        orgAddressEditText = findViewById(R.id.orgEditAddressMP)
        orgEmailEditText = findViewById(R.id.orgEditEmailMP)

        database = FirebaseDatabase.getInstance()
        authentication = FirebaseAuth.getInstance()

        imgV.setOnClickListener {
            startFileChooser()
        }

        getOrgListData()

    }



    fun saveOrgChangesClick(view: View) {
        val headline = headlineEditText.text.toString()
        val orgName = orgNameEditText.text.toString()
        val orgEmail = orgEmailEditText.text.toString()
        val orgAddress = orgAddressEditText.text.toString()

        val auth = authentication.currentUser?.uid
        val databaseRef = database.getReference("Users").child("Organization").child(auth.toString())

        uploadOrgImage()

        when{
            headline.isEmpty() -> headlineEditText.setError("Field Missing")
            orgName.isEmpty() -> orgNameEditText.setError("Field Missing")
            orgEmail.isEmpty() -> orgEmailEditText.setError("Field Missing")
            orgAddress.isEmpty() -> orgAddressEditText.setError("Field Missing")
            else -> {
                databaseRef.child("headline").setValue(headline)
                databaseRef.child("userName").setValue(orgName)
                databaseRef.child("userEmail").setValue(orgEmail)
                databaseRef.child("address").setValue(orgAddress)
            }
        }

    }

    private fun uploadOrgImage(){
        val pd = ProgressDialog(this)
        pd.setTitle("Uploading")
        pd.show()

        val auth = authentication.currentUser?.uid
        val databaseRef = database.getReference("Users").child("Organization").child(auth.toString())


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
                Toast.makeText(applicationContext,p0.message, Toast.LENGTH_LONG).show()

            }.addOnProgressListener { p0->
                var progress = (100.0 * p0.bytesTransferred)/ p0.totalByteCount
                pd.setMessage("Uploaded ${progress.toInt()}%")
            }
    }



    private fun startFileChooser() { val i = Intent()
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


    private fun getOrgListData() {
        database = FirebaseDatabase.getInstance()
        val auth = authentication.currentUser?.uid
        val orgRef = database.getReference("Users").child("Organization").child(auth.toString())
        orgRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val headline = snapshot.child("headline").getValue()
                headlineEditText.setText("$headline")
                val orgName = snapshot.child("userName").getValue()
                orgNameEditText.setText("$orgName")
                val orgEmail = snapshot.child("userEmail").getValue()
                orgEmailEditText.setText("$orgEmail")
                val orgAddress = snapshot.child("address").getValue()
                orgAddressEditText.setText("$orgAddress")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}