package com.example.gconnect.ui.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gconnect.Individual
import com.example.gconnect.R
import com.example.gconnect.databinding.FragmentUserListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


data class  Connection ( val id: String? = null, val fullName: String? =null, var headline: String?=null)

data class Notification( val eventId: String? = null, val fromUid : String? = null, val toUid: String? = null,
                        val fromEmail: String? = null, val message: String? = null, val sender: String? = null,
                         val receiver : String? = null )


class UserListFragment : Fragment() {

    private lateinit var userListViewModel: UserListViewModel
    private var _binding: FragmentUserListBinding? = null
    lateinit var tempArrayList :ArrayList<Connection>


    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!
    lateinit var rView : RecyclerView
    var connectList = mutableListOf<Connection>()
    private lateinit var database : FirebaseDatabase
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        userListViewModel =
            ViewModelProvider(this).get(UserListViewModel::class.java)

        _binding =FragmentUserListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tempArrayList = arrayListOf<Connection>()
        rView = requireView().findViewById(R.id.userListRView)
        rView.layoutManager = GridLayoutManager(activity, 2)
        rView.setHasFixedSize(true)
        connectList = arrayListOf()

        getUserListData()
    }
    private fun notification(selectedUser : Connection) {

        val stdRef = database.getReference("Users").child("Individual").child("${auth.currentUser?.uid}")

        stdRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val fullName = snapshot.child("fullName").getValue()
                val key = stdRef?.push()?.key
                val notificationNode = database.getReference("Users").child("Notification")
                val individualUser = Notification(key ,auth.currentUser?.uid, selectedUser.id.toString()
                    ,auth.currentUser?.email,"$fullName is Trying to connect with you","$fullName", "${selectedUser.fullName}")
                notificationNode.child(individualUser.eventId.toString()).setValue(individualUser)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getUserListData() {
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        val stdref = database.getReference("Users").child("Individual")

        stdref.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                   snapshot.children.forEach{
                       val std = it.getValue(Connection::class.java)
                       if(std?.id != auth.currentUser?.uid) {
                           tempArrayList.add(std!!)
                       }
                   }
                    rView.adapter = UserAdapter(tempArrayList, ::notification)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    @SuppressLint("ResourceType")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.connection_search,menu)
        val item = menu.findItem(R.id.searchOption)
        val seachView = item.actionView as SearchView
        seachView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                tempArrayList.clear()
                val searchText  = newText!!.toLowerCase(Locale.getDefault())
                if(searchText.isNotEmpty()){
                    connectList.forEach{
                        if (it.fullName?.toLowerCase(Locale.getDefault())?.contains(searchText)!!){
                            tempArrayList.add(it)
                        }
                    }
                    rView.adapter?.notifyDataSetChanged()
                }
                else{
                    tempArrayList.clear()
                    tempArrayList.addAll(connectList)
                    rView.adapter?.notifyDataSetChanged()
                }
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}