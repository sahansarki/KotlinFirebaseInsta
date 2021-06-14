package com.example.kotlinfirebaseinsta.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinfirebaseinsta.FireBaseService.FirebaseLogic
import com.example.kotlinfirebaseinsta.FireBaseService.FirebaseLogic.db
import com.example.kotlinfirebaseinsta.Model.Post
import com.example.kotlinfirebaseinsta.Model.User
import com.example.kotlinfirebaseinsta.adapter.FeedRecyclerAdapter
import com.example.kotlinfirebaseinsta.R
import com.example.kotlinfirebaseinsta.viewmodel.FeedActivityViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.recyler_view_row.*
import kotlinx.android.synthetic.main.recyler_view_row.view.*
import java.io.Serializable

class FeedActivity : AppCompatActivity() {

    private lateinit var feedViewModel: FeedActivityViewModel

    //    var feedAdapter = FeedRecyclerAdapter(null, null)
    private lateinit var feedAdapter: FeedRecyclerAdapter
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInfllater = menuInflater
        menuInfllater.inflate(R.menu.options_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.add_post) {
            // Upload Activity
            val intent = Intent(applicationContext, UploadActivity::class.java)
            startActivity(intent)

        } else if (item.itemId == R.id.logout) {
            //Logout
//            auth.signOut()
            feedViewModel.signOut()
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()

        }

        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        feedViewModel = ViewModelProvider(this).get(FeedActivityViewModel::class.java)
        feedViewModel.getDataFromFirestore()

        feedAdapter = FeedRecyclerAdapter(null, feedViewModel)
        //recylerview ayarları
        val layoutManager = LinearLayoutManager(this)
        recylerview.layoutManager = layoutManager

        recylerview.adapter = feedAdapter

        observeLiveData()


    }


    private fun observeLiveData() {
        feedViewModel.userPosts.observe({
            lifecycle
        }, { Post ->
            Post?.let {
                feedAdapter.post = Post
                feedAdapter.updatePost(Post)


            }

        })

        feedViewModel.userView.observe({
            lifecycle
        }, { view ->

            view?.let { View ->

                val intent = Intent(application, ProfileActivity::class.java)
                val email = View.recyclerEmailText.text.toString()

                db.collection("Users").addSnapshotListener { snapshot, exception ->
                    if (snapshot != null) {
                        if (!snapshot.isEmpty) {
                            val documents = snapshot.documents
                            for (document in documents) {
                                if(document.get("email") as String == email) {
                                    val user = User(email,document.get("username") as String,"",document.get("photoUrl") as String)
                                    intent.putExtra("user", user as Serializable)
                                    startActivity(intent)

                                }
                            }
                        }
                    }
                }
            }

    })

}


}


