package com.example.gconnect.ui.Connections

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.gconnect.R
import com.example.gconnect.ui.Notification.ConnectedMembers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

data class ChatMessages(val id: String? = null, val fromId: String? = null,
                        val toId: String? = null, val textMsg: String? = null, val name: String?=null)

class ChatLogActivity : AppCompatActivity() {
//        val adapter = GroupAdapter<ViewHolder>()
        lateinit var rView : RecyclerView
        lateinit var sendButton: Button
        lateinit var editText: EditText
        lateinit var dbUser : FirebaseDatabase
        lateinit var authUser : FirebaseAuth
        lateinit var connObj : ConnectedMembers
        var connectChatList = mutableListOf<ChatMessages>()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_chat_log)

            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
            dbUser = FirebaseDatabase.getInstance()
            authUser = FirebaseAuth.getInstance()

            sendButton = findViewById(R.id.send_button_chat_log)
            editText = findViewById(R.id.edittext_chat_log)
            rView = findViewById(R.id.recyclerview_chat_log)

            connObj = intent.getParcelableExtra("objConnectedMembers")!!

//            recyclerview_chat_log.adapter = adapter
            if(authUser.currentUser?.uid == connObj.toUid) {
                supportActionBar?.title = connObj?.sender
            }
            else{
                supportActionBar?.title = connObj?.receiver
            }

            connectChatList = arrayListOf()



        }

    override fun onResume() {
        super.onResume()
        connectChatList.clear()
        performReadMessages()
    }

    fun SendMessageClick(view: View) {
        addChatMessages()
        editText.setText("")
        connectChatList.clear()
    }


    private fun performReadMessages() {
        val readReference = dbUser.getReference("ChatMessages").child(connObj.fromUid.toString()).child(connObj.toUid.toString())
        readReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    val msg = it.getValue(ChatMessages::class.java)
                    connectChatList.add(msg!!)
                    Log.d("ChatLog", "$connectChatList")
                }
                rView.adapter = ChatLogAdapter(connectChatList, connObj)
            }



            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun addChatMessages() {
        val sendReference = dbUser.getReference().child("ChatMessages").child(connObj.fromUid.toString()).child(connObj.toUid.toString())
        var fullName :String = ""
        if(authUser.currentUser?.uid == connObj.toUid) {
            fullName = connObj.receiver.toString()
        }
        else{
            fullName = connObj.sender.toString()
        }
        val message = ChatMessages(sendReference?.push()?.key, connObj.fromUid, connObj.toUid, editText.text.toString(),fullName)
        sendReference.child(message.id.toString()).setValue(message)
    }



    }
