package com.mvvm.mycarrot.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ItemRvItemBinding
import com.mvvm.mycarrot.databinding.ItemRvUserBinding
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.utils.onThrottleClick

class UserRvAdapter :RecyclerView.Adapter<UserRvAdapter.CustomViewHolder>(){

    interface ClickListener{
        fun onClick(position:Int)
    }

    var itemList = listOf<UserObject>()
    var listener:ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.item_rv_user,
                parent,false), listener
        )
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val user = itemList[position]
        val customViewHolder = holder as CustomViewHolder
        customViewHolder.bind(user)
    }

    fun setList(inputList:List<UserObject>){
        itemList = inputList
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(val binding: ItemRvUserBinding, val listener: ClickListener?):RecyclerView.ViewHolder(binding.root){
        init {
            itemView.onThrottleClick { listener?.onClick(adapterPosition) }
        }
        fun bind(inputUser:UserObject){
            binding.apply {
                user = inputUser
            }
        }
    }
}