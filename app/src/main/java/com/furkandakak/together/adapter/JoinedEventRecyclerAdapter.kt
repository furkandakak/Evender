package com.furkandakak.together.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.furkandakak.together.pages.JoinedEventDetail
import com.furkandakak.together.databinding.RecyclerRowJoinedBinding
import com.furkandakak.together.model.Post
import java.util.ArrayList


class JoinedEventRecyclerAdapter(private val postlist: ArrayList<Post>): RecyclerView.Adapter<JoinedEventRecyclerAdapter.PostHolder>()

{

    class PostHolder(val binding: RecyclerRowJoinedBinding): RecyclerView.ViewHolder(binding.root)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding =
            RecyclerRowJoinedBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder((binding))
    }

    //data to recycler
    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.nickRow.text=postlist.get(position).header
        holder.binding.reccNick.text=postlist.get(position).nick
        holder.binding.recycCountry.text=postlist.get(position).country
        holder.binding.recycCity.text=postlist.get(position).city
        holder.binding.recycCategory.text=postlist.get(position).category
        holder.binding.favTextMain.text=postlist.get(position).favCount
        holder.binding.joinTextMain.text=postlist.get(position).joinCount
        holder.binding.eventDate.text=postlist.get(position).eventDate


        //data to detail page
        val take_detail_from_firebase=postlist.get(position).detail
        val doc_id_from_firebase=postlist.get(position).documentId


        //click each event to go detail page
        holder.itemView.setOnClickListener()
        {
            //page adress to go
            val intent= Intent(holder.itemView.context, JoinedEventDetail::class.java)

            //which items will be copied new page
            intent.putExtra("country",postlist.get(position).country)
            intent.putExtra("city",postlist.get(position).city)
            intent.putExtra("category",postlist.get(position).category)
            intent.putExtra("header",postlist.get(position).header)
            intent.putExtra("nick",postlist.get(position).nick)
            intent.putExtra("detail",take_detail_from_firebase)
            intent.putExtra("documentId",doc_id_from_firebase)
            intent.putExtra("eventDate",postlist.get(position).eventDate)

            //start new page
            it.context.startActivity(intent)
        }
        //click each event to go detail page

    }


    override fun getItemCount(): Int {
        return postlist.size
    }



}
