package com.furkandakak.together.pages

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.furkandakak.together.R
import com.furkandakak.together.databinding.ActivityCreateEventBinding
import com.furkandakak.together.spinners.SpinnerCategoryArray
import com.furkandakak.together.spinners.SpinnerCityArray
import com.furkandakak.together.spinners.SpinnerCountryArray
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class CreateEvent : AppCompatActivity(){
    // spinner country
    lateinit var option:Spinner
    lateinit var show_country: TextView
    // spinner country

    // spinner city
    lateinit var option2:Spinner
    lateinit var show_city: TextView
    // spinner city

    // spinner category
    lateinit var option3:Spinner
    lateinit var show_category: TextView
    // spinner category

    //google database
    private lateinit var auth:FirebaseAuth
    private lateinit var firestore:FirebaseFirestore
    //google database

    //date for sorting
    lateinit var eventDateSorting:Date
    //date for sorting


    private lateinit var binding:ActivityCreateEventBinding
    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCreateEventBinding.inflate(layoutInflater)
        val view= binding.root
        setContentView(view)

        //google database
        auth=Firebase.auth
        firestore=Firebase.firestore
        //google database

        //date for event
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        binding.dateView.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, myear, mmonth, mdayOfMonth ->
                var plus_month=mmonth+1
                binding.dateView.setText(""+ mdayOfMonth +"/"+ plus_month +"/"+ myear)

                cal.set(myear,mmonth,mdayOfMonth)
                eventDateSorting=cal.time
            }, year, month, day)

            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
        //date for event

        //spinner
        //spinner country
        option=findViewById(R.id.spinner_country_create) as Spinner
        show_country=findViewById(R.id.textView_country) as TextView
        //spinner country


        //spinner city
        option2=findViewById(R.id.spinner_city_create) as Spinner
        show_city=findViewById(R.id.textView_city) as TextView
        //spinner city

        //spinner category
        option3=findViewById(R.id.spinner_category_create) as Spinner
        show_category=findViewById(R.id.textView_category) as TextView
        //spinner category

        //get array for using in spinner(Country)
        val child_country_class = SpinnerCountryArray()
        val taking_array_country =child_country_class.country_array

        //get array for using in spinner(City)
        val child_city_class = SpinnerCityArray()
        val taking_array_city =child_city_class.city_array

        //get array for using in spinner(Category)
        val child_category_class = SpinnerCategoryArray()
        val taking_array_category =child_category_class.category_array

        //Country spinner adapter beginnig
        option.adapter=ArrayAdapter<String>(this,
            R.layout.spinner_item_create,taking_array_country)

        val spinner = Spinner(this)


        option.onItemSelectedListener=object :AdapterView.OnItemSelectedListener
        {

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long)
            {
                show_country.setText(taking_array_country.get(p2))


            }

            override fun onNothingSelected(p0: AdapterView<*>?)
            {

            }

        }
        //Country spinner adapter end

        //city spinner adapter beginning
        option2.adapter=ArrayAdapter<String>(this,
            R.layout.spinner_item_create,taking_array_city)

        option2.onItemSelectedListener=object :AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long)
            {

                show_city.setText(taking_array_city.get(p2))
            }

            override fun onNothingSelected(p0: AdapterView<*>?)
            {

            }

        }
        //City spinner end

        //category spinner adapter beginning
        option3.adapter=ArrayAdapter<String>(this,
            R.layout.spinner_item_create,taking_array_category)

        option3.onItemSelectedListener=object :AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long)
            {
                show_category.setText(taking_array_category.get(p2))
            }

            override fun onNothingSelected(p0: AdapterView<*>?)
            {

            }

        }
        //category spinner end

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun button_create_event(view: View)
    {val currentTime: Date = Calendar.getInstance().getTime()

        //regex for disable white space at beginnig or just send white space
        var re = Regex("^\\s*$")

        //control if any item is empty
        if(option.selectedItem=="Ülke Seçiniz:" || option2.selectedItem=="Şehir Seçiniz:" || option3.selectedItem=="Kategori Seçiniz:" ||binding.headerText.text.toString().matches(regex = re)
            ||binding.detailCreateText.text.toString().matches(regex = re))
        {
            Toast.makeText(applicationContext, "Bazı Alanları Boş Bıraktınız !", Toast.LENGTH_SHORT).show()

        }

        else if (binding.dateView.text=="Tarih Eklemek İçin Tıklayın:")
        {
            Toast.makeText(applicationContext, "Tarih Seçmediniz !", Toast.LENGTH_SHORT).show()
        }


        //add to firebase
        else if(auth.currentUser!=null)
        {

            //hashmap to add datas to firebase
            val postMap = hashMapOf<String,Any>()
            postMap.put("country",binding.spinnerCountryCreate.selectedItem.toString())
            postMap.put("city",binding.spinnerCityCreate.selectedItem.toString())
            postMap.put("category",binding.spinnerCategoryCreate.selectedItem.toString())
            postMap.put("header",binding.headerText.text.toString().trimStart().trimEnd().lowercase().capitalize())
            postMap.put("detail",binding.detailCreateText.text.toString().trimStart().trimEnd().capitalize())
            postMap.put("date",com.google.firebase.Timestamp.now())
            postMap.put("userId", auth.currentUser!!.uid.toString())

            //random ID for each event(eventID)
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val key = database.getReference("Created").push().getKey()
            postMap.put("eventId",key.toString())

            //drop at user's mail ".com" for using as a nick
            val string = auth.currentUser!!.email.toString()
            val domain: String? = string.substringBeforeLast("@")
            if (domain != null) {
                postMap.put("nick",domain)
            }


            //add header to tabase as word
            var string_to_word=binding.headerText.text.toString().lowercase()
            val new_list_for_header = string_to_word!!.split(" ")
            postMap.put("headerArray",new_list_for_header)



            //create array for store users who favorite event
            var favorite_array_creater=auth.currentUser!!.uid.toString()
           // val new_list_for_favorite_user = favorite_array_creater!!.split(" ")
            //postMap.put("favoriteUser",new_list_for_favorite_user)
            val list: MutableList<String> = ArrayList()
            list.add(favorite_array_creater)
            list.add("1")
            postMap.put("favoriteUser",list)


            //create array for store users who join event
            var joined_array_creater=auth.currentUser!!.uid.toString()
            //val new_list_for_joined_user = joined_array_creater!!.split(" ")
            //postMap.put("joinedUser",new_list_for_joined_user)

            val list2: MutableList<String> = ArrayList()
            list2.add(joined_array_creater)
            list2.add("1")
            postMap.put("joinedUser",list2)


            //get Doc ID and add to document field.
            val ref: DocumentReference = firestore.collection("Created").document()
            val doc_id: String =ref.getId()
            postMap.put("documentId",doc_id)

            //date of event
            postMap.put("eventDate",binding.dateView.text.toString())
            postMap.put("sortEventDate",eventDateSorting)

            postMap.put("favCount","0".toString())
            postMap.put("joinCount","0".toString())

            //send first message all event as warning
            add_first_message(doc_id)


           ref.set(postMap).addOnSuccessListener {
                finish()

                Toast.makeText(applicationContext, "Etkinlik Oluşturuldu Ve Katıldıklarım'a Eklendi", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {

                Toast.makeText(this@CreateEvent,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }
    }

    fun button_create_to_home(view: View)
    {
        val intent= Intent(this, MainPage::class.java)
        startActivity(intent)
    }

    //send first message all event as warning
    @RequiresApi(Build.VERSION_CODES.O)
    fun add_first_message(docID:String)
    {
        //add to database
        val chatMap = hashMapOf<String,Any>()
        chatMap.put("message","Lütfen Sohbet Ederken Saygılı olun ! İYİ SOHBETLER")
        chatMap.put("nick","EVENDER BOT")
        chatMap.put("documentId",docID)

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = current.format(formatter)
        chatMap.put("date",formatted)
        chatMap.put("userId", auth.currentUser!!.uid.toString())

        //sort as date
        chatMap.put("dateForSorting",com.google.firebase.Timestamp.now())
        firestore.collection("Messages").add(chatMap)
    }




}


