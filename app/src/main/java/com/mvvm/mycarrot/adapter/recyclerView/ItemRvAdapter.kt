package com.mvvm.mycarrot.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ItemRvItemBinding
import com.mvvm.mycarrot.data.model.ItemObject
import com.mvvm.mycarrot.utils.onThrottleClick

class ItemRvAdapter : RecyclerView.Adapter<ItemRvAdapter.CustomViewHolder>() {

    interface ClickListener {
        fun onClick(position: Int)
    }

    var itemList = listOf<ItemObject>()
    var listener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_rv_item,
                parent,
                false
            ), listener
        )
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = itemList[position]
        val customViewHolder = holder as CustomViewHolder
        customViewHolder.bind(item)
    }

    fun setList(inputList: List<ItemObject>) {
        itemList = inputList
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(val binding: ItemRvItemBinding, val listener: ClickListener?) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.onThrottleClick { listener?.onClick(adapterPosition) }
        }

        fun bind(inputItem: ItemObject) {
            binding.apply {
                itemRvItemTvTitle.isSelected = true // TextView 흐르는 효과
                item = inputItem
            }
        }
    }
}