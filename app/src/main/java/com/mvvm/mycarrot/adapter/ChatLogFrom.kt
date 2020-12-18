package com.mvvm.mycarrot.adapter

import com.bumptech.glide.Glide
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.model.MessageDTO
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_chat_from.view.*

class ChatLogFrom(val dto: MessageDTO) : Item<GroupieViewHolder>() {
    override fun getLayout() = R.layout.item_chat_from

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.chat_from_tv_message.text = dto.message

        Glide.with(viewHolder.itemView.chat_from_iv_profile.context)
            .load(dto.yourProfileUrl)
            .placeholder(R.drawable.ic_user)
            .circleCrop()
            .thumbnail(0.1f)
            .into(viewHolder.itemView.chat_from_iv_profile)
    }
}