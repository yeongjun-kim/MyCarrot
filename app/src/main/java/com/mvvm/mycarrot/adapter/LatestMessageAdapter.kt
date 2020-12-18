package com.mvvm.mycarrot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ItemRvLatestmessageBinding
import com.mvvm.mycarrot.model.LatestMessageDTO
import com.mvvm.mycarrot.utils.onThrottleClick

class LatestMessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ClickListener {
        fun onClick(position: Int)
    }

    var latestMessageList = listOf<LatestMessageDTO>()
    var listener: ClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CustomViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_rv_latestmessage,
                parent, false
            ), listener
        )
    }

    override fun getItemCount() = latestMessageList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = latestMessageList[position]
        val customViewHolder = holder as CustomViewHolder
        customViewHolder.bind(item)
    }

    inner class CustomViewHolder(
        val binding: ItemRvLatestmessageBinding,
        val listener: ClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.onThrottleClick { listener?.onClick(adapterPosition) }
        }

        fun bind(inputItem: LatestMessageDTO) {
            binding.apply {
                item = inputItem
            }
        }

    }

}