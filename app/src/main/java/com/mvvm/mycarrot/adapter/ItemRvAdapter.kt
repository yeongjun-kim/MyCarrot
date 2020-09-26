package com.mvvm.mycarrot.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ItemRvItemBinding
import com.mvvm.mycarrot.model.ItemObject

class ItemRvAdapter :RecyclerView.Adapter<ItemRvAdapter.CustomViewHolder>(){

    private var itemList = listOf<ItemObject>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.item_rv_item,
                parent,false)
        )
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = itemList[position]
        val customViewHolder = holder as CustomViewHolder
        customViewHolder.bind(item)
    }

    fun setList(inputList:List<ItemObject>){
        itemList = inputList
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(val binding: ItemRvItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(inputItem:ItemObject){
            binding.apply {
                itemRvItemTvTitle.isSelected = true // TextView 흐르는 효과
                item = inputItem
            }
        }
    }
}