package com.mvvm.mycarrot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ItemRvOwneritemBinding
import com.mvvm.mycarrot.databinding.ItemRvOwneritemHorizontalBinding
import com.mvvm.mycarrot.model.ItemObject
import kotlinx.android.synthetic.main.activity_item.view.*

class OwnerItemRvAdapterHorizontal: RecyclerView.Adapter<OwnerItemRvAdapterHorizontal.CustomViewHolder>() {

    interface ClickListener{
        fun onClick(position:Int)
    }

    var itemList = listOf<ItemObject>()
    var listener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_rv_owneritem_horizontal,
                parent, false), listener
        )
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = itemList[position]
        val customViewHolder = holder as CustomViewHolder
        customViewHolder.bind(item)
    }

    fun setList(inputList:List<ItemObject>,maxNum:Int? = null){
        itemList = if(maxNum ==null || inputList.size <= maxNum) inputList
        else inputList.subList(0,maxNum)

        notifyDataSetChanged()
    }


    inner class CustomViewHolder(val binding:ItemRvOwneritemHorizontalBinding, val listener:ClickListener?) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener { listener?.onClick(adapterPosition) }
        }

        fun bind(inputItem:ItemObject){
            binding.apply {
                item = inputItem
            }
        }
    }
}