package com.example.gconnect.ui.Connections

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gconnect.R
import com.example.gconnect.ToSeeConnectedUsersProfile
import com.example.gconnect.ui.Notification.ConnectedMembers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ConnectedUsersAdapter(val connectedUserData: MutableList<ConnectedMembers>, val selectionDone: (ConnectedMembers)-> Unit): RecyclerView.Adapter<ConnectedUsersAdapter.ConnectionHolder>() {
    private lateinit var database : FirebaseDatabase
    private lateinit var auth : FirebaseAuth



    class ConnectionHolder(v : View): RecyclerView.ViewHolder(v) {
        val imageView = v.findViewById<ImageView>(R.id.imageViewCU)
        val fullName = v.findViewById<TextView>(R.id.fullNameCU)

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConnectedUsersAdapter.ConnectionHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.connected_users_item, parent,false)
        return ConnectionHolder(view)
    }

    override fun onBindViewHolder(holder: ConnectedUsersAdapter.ConnectionHolder, position: Int) {
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        val connectedData = connectedUserData[position]
        if(auth.currentUser?.uid == connectedData.toUid) {
            holder.fullName.setText(connectedData.sender)
        }
        else{
            holder.fullName.setText(connectedData.receiver)
        }

        holder.itemView.setOnClickListener{
            selectionDone(connectedData)
        }

        //ContextMenu
        holder.itemView.setOnCreateContextMenuListener { contextMenu, view, contextMenuInfo ->
            contextMenu.add("View Profile").setOnMenuItemClickListener {
                //holder.itemView.context.startActivity(Intent(holder.itemView.context,ToSeeConnectedUsersProfile::class.java))
                val i = Intent(holder.itemView.context, ToSeeConnectedUsersProfile::class.java)
                i.putExtra("objConnectedMembers",connectedData)
                holder.itemView.context.startActivity(i)
                true
            }

            contextMenu.add("Block User").setOnMenuItemClickListener {
                val tempRef1 = database.getReference("Users").child("Individual")
                    .child(auth.currentUser?.uid.toString()).child("Connections")
                    .child(auth.currentUser?.uid.toString()).child(connectedData.fromUid.toString())
                tempRef1.removeValue()
                val tempRef2 = database.getReference("Users").child("Individual")
                    .child(auth.currentUser?.uid.toString()).child("Connections")
                    .child(auth.currentUser?.uid.toString()).child(connectedData.toUid.toString())
                tempRef2.removeValue()
                true
            }
        }

    }

    override fun getItemCount(): Int {
        return connectedUserData.size
    }

}