package com.mvvm.mycarrot.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ItemRvOwneritemBinding
import com.mvvm.mycarrot.data.model.ItemObject
import com.mvvm.mycarrot.utils.onThrottleClick

class OwnerItemRvAdapter : RecyclerView.Adapter<OwnerItemRvAdapter.CustomViewHolder>() {

    interface ClickListener {
        fun onClick(position: Int)
    }

    var itemList = listOf<ItemObject>()
    var listener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_rv_owneritem,
                parent, false
            ), listener
        )
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = itemList[position]
        val customViewHolder = holder 
        customViewHolder.bind(item)
    }

    fun setList(inputList: List<ItemObject>, maxNum: Int? = null) {
        itemList = if (maxNum == null || inputList.size <= maxNum) inputList
        else inputList.subList(0, maxNum)

        notifyDataSetChanged()
    }


    inner class CustomViewHolder(
        val binding: ItemRvOwneritemBinding,
        val listener: ClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.onThrottleClick { listener?.onClick(adapterPosition) }
        }

        fun bind(inputItem: ItemObject) {
            binding.apply {
                item = inputItem
            }
        }
    }
}