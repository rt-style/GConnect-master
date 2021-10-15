package com.example.gconnect.ui.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gconnect.Individual
import com.example.gconnect.R


class UserAdapter(val userData: MutableList<Connection>, val selectionDone: (Connection) -> Unit): RecyclerView.Adapter<UserAdapter.ConnectionHolder>() {

    class ConnectionHolder(v: View): RecyclerView.ViewHolder(v){
        //wrapper of view
        val iView = v.findViewById<ImageView>(R.id.connect_imageView)
        val nametextView = v.findViewById<TextView>(R.id.connect_fname)
//        val headlineTextView = v.findViewById<TextView>(R.id.connect_headline)
        val headlineTextView = v.findViewById<TextView>(R.id.connect_skills)
        val connectB = v.findViewById<Button>(R.id.connectB)
    }




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserAdapter.ConnectionHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent,false)
        return ConnectionHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.ConnectionHolder, position: Int) {
        val emp = userData[position]
        holder.nametextView.setText(emp.fullName)
//        holder.headlineTextView.setText(emp.headline)
        holder.headlineTextView.setText(emp.headline)
//        holder.iView.setImageResource(emp.image)



        holder.connectB.setOnClickListener {
            Log.d("CheckOnData", "${emp.id.toString()},${emp.fullName}")
            selectionDone(emp)
        }
    }

    override fun getItemCount(): Int {
        return userData.size
    }


}