package com.example.kotlinfirebaseinsta.adapter

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinfirebaseinsta.FireBaseService.FirebaseLogic
import com.example.kotlinfirebaseinsta.Model.Post
import com.example.kotlinfirebaseinsta.R
import com.example.kotlinfirebaseinsta.ui.ProfileActivity
import com.example.kotlinfirebaseinsta.viewmodel.FeedActivityViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyler_view_row.view.*

class FeedRecyclerAdapter(var post: ArrayList<Post>?, var feedViewModel: FeedActivityViewModel?) :
    RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>() {


    class PostHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recyler_view_row, parent, false)

        return PostHolder(view)
    }

    override fun getItemCount(): Int {
        return post?.size ?: 0
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {


        holder.itemView.recyclerCommentText?.text =
            if (post != null) post!![position].comment else ""

        if (post != null) {


            FirebaseLogic.db.collection("Users").addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    if (!snapshot.isEmpty) {
                        val documents = snapshot.documents
                        for (document in documents) {
                            if (document.get("email") as String == post!![position].userEmail) {
                                Picasso.get().load(document.get("photoUrl") as String)
                                    .into(holder.itemView.recyclerUserPhoto)
                                holder.itemView.recyclerCommentName?.text =
                                    document.get("username") as String
                                holder.itemView.recyclerEmailText?.text =
                                    document.get("email") as String
                            }
                        }
                    }
                }
            }


            Picasso.get().load(post!![position].downloadUrl).into(holder.itemView.recyclerImageView)
        } else {
            Picasso.get()
                .load("https://firebasestorage.googleapis.com/v0/b/kotlinfirebaseinsta-43eca.appspot.com/o/images%2Fdefec551-edb3-4571-a5c0-f3ee5820a4c4.jpg?alt=media&token=68d50e45-b295-4a8e-bef7-6347b4c5fdcb")
                .into(holder.itemView.recyclerImageView)
        }

        holder.itemView.recyclerEmailText.setOnClickListener {
//            println(it.recyclerCommentName.text)
            feedViewModel?.userView?.value = it

        }




    }

    /*
    val l: Int = if (b != null) b.length else -1!
    val l = b?.length ?: -1
     */

    fun updatePost(newPosts: ArrayList<Post>) {


        if (post != null) {
            post = newPosts
            notifyDataSetChanged()
        }

    }


}