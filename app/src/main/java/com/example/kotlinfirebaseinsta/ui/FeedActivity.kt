package com.example.kotlinfirebaseinsta.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinfirebaseinsta.FireBaseService.FirebaseLogic
import com.example.kotlinfirebaseinsta.Model.Post
import com.example.kotlinfirebaseinsta.adapter.FeedRecyclerAdapter
import com.example.kotlinfirebaseinsta.R
import com.example.kotlinfirebaseinsta.viewmodel.FeedActivityViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {

    private lateinit var feedViewModel : FeedActivityViewModel

    var feedAdapter = FeedRecyclerAdapter(null)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInfllater = menuInflater
        menuInfllater.inflate(R.menu.options_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if ( item.itemId == R.id.add_post) {
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

        //recylerview ayarları
        val layoutManager = LinearLayoutManager(this)
        recylerview.layoutManager = layoutManager

        recylerview.adapter = feedAdapter

        observeLiveData()

    }

    private fun observeLiveData() {
        feedViewModel.userPosts.observe({
            lifecycle
        } , { Post ->
            Post?.let {
                feedAdapter.post = Post
                feedAdapter.updatePost(Post)

            }

        })
    }



}


