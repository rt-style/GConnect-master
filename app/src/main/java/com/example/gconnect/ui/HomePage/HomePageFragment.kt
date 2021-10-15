package com.example.gconnect.ui.HomePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gconnect.Individual
import com.example.gconnect.R
import com.example.gconnect.databinding.FragmentHomePageBinding
import com.example.gconnect.databinding.FragmentJobsBinding
import com.example.gconnect.ui.jobs.JobAdapter

import com.example.gconnect.ui.list.Notification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class JobPost (val id : String? = null, val companyName :String? =null, val jobTitle : String? = null,
                    val jobDescription : String? =null, val location : String ? =null, val vacancy : Int =0, val companyId: String? =null)


class HomePageFragment : Fragment() {

    lateinit var db: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    var postlist = mutableListOf<JobPost>()
    lateinit var rView: RecyclerView
    lateinit var individualObj : Individual
    private lateinit var homePageViewModel: HomePageViewModel
    private var _binding: FragmentHomePageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homePageViewModel =
            ViewModelProvider(this).get(HomePageViewModel::class.java)

        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onResume() {
        super.onResume()
        postlist.clear()
        getUserData()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        rView = view.findViewById(R.id.rView)

    }
    private fun applyJob(obj: JobPost){
        val currentUser = auth.currentUser?.uid
        val notificationNode = db.getReference("Users").child("Notification")
        val key = notificationNode.push().key
        val individualUser = Notification(key,currentUser,"${obj.companyId}",auth.currentUser?.email
            ,"${auth.currentUser?.email} has applied for ${obj.jobTitle}")

        notificationNode.child(individualUser.eventId.toString()).setValue(individualUser)
    }

    private fun getUserData() {

        db = FirebaseDatabase.getInstance()
        val stdref = db.getReference("PostJob")

        stdref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (usersnapshot in snapshot.children) {

                        val std = usersnapshot.getValue(JobPost::class.java)
                        postlist.add(std!!)
                    }
                    rView.layoutManager = LinearLayoutManager(context)
                    rView.setHasFixedSize(true)
                    rView.adapter = JobAdapter(postlist,::applyJob)

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