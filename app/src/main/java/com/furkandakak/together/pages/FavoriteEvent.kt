package com.furkandakak.together.pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.furkandakak.together.adapter.FavoriteEventRecyclerAdapter
import com.furkandakak.together.databinding.ActivityFavoriteEventBinding
import com.furkandakak.together.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FavoriteEvent : AppCompatActivity() {
    //database
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var postArrayList:ArrayList<Post>
    private lateinit var mainAdapter: FavoriteEventRecyclerAdapter
    //database


    private lateinit var binding:ActivityFavoriteEventBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFavoriteEventBinding.inflate(layoutInflater)
        val view= binding.root
        setContentView(view)

        //database
        auth= Firebase.auth
        db= Firebase.firestore

        postArrayList=ArrayList<Post>()
        //database
        getData()



        //recycler view link
        binding.recyclerView.layoutManager= LinearLayoutManager(this)
        mainAdapter= FavoriteEventRecyclerAdapter(postArrayList)
        binding.recyclerView.adapter=mainAdapter
        //recycler view link
    }

    //get data from firabase
    private fun getData()
    {
        var current_user=auth.currentUser!!.uid.toString()
        db.collection("Created").whereArrayContains("favoriteUser",current_user).orderBy("sortEventDate",Query.Direction.ASCENDING).addSnapshotListener { value, error ->
            if (error != null) {
                // Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
            }
            else
            {
                if (value != null)
                {
                    if(!value.isEmpty)
                    {
                        val documents =value.documents

                        postArrayList.clear()
                        for (document in documents)
                        {
                            val category = document.get("category") as String
                            val city = document.get("city") as String
                            val country = document.get("country") as String
                            val detail = document.get("detail") as String
                            val header = document.get("header") as String
                            val nick = document.get("nick") as String
                            val documentId=document.get("documentId") as String
                            val eventDate=document.get("eventDate") as? String
                            val favCount=document.get("favCount") as String
                            val joinCount=document.get("joinCount") as String
                            val post= Post(category,city,country,detail,header,nick,documentId, eventDate.toString(),favCount,joinCount)
                            postArrayList.add(post)

                        }
                        mainAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    //get data from firabase

    //go to main page
    fun fav_to_home(view: View)
    {
        val intent= Intent(this, MainPage::class.java)
        startActivity(intent)
    }
}