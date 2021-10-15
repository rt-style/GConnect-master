package com.example.gconnect.ui.jobs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gconnect.R
import com.example.gconnect.ui.HomePage.JobPost
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class JobAdapter (val data: MutableList<JobPost>, val selectionDone: (JobPost)-> Unit) : RecyclerView.Adapter<JobAdapter.JobHolder>() {

    lateinit var db: FirebaseDatabase
    lateinit var auth: FirebaseAuth

    class JobHolder(v : View) : RecyclerView.ViewHolder(v){
        // wrapper of view

        //val iView = v.findViewById<ImageView>(R.id.imageView)
        //val thoughtsTextView = v.findViewById<TextView>(R.id.thoughtsT)

        val companyname = v.findViewById<TextView>(R.id.company_nameT)
        val jobtitle = v.findViewById<TextView>(R.id.job_titleT)
        val jobdescription = v.findViewById<TextView>(R.id.job_descriptionT)
        val joblocation = v.findViewById<TextView>(R.id.locationT)
        val jobvacancy = v.findViewById<TextView>(R.id.vacancyT)
        val applyButton = v.findViewById<Button>(R.id.applyB)
        val deleteButton = v.findViewById<Button>(R.id.deleteB)




    }


    //called by layoutManager
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobAdapter.JobHolder {
        //inflate layout xml
        //create a view holder and return
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.job_list_item, parent,false)
        return JobHolder(view)
    }


    override fun onBindViewHolder(holder: JobAdapter.JobHolder, position: Int) {
        db = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        // data binding
        val emp = data[position]
        // holder.thoughtsTextView.text = emp.thoughts
        holder.companyname.text = emp.companyName
        holder.jobtitle.text = emp.jobTitle
        holder.jobdescription.text = emp.jobDescription
        holder.joblocation.text = emp.location
        holder.jobvacancy.text = emp.vacancy.toString()

        val orgRef = db.getReference("Users").child("Organization").child(auth.currentUser?.uid.toString())
        val userref = db.getReference("Users").child("Individual").child(auth.currentUser?.uid.toString())
        val postRef = db.getReference("PostJob").child(emp.id.toString())


        userref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child("type").getValue() == "Individual" ) {
                    holder.applyButton.setEnabled(true)
                    holder.applyButton.setOnClickListener{
                        selectionDone(emp)
                    }
                    holder.deleteButton.setEnabled(false)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        orgRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child("type").getValue() == "Organization" && auth.currentUser?.uid == emp.companyId ) {
                    holder.applyButton.setEnabled(false)
                    holder.deleteButton.setEnabled(true)
                    holder.deleteButton.setOnClickListener {
                        postRef.removeValue()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })




    }

    override fun getItemCount(): Int {
        return data.size
    }


}