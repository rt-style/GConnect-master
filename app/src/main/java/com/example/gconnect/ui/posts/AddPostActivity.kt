package com.example.gconnect.ui.posts

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.gconnect.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

data class ImageData (val imgid : String ?=null
                      , val description: String?=null)

class AddPostActivity : AppCompatActivity() {


    lateinit var db: FirebaseDatabase
    lateinit var imageButton: Button
    lateinit var description: EditText
    var dataReference: DatabaseReference? = null
    var imageUri: Uri? = null
    var image: Uri? = null
    lateinit var post_image: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        post_image = findViewById(R.id.postYourImage)
        imageButton = findViewById(R.id.imageanddescriptionB)
        description = findViewById(R.id.description)
        imageButton.setOnClickListener {
            uploadImage()
            finish()
        }

        post_image.setOnClickListener {
            startFilechooser()
        }
    }


    private fun uploadImage() {

        val formatter = java.text.SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)

        val imageRef = FirebaseStorage.getInstance().reference.child("images/$fileName")

        imageRef.putFile(imageUri!!)

            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {
                    image = it
                    var th = description.text.toString()
                    if(th.isEmpty()){
                        th = ""
                    }
                    db = FirebaseDatabase.getInstance()
                    val stdref = db.getReference("Image")
                    val imageid = image.toString()
                    val img = ImageData(imageid,th)
                    stdref.child(fileName).setValue(img)
                }
                Toast.makeText(applicationContext,"File  Uploaded", Toast.LENGTH_LONG).show()
            }.addOnFailureListener{p0 ->
                Toast.makeText(applicationContext,p0.message, Toast.LENGTH_LONG).show()
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
        if(requestCode ==111 &&resultCode == RESULT_OK && data !=null){

            imageUri= data.data!!
            var bitmap : Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageUri)
            post_image.setImageBitmap(bitmap)

        }
    }


}