package com.furkandakak.together.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.furkandakak.together.databinding.RecyclerRowChatBinding
import com.furkandakak.together.model.Chat
import java.util.ArrayList


class DetailMainAdapter(private val chatlist: ArrayList<Chat>): RecyclerView.Adapter<DetailMainAdapter.ChatHolder>()

{

    class ChatHolder(val binding: RecyclerRowChatBinding): RecyclerView.ViewHolder(binding.root)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val binding =
            RecyclerRowChatBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ChatHolder((binding))
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {

        holder.binding.dateRow.text=chatlist.get(position).date
        holder.binding.nickRow.text=chatlist.get(position).nick
        holder.binding.messageRow.text=chatlist.get(position).message


    }


    override fun getItemCount(): Int {
        return chatlist.size
    }



}

