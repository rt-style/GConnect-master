package com.example.gconnect.ui.Notification


import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.versionedparcelable.VersionedParcelize
import com.example.gconnect.R
import com.example.gconnect.databinding.FragmentNotificationBinding
import com.example.gconnect.ui.list.Notification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.parcel.Parcelize

//
//data class userNotifications(val eventId: String? = null, val fromEmail: String? = null, val message: String? = null, val fromUid: String? = null, val toUid: String? = null )

@Parcelize
data class ConnectedMembers(val sender: String? = null, val receiver: String? = null,
                            val fromUid: String? = null, val toUid: String? = null ): Parcelable


class NotificationFragment : Fragment() {

    private lateinit var notificationViewModel: NotificationViewModel
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    lateinit var rView : RecyclerView
    var connectList = mutableListOf<Notification>()
    private lateinit var database : FirebaseDatabase
    private lateinit var auth : FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationViewModel =
            ViewModelProvider(this).get(NotificationViewModel::class.java)

        _binding =FragmentNotificationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rView = requireView().findViewById(R.id.notificationRView)
        rView.layoutManager = LinearLayoutManager(activity)
        rView.setHasFixedSize(true)
        connectList = arrayListOf()

        getNotifications()
    }


    private fun notificationReject(selectedReject: Notification){
        val rejectNotify = database.getReference("Users")
            .child("Notification").child(selectedReject.toUid.toString())

        rejectNotify.removeValue()
    }

    private fun notificationAccept(selectedAccept: Notification){
        val acceptFromNotify = database.getReference("Users").child("Individual")
            .child("${auth.currentUser?.uid}").child("Connections")

        val addFromConnection = ConnectedMembers(selectedAccept.sender ,selectedAccept.receiver ,selectedAccept.fromUid.toString(), auth.currentUser?.uid)
        acceptFromNotify.child(addFromConnection.toUid.toString()).child(addFromConnection.fromUid.toString()).setValue(addFromConnection)

        val acceptToNotify = database.getReference("Users").child("Individual")
            .child("${selectedAccept.fromUid.toString()}").child("Connections")

        val addToConnection = ConnectedMembers(selectedAccept.sender, selectedAccept.receiver ,selectedAccept.fromUid.toString(), auth.currentUser?.uid)
        acceptToNotify.child(addToConnection.fromUid.toString()).child(addToConnection.toUid.toString()).setValue(addToConnection)


        val removeNotify = database.getReference("Users")
            .child("Notification").child(selectedAccept.eventId.toString())
        removeNotify.removeValue()

    }


    override fun onResume() {
        super.onResume()
        connectList.clear()
    }

    private fun getNotifications() {
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        val stdref = database.getReference("Users").child("Notification")

        stdref.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach{
                        val std = it.getValue(Notification::class.java)
                        if(std?.fromUid != auth.currentUser?.uid && std?.toUid == auth.currentUser?.uid)
                            connectList.add(std!!)
                    rView.adapter = NotificationAdapter(connectList, ::notificationReject, ::notificationAccept)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}


