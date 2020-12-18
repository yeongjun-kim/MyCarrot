package com.mvvm.mycarrot.adapter

import com.bumptech.glide.Glide
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.model.LatestMessageDTO
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_rv_latestmessage.view.*

class LatestGroupie(val dto: LatestMessageDTO, val listener: ClickListener) :
    Item<GroupieViewHolder>() {

    interface ClickListener {
        fun onClick(item: LatestMessageDTO)
    }

    override fun getLayout() = R.layout.item_rv_latestmessage

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.setOnClickListener {
            listener.onClick(dto)
        }

        viewHolder.itemView.item_latestmessage_tv_nickname.text = dto.opponentNickname
        viewHolder.itemView.item_latestmessage_tv_message.text = dto.message
        viewHolder.itemView.item_latestmessage_tv_dong.text = dto.opponentLocation.split(" ")[1]

        Glide.with(viewHolder.itemView.item_latestmessage_iv_profile.context)
            .load(dto.opponentProfileUrl)
            .placeholder(R.drawable.ic_user)
            .circleCrop()
            .thumbnail(0.1f)
            .into(viewHolder.itemView.item_latestmessage_iv_profile)

        Glide.with(viewHolder.itemView.item_latestmessage_iv_itemurl.context)
            .load(dto.itemImageUrl)
            .placeholder(R.drawable.ic_user)
            .circleCrop()
            .thumbnail(0.1f)
            .into(viewHolder.itemView.item_latestmessage_iv_itemurl)
    }
}