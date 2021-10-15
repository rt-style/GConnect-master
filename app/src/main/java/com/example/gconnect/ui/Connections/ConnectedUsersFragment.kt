package com.example.gconnect.ui.Connections

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gconnect.R
import com.example.gconnect.databinding.FragmentConnectedUsersBinding
import com.example.gconnect.databinding.FragmentUserListBinding
import com.example.gconnect.ui.Notification.ConnectedMembers
import com.example.gconnect.ui.list.Connection
import com.example.gconnect.ui.list.Notification
import com.example.gconnect.ui.list.UserListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ConnectedUsersFragment : Fragment() {

    private lateinit var userListViewModel: UserListViewModel
    private var _binding: FragmentConnectedUsersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var rView : RecyclerView
    var connectList = mutableListOf<ConnectedMembers>()
    private lateinit var database : FirebaseDatabase
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentConnectedUsersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rView = requireView().findViewById(R.id.connectedUsersRView)
        rView.layoutManager = LinearLayoutManager(activity)
        rView.setHasFixedSize(true)
        connectList = arrayListOf()

        getConnectedUsersList()
    }

    private fun getConnectedUsersList() {
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        val connectedUsers = database.getReference("Users").child("Individual")
            .child(auth.currentUser?.uid.toString()).child("Connections").child(auth.currentUser?.uid.toString())

        Log.d("Connect","$connectedUsers ")
        connectedUsers.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Connect", "Before for each")
                    snapshot.children.forEach{
                        val std = it.getValue(ConnectedMembers::class.java)
                        connectList.add(std!!)
                        Log.d("Connect","$connectList ")
                    }
                    rView.adapter = ConnectedUsersAdapter(connectList, :: onSelectUser)

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("Connect", "On Cancelled")
            }
        })
    }

    private fun onSelectUser(objUser: ConnectedMembers){
        val i = Intent(activity,ChatLogActivity::class.java)
        i.putExtra("objConnectedMembers",objUser)
        startActivity(i)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}