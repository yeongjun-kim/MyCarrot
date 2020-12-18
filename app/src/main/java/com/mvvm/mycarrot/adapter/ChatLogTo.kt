package com.mvvm.mycarrot.adapter

import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.model.MessageDTO
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_chat_to.view.*


class ChatLogTo(val dto: MessageDTO) : Item<GroupieViewHolder>() {
    override fun getLayout() = R.layout.item_chat_to

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.chat_to_tv_message.text = dto.message
    }
}