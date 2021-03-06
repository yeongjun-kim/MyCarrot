package com.mvvm.mycarrot.adapter.groupie

import com.bumptech.glide.Glide
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.data.model.LatestMessageDTO
import com.mvvm.mycarrot.utils.onThrottleClick
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_rv_latestmessage.view.*
import java.text.SimpleDateFormat

class LatestGroupie(val dto: LatestMessageDTO, val listener: ClickListener?) :
    Item<GroupieViewHolder>() {

    interface ClickListener {
        fun onClick(item: LatestMessageDTO)
    }


    override fun getLayout() = R.layout.item_rv_latestmessage

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.onThrottleClick {
            listener?.onClick(dto)
        }

        viewHolder.itemView.itemLatestmessage_tv_nickname.text = dto.opponentNickname
        viewHolder.itemView.itemLatestmessage_tv_message.text = dto.message
        viewHolder.itemView.itemLatestmessage_tv_dong.text = dto.opponentLocation.split(" ")[1]
        viewHolder.itemView.itemLatestmessage_tv_time.text =
            SimpleDateFormat("MM월 dd일").format(dto.timestamp)


        Glide.with(viewHolder.itemView.itemLatestmessage_iv_profile.context)
            .load(dto.opponentProfileUrl)
            .placeholder(R.drawable.ic_user)
            .circleCrop()
            .thumbnail(0.1f)
            .into(viewHolder.itemView.itemLatestmessage_iv_profile)

        Glide.with(viewHolder.itemView.itemLatestmessage_iv_itemurl.context)
            .load(dto.itemImageUrl)
            .placeholder(R.drawable.ic_user)
            .circleCrop()
            .thumbnail(0.1f)
            .into(viewHolder.itemView.itemLatestmessage_iv_itemurl)
    }
}

