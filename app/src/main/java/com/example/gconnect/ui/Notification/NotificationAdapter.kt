package com.example.gconnect.ui.Notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gconnect.R
import com.example.gconnect.ui.list.Notification


class NotificationAdapter(val userData: MutableList<Notification>, val selectionReject: (Notification) -> Unit,
                          val selectionAccept: (Notification)-> Unit ): RecyclerView.Adapter<NotificationAdapter.ConnectionHolder>() {

    class ConnectionHolder(v: View): RecyclerView.ViewHolder(v){
        val email_Notification = v.findViewById<TextView>(R.id.emailN)
        val message_Notification = v.findViewById<TextView>(R.id.messageN)
        val accept_NotificationB = v.findViewById<Button>(R.id.acceptNotificationB)
        val reject_NotificationB = v.findViewById<Button>(R.id.rejectNotificationB)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.ConnectionHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent,false)
        return NotificationAdapter.ConnectionHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationAdapter.ConnectionHolder, position: Int) {
        val user = userData[position]
        holder.email_Notification.setText(user.fromEmail)
        holder.message_Notification.setText(user.message)

        holder.reject_NotificationB.setOnClickListener {
            selectionReject(user)
        }

        holder.accept_NotificationB.setOnClickListener {
            selectionAccept(user)
        }

    }


    override fun getItemCount(): Int {
        return userData.size
    }


}

