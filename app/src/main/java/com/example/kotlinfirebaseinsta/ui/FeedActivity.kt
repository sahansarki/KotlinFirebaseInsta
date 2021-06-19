package com.example.kotlinfirebaseinsta.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinfirebaseinsta.FireBaseService.FirebaseLogic.db
import com.example.kotlinfirebaseinsta.Model.User
import com.example.kotlinfirebaseinsta.adapter.FeedRecyclerAdapter
import com.example.kotlinfirebaseinsta.R
import com.example.kotlinfirebaseinsta.viewmodel.FeedActivityViewModel
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.recyler_view_row.view.*
import java.io.Serializable

class FeedActivity : AppCompatActivity() {

    private lateinit var feedViewModel: FeedActivityViewModel

    private lateinit var feedAdapter: FeedRecyclerAdapter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.options_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.add_post) {

            val intent = Intent(applicationContext, UploadActivity::class.java)
            startActivity(intent)

        } else if (item.itemId == R.id.logout) {

            feedViewModel.signOut()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }

        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        feedViewModel = ViewModelProvider(this).get(FeedActivityViewModel::class.java)


        feedAdapter = FeedRecyclerAdapter(null, feedViewModel)

        val layoutManager = LinearLayoutManager(this)
        recylerview.layoutManager = layoutManager

        recylerview.adapter = feedAdapter
        feedViewModel.getDataFromFirestore()

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

                    if(exception != null) {
                        Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
                    }
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


