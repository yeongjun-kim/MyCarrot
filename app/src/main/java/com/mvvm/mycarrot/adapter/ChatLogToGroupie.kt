package com.mvvm.mycarrot.adapter

import android.opengl.Visibility
import android.view.View
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.model.MessageDTO
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_chat_to.view.*
import java.text.SimpleDateFormat


class ChatLogToGroupie(val dto: MessageDTO, val dateChange:Boolean) : Item<GroupieViewHolder>() {
    override fun getLayout() = R.layout.item_chat_to

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if(dateChange){
            viewHolder.itemView.chatTo_cl1.visibility = View.VISIBLE
            viewHolder.itemView.chatTo_tv_date.text = SimpleDateFormat("yyyy년 MM월 dd일").format(dto.timestamp)
        } else viewHolder.itemView.chatTo_cl1.visibility = View.GONE

        viewHolder.itemView.chat_to_tv_message.text = dto.message
        viewHolder.itemView.chat_to_tv_time.text =  SimpleDateFormat("K:mm a").format(dto.timestamp)
    }
}