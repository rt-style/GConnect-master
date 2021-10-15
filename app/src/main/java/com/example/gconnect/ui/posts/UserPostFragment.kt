package com.example.gconnect.ui.posts

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gconnect.R
import com.example.gconnect.databinding.FragmentUserPostBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UserPostFragment : Fragment() {

    private lateinit var userPostViewModel: UserPostViewModel
    private var _binding: FragmentUserPostBinding?=null

    private val binding get() = _binding!!

    lateinit var postButton : ImageView
    lateinit var db: FirebaseDatabase
    lateinit var rView : RecyclerView
    var postlist = mutableListOf<ImageData>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userPostViewModel = ViewModelProvider(this).get(UserPostViewModel::class.java)

        _binding = FragmentUserPostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postButton = view.findViewById(R.id.postButton)
        rView = view.findViewById(R.id.rV)

        postButton.setOnClickListener {
            val i = Intent(activity,AddPostActivity::class.java)
            startActivity(i)
        }

        getImageData()
    }

    private fun getImageData() {
        db = FirebaseDatabase.getInstance()
        val stdref = db.getReference("Image")

        stdref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (usersnapshot in snapshot.children) {

                        val std = usersnapshot.getValue(ImageData::class.java)
                        postlist.add(std!!)
                    }
                    rView.layoutManager = LinearLayoutManager(context)
                    rView.setHasFixedSize(true)
                    rView.adapter = ImageAdapter(postlist)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}