package com.mvvm.mycarrot.adapter.groupie

import android.view.View
import com.bumptech.glide.Glide
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.data.model.MessageDTO
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_chat_from.view.*
import java.text.SimpleDateFormat

class ChatLogFromGroupie(val dto: MessageDTO, val dateChange: Boolean) : Item<GroupieViewHolder>() {
    override fun getLayout() = R.layout.item_chat_from

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if (dateChange) {
            viewHolder.itemView.chatFrom_cl1.visibility = View.VISIBLE
            viewHolder.itemView.chatFrom_tv_date.text =
                SimpleDateFormat("yyyy년 MM월 dd일").format(dto.timestamp)
        } else viewHolder.itemView.chatFrom_cl1.visibility = View.GONE

        viewHolder.itemView.chat_from_tv_message.text = dto.message
        viewHolder.itemView.chat_from_tv_time.text =
            SimpleDateFormat("K:mm a").format(dto.timestamp)

        Glide.with(viewHolder.itemView.chat_from_iv_profile.context)
            .load(dto.opponentProfileUrl)
            .placeholder(R.drawable.ic_user)
            .circleCrop()
            .thumbnail(0.1f)
            .into(viewHolder.itemView.chat_from_iv_profile)
    }
}