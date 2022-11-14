package com.furkandakak.together.pages

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.furkandakak.together.R
import com.furkandakak.together.adapter.DetailMainAdapter
import com.furkandakak.together.adapter.MainPageRecyclerAdapter
import com.furkandakak.together.databinding.ActivityJoinedEventDetailBinding
import com.furkandakak.together.model.Chat
import com.furkandakak.together.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class JoinedEventDetail : AppCompatActivity() {
    //database
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    //favorite and join count
    lateinit var get_size_fav: String
    lateinit var get_size_join:String

    //for favorite and join
    private lateinit var postArrayList:ArrayList<Post>
    private lateinit var mainAdapter2: MainPageRecyclerAdapter


    // scroll
    private lateinit var textview_detail: TextView
    private lateinit var textView_header: TextView

    //adapter link
    private lateinit var chatArrayList:ArrayList<Chat>
    private lateinit var mainAdapter: DetailMainAdapter
    //adapter link

    private lateinit var binding: ActivityJoinedEventDetailBinding
    lateinit var get_doc_id_for_selected_event: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinedEventDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // database
        auth = Firebase.auth
        firestore = Firebase.firestore


        //recyclerview
        chatArrayList=ArrayList<Chat>()

        //for favorite
        postArrayList=ArrayList<Post>()


        //recyclerview link
        binding.recMainDetail.layoutManager= LinearLayoutManager(this)
        mainAdapter= DetailMainAdapter(chatArrayList)
        binding.recMainDetail.adapter=mainAdapter


        //get data from joinedevent
        binding.textDetailCountry.text = intent.extras?.get("country").toString()
        binding.textDetailCity.text = intent.extras?.get("city").toString()
        binding.textDetailCategory.text = intent.extras?.get("category").toString()
        binding.textDetailHeader.text = intent.extras?.get("header").toString()
        binding.textDetailNick.text = intent.extras?.get("nick").toString()
        binding.textDetailDetail.text = intent.extras?.get("detail").toString()
        binding.dateText.text=intent.extras?.get("eventDate").toString()


        //get doc id for favorite and join
        get_doc_id_for_selected_event = intent.extras?.get("documentId").toString()

        getDataForChat()

        //scroll
        textview_detail = findViewById(R.id.text_detail_detail)
        textview_detail.text = intent.extras?.get("detail").toString()
        textview_detail.movementMethod = ScrollingMovementMethod()

        textView_header = findViewById(R.id.text_detail_header)
        textView_header.text = intent.extras?.get("header").toString()
        textView_header.movementMethod = ScrollingMovementMethod()

        fav_counter()
        join_counter()
    }

    //get data from firebase
    private fun getDataForChat()
    {
        firestore.collection("Messages").whereEqualTo("documentId",get_doc_id_for_selected_event).orderBy("dateForSorting",
            Query.Direction.ASCENDING).addSnapshotListener { value, error ->
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

                        chatArrayList.clear()
                        for (document in documents)
                        {
                            val message = document.get("message") as String
                            val nick = document.get("nick") as String
                            val documentId = document.get("documentId") as String
                            val userId = document.get("userId") as String
                            val date = document.get("date") as String
                            val chat= Chat(message, nick, documentId, userId, date)
                            chatArrayList.add(chat)
                        }
                        mainAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }


    //delete from joined when click
    fun remove_from_join(view: View)
    {
        if (auth.currentUser != null) {

            var now_user=auth.currentUser!!.uid.toString()

            //delete user id from database (joined)
            firestore.collection("Created").document( get_doc_id_for_selected_event).update("joinedUser", FieldValue.arrayRemove(now_user))
            this.recreate()

            //go to joined event page
            val intent= Intent(this, JoinedEvent::class.java)
            startActivity(intent)
            finishAffinity()
            Toast.makeText(applicationContext, "Katıldıklarımdan Çıkarıldı", Toast.LENGTH_SHORT).show()

        }
    }


    //add to favorite when click
    fun add_to_fav(view: View)
    {
        if (auth.currentUser != null) {

            var now_user=auth.currentUser!!.uid.toString()


            //add to database user's id
            firestore.collection("Created").document( get_doc_id_for_selected_event).update("favoriteUser", FieldValue.arrayUnion(now_user))
            this.recreate()
            Toast.makeText(applicationContext, "Favorilerime Eklendi", Toast.LENGTH_SHORT).show()

        }
    }


    //click_for_message
    @RequiresApi(Build.VERSION_CODES.O)
    fun click_for_message(view: View)
    {
        val dialog = Dialog(this)
        dialog.setTitle("Mesaj Girin")
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.chat_view)
        val chat = dialog.findViewById(R.id.chat_text) as TextView
        val send = dialog.findViewById(R.id.send) as Button
        val cancel = dialog.findViewById(R.id.cancel) as Button

        send.setOnClickListener {

            finish()
            startActivity(getIntent())

            //control with regex white spaces and empty area
            var re = Regex("^\\s*$")
            if(chat.text.toString().matches(regex = re))
            {
                Toast.makeText(applicationContext, "Boş Mesaj Gönderemezsiniz !", Toast.LENGTH_SHORT).show()
            }
            else if(auth.currentUser!=null)
            {

                //add data to firebase
                val chatMap = hashMapOf<String,Any>()
                chatMap.put("message",chat.text.toString().trimStart().trimEnd().lowercase().capitalize())

                //use user mail as nick
                val string = auth.currentUser!!.email.toString()
                val domain: String? = string.substringBeforeLast("@")
                if (domain != null) {
                    chatMap.put("nick",domain)
                }

                chatMap.put("documentId",get_doc_id_for_selected_event)

                chatMap.put("userId", auth.currentUser!!.uid.toString())

                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val formatted = current.format(formatter)
                chatMap.put("date",formatted)

                //for sort message as date
                chatMap.put("dateForSorting",com.google.firebase.Timestamp.now())


                firestore.collection("Messages").add(chatMap)
                Toast.makeText(applicationContext, "Mesaj Gönderildi", Toast.LENGTH_SHORT).show()
            }
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }
    fun complaint_button(view: View)
    {
        val dialog = Dialog(this)
        dialog.setTitle("Şikayet Et")
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.complaint_view)
        val text_area = dialog.findViewById(R.id.complaint_text) as TextView
        val send = dialog.findViewById(R.id.send_complaint) as Button
        val cancel = dialog.findViewById(R.id.cancel_complaint) as Button

        send.setOnClickListener {

            finish()
            startActivity(getIntent())

            //control with regex white spaces and empty area
            var re = Regex("^\\s*$")
            if(text_area.text.toString().matches(regex = re))
            {
                Toast.makeText(applicationContext, "Boş Şikayet Gönderemezsiniz !", Toast.LENGTH_SHORT).show()
            }
            else
            {
                //add data to firebase
                val complaintMap = hashMapOf<String,Any>()
                complaintMap.put("complaint",text_area.text.toString().trimStart().trimEnd())

                complaintMap.put("eventDocumentId",get_doc_id_for_selected_event)

                complaintMap.put("userId", auth.currentUser!!.uid.toString())

                complaintMap.put("mail", auth.currentUser!!.email.toString())

                complaintMap.put("date",com.google.firebase.Timestamp.now())

                firestore.collection("Complaint").add(complaintMap)
                Toast.makeText(applicationContext, "Şikayet Gönderildi", Toast.LENGTH_SHORT).show()
            }
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    //show favorite count
    private fun fav_counter()
    {
        firestore.collection("Created").whereEqualTo("documentId",get_doc_id_for_selected_event).whereArrayContains("favoriteUser","1").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        val obj = doc.getData()["favoriteUser"]
                        if(obj != null) {
                            var arraySize = (obj as List<String>).size
                            Log.d("TAG", "$arraySize")
                            if (arraySize>=1)
                            { arraySize=arraySize-1}
                            get_size_fav=arraySize.toString()
                            binding.textFav.setText(get_size_fav).toString()
                            firestore.collection("Created").document(get_doc_id_for_selected_event).update("favCount",get_size_fav)

                        }
                    }
                }
            }
    }

    //show join count
    private fun join_counter()
    {
        firestore.collection("Created").whereEqualTo("documentId",get_doc_id_for_selected_event).whereArrayContains("joinedUser","1").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        val obj = doc.getData()["joinedUser"]
                        if(obj != null) {
                            var arraySize = (obj as List<String>).size
                            Log.d("TAG", "$arraySize")
                            if (arraySize>=1)
                            { arraySize=arraySize-1}
                            get_size_join=arraySize.toString()
                            binding.textJoin.setText(get_size_join).toString()
                            firestore.collection("Created").document(get_doc_id_for_selected_event).update("joinCount",get_size_join)

                        }
                    }
                }
            }
    }
}