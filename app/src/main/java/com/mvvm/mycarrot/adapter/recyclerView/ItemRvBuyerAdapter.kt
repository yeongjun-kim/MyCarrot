package com.mvvm.mycarrot.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ItemRvBuyerBinding
import com.mvvm.mycarrot.data.model.LatestMessageDTO
import com.mvvm.mycarrot.utils.onThrottleClick

class ItemRvBuyerAdapter : RecyclerView.Adapter<ItemRvBuyerAdapter.CustomViewHolder>() {

    interface ClickListener {
        fun onClick(position: Int)
    }

    var messageList = listOf<LatestMessageDTO>()
    var listener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_rv_buyer,
                parent, false
            ), listener
        )
    }

    override fun getItemCount() = messageList.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = messageList[position]
        val customViewHolder = holder as CustomViewHolder
        customViewHolder.bind(item)
    }

    fun setList(inputList: List<LatestMessageDTO>) {
        messageList = inputList
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(val binding: ItemRvBuyerBinding, val listener: ClickListener?) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.onThrottleClick { listener?.onClick(adapterPosition) }
        }

        fun bind(inputMessage: LatestMessageDTO) {
            binding.apply {
                message = inputMessage
            }
        }
    }
}