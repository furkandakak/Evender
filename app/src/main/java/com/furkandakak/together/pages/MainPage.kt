package com.furkandakak.together.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.furkandakak.together.R
import com.furkandakak.together.adapter.MainPageRecyclerAdapter
import com.furkandakak.together.databinding.ActivityMainPageBinding
import com.furkandakak.together.model.Post
import com.furkandakak.together.spinners.SpinnerCategoryArray
import com.furkandakak.together.spinners.SpinnerCityArray
import com.furkandakak.together.spinners.SpinnerCountryArray
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainPage : AppCompatActivity() {

    //database
    private lateinit var auth:FirebaseAuth
    private lateinit var db:FirebaseFirestore
    private lateinit var postArrayList:ArrayList<Post>
    private lateinit var mainAdapter:MainPageRecyclerAdapter

    lateinit var get_size: String

    // spinner country
    lateinit var option: Spinner
    lateinit var show_country: TextView
    // spinner country

    // spinner city
    lateinit var option2: Spinner
    lateinit var show_city: TextView
    // spinner city

    // spinner category
    lateinit var option3: Spinner
    lateinit var show_category: TextView
    // spinner category

    lateinit var binding:ActivityMainPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      binding=ActivityMainPageBinding.inflate(layoutInflater)
        val view= binding.root
        setContentView(view)

        //database
        auth=Firebase.auth
        db=Firebase.firestore
        postArrayList=ArrayList<Post>()

        getData()

        //recyclerview link
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        mainAdapter= MainPageRecyclerAdapter(postArrayList)
        binding.recyclerView.adapter=mainAdapter

        //spinner country
        option=findViewById(R.id.spinner_country) as Spinner
         show_country=findViewById(R.id.textView_country_main) as TextView
        //spinner country

        //spinner city
        option2=findViewById(R.id.spinner_city) as Spinner
         show_city=findViewById(R.id.textView_city_main) as TextView
        //spinner city

        //spinner category
        option3=findViewById(R.id.spinner_category) as Spinner
         show_category=findViewById(R.id.textView_category_main) as TextView
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
        option.adapter= ArrayAdapter<String>(this,
            R.layout.spinner_item,taking_array_country)

        option.onItemSelectedListener=object : AdapterView.OnItemSelectedListener
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
        option2.adapter= ArrayAdapter<String>(this,
            R.layout.spinner_item,taking_array_city)

        option2.onItemSelectedListener=object : AdapterView.OnItemSelectedListener
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
        option3.adapter= ArrayAdapter<String>(this,
            R.layout.spinner_item,taking_array_category)

        option3.onItemSelectedListener=object : AdapterView.OnItemSelectedListener
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
    //get data from firebase
    private fun getData()
    {
        db.collection("Created").orderBy("sortEventDate",Query.Direction.ASCENDING).addSnapshotListener { value, error ->
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
                            val favCount=document.get("favCount") as? String
                            val joinCount=document.get("joinCount") as? String

                            val post= Post(category,city,country,detail,header,nick,documentId,eventDate.toString(),
                                favCount.toString(),joinCount.toString()
                            )
                            postArrayList.add(post)

                        }
                        mainAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    // when click filter get data
    private fun getData_for_filter()
    {
        var country2=binding.spinnerCountry.selectedItem.toString()
        var city2=binding.spinnerCity.selectedItem.toString()
        var category2=binding.spinnerCategory.selectedItem.toString()

        db.collection("Created").whereEqualTo("country",country2).whereEqualTo("city",city2).whereEqualTo("category",category2).orderBy("sortEventDate",Query.Direction.ASCENDING).addSnapshotListener { value, error ->
            if (error != null) {
                //Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
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
                            val eventDate=document.get("eventDate") as String
                            val favCount=document.get("favCount") as String
                            val joinCount=document.get("joinCount") as String

                            val post= Post(category,city,country,detail,header,nick,documentId,eventDate,favCount,joinCount)
                            postArrayList.add(post)
                        }
                        mainAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

    }

    // butons
    fun home_button_clicked(view: View)
    {
        finish()
        startActivity(getIntent())
    }

    fun create_button_clicked(view: View)
    {
        val intent= Intent(this, CreateEvent::class.java)
        startActivity(intent)
    }

    fun joined_button_clicked(view: View)
    {
        val intent= Intent(this, JoinedEvent::class.java)
        startActivity(intent)
    }

    fun favorite_button_clicked(view: View)
    {
        val intent= Intent(this, FavoriteEvent::class.java)
        startActivity(intent)
    }

    fun account_button_clicked(view: View)
    {
        val intent= Intent(this, Account::class.java)
        startActivity(intent)
    }

    fun button_search_main(view: View)
    {
        //if spinner empty
        /*because of country selecting is disable, I hide this
        if(option.selectedItem=="Ülke Seçiniz:")
        {
            Toast.makeText(applicationContext, "Ülke Seçmediniz !", Toast.LENGTH_SHORT).show()
            // binding.textViewCountryMain.setText("Ülke Seçiniz:")
        }*/

        //get data just selected== category
        if(option2.selectedItem=="Şehir Seçiniz:" && option3.selectedItem!="Kategori Seçiniz:")
        {
            getData_for_filter_category()
            // binding.textViewCityMain.setText("Şehir Seçiniz:")
        }

        //get data just selected== city
        else if(option3.selectedItem=="Kategori Seçiniz:" && option2.selectedItem!="Şehir Seçiniz:")
        {
            getData_for_filter_city()
            // binding.textViewCategoryMain.setText("Kategori Seçiniz:")
        }
        //if nothing selected show warning
        else if(option2.selectedItem=="Şehir Seçiniz:" && option3.selectedItem=="Kategori Seçiniz:")
        {
            Toast.makeText(applicationContext, "Şehir veya Kategori Seçmediniz !", Toast.LENGTH_SHORT).show()
        }
        //get all all filters data
        else{getData_for_filter()}
    }

    fun clean_filter(view: View)
    {
        finish()
        startActivity(getIntent())
    }
    fun click_to_word(view: View)
    {
        getData_word()
    }

    private fun getData_word() {
        var entered_word = binding.edittextFindWord.text.toString().lowercase().trimStart().trimEnd()

        val new_list_for_header = entered_word!!.split(" ")

        ////regex for control white space or empty
        var re = Regex("^\\s*$")
        if (entered_word.matches(regex = re)) {
            Toast.makeText(applicationContext, "Boş Arama Yapamazsınız !", Toast.LENGTH_SHORT)
                .show()
        }

        else{

        db.collection("Created").orderBy("sortEventDate",Query.Direction.ASCENDING)
            .whereArrayContainsAny("headerArray", new_list_for_header)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    //Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
                } else {
                    if (value != null) {
                        if (!value.isEmpty) {
                            val documents = value.documents

                            postArrayList.clear()
                            for (document in documents) {
                                val category = document.get("category") as String
                                val city = document.get("city") as String
                                val country = document.get("country") as String
                                val detail = document.get("detail") as String
                                val header = document.get("header") as String
                                val nick = document.get("nick") as String
                                val documentId = document.get("documentId") as String
                                val eventDate=document.get("eventDate") as String
                                val favCount=document.get("favCount") as String
                                val joinCount=document.get("joinCount") as String

                                val post =
                                    Post(category, city, country, detail, header, nick, documentId,eventDate,favCount,joinCount)
                                postArrayList.add(post)
                            }
                            mainAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    //just category filter /show all data for selected category
    // when click filter get data
    private fun getData_for_filter_category()
    {
        var country2=binding.spinnerCountry.selectedItem.toString()
        var category2=binding.spinnerCategory.selectedItem.toString()

        db.collection("Created").whereEqualTo("country",country2).whereEqualTo("category",category2).orderBy("sortEventDate",Query.Direction.ASCENDING).addSnapshotListener { value, error ->
            if (error != null) {
                //Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
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
                            val eventDate=document.get("eventDate") as String
                            val favCount=document.get("favCount") as String
                            val joinCount=document.get("joinCount") as String

                            val post= Post(category,city,country,detail,header,nick,documentId,eventDate,favCount,joinCount)
                            postArrayList.add(post)
                        }
                        mainAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

    }

    //just city filter /show all data for selected city
    // when click filter get data
    private fun getData_for_filter_city()
    {
        var country2=binding.spinnerCountry.selectedItem.toString()
        var city2=binding.spinnerCity.selectedItem.toString()

        db.collection("Created").whereEqualTo("country",country2).whereEqualTo("city",city2).orderBy("sortEventDate",Query.Direction.ASCENDING).addSnapshotListener { value, error ->
            if (error != null) {
                //Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
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
                            val eventDate=document.get("eventDate") as String
                            val favCount=document.get("favCount") as String
                            val joinCount=document.get("joinCount") as String

                            val post= Post(category,city,country,detail,header,nick,documentId,eventDate,favCount,joinCount)
                            postArrayList.add(post)
                        }
                        mainAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

    }

}

