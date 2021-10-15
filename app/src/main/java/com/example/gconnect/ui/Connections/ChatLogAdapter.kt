package com.example.gconnect.ui.Connections

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gconnect.R
import com.example.gconnect.ui.Notification.ConnectedMembers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ChatLogAdapter(val chatUserData: MutableList<ChatMessages>, val connObj: ConnectedMembers): RecyclerView.Adapter<ChatLogAdapter.ConnectionHolder>() {

    private lateinit var database : FirebaseDatabase
    private lateinit var auth : FirebaseAuth

    class ConnectionHolder(v: View): RecyclerView.ViewHolder(v) {
        val chatMsg = v.findViewById<TextView>(R.id.chatMessageT)
        val chatName = v.findViewById<TextView>(R.id.chatNameT)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatLogAdapter.ConnectionHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_list_item, parent,false)
        return ConnectionHolder(view)
    }

    override fun onBindViewHolder(holder: ChatLogAdapter.ConnectionHolder, position: Int) {
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        val connectedData = chatUserData[position]
        holder.chatName.text = connectedData.name
        holder.chatMsg.text = connectedData.textMsg
    }

    override fun getItemCount(): Int {
        return chatUserData.size
    }

}