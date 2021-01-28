package com.mvvm.mycarrot.adapter.recyclerView


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ItemRvCollectBinding
import com.mvvm.mycarrot.data.model.ItemObject
import com.mvvm.mycarrot.utils.onThrottleClick

class CollectRvAdapter : RecyclerView.Adapter<CollectRvAdapter.CustomViewHolder>() {

    interface ClickListener {
        fun onClick(position: Int)
    }

    var itemList = listOf<ItemObject>()
    var listener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_rv_collect,
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

    fun setList(inputList: List<ItemObject>) {
        itemList = inputList
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(val binding: ItemRvCollectBinding, val listener: ClickListener?) :
        RecyclerView.ViewHolder(binding.root) {
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