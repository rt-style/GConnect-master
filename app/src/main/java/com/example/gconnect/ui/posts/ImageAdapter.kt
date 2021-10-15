package com.example.gconnect.ui.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gconnect.R
import com.jackandphantom.androidlikebutton.AndroidLikeButton
import com.squareup.picasso.Picasso

class ImageAdapter(val data: MutableList<ImageData>)  : RecyclerView.Adapter<ImageAdapter.imageHolder>() {

        class imageHolder(v : View) : RecyclerView.ViewHolder(v){
            // wrapper of view

            val iView = v.findViewById<ImageView>(R.id.imageView)
            val descriptionTextView = v.findViewById<TextView>(R.id.descriptionT)
            val likeButton = v.findViewById<AndroidLikeButton>(R.id.likeButton)

        }


        //called by layoutManager
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.imageHolder {
            //inflate layout xml
            //create a view holder and return
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.user_post_list_item, parent,false)
            return imageHolder(view)
        }


        override fun onBindViewHolder(holder: ImageAdapter.imageHolder, position: Int) {
            // data binding
            val emp = data[position]
            Picasso.get().load(emp.imgid).into(holder.iView)


            holder.descriptionTextView.text = emp.description

            holder.likeButton.setOnLikeEventListener(object : AndroidLikeButton.OnLikeEventListener {
                override fun onLikeClicked(androidLikeButton: AndroidLikeButton?) {

                    Toast.makeText(holder.itemView.context, "You liked this post", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onUnlikeClicked(androidLikeButton: AndroidLikeButton?) {
                    Toast.makeText(holder.itemView.context, "You unliked this post", Toast.LENGTH_SHORT)
                        .show()
                }

            })


        }

        override fun getItemCount(): Int {
            return data.size
        }

    }


