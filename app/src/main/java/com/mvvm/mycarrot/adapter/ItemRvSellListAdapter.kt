package com.mvvm.mycarrot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ItemRvSelllistBinding
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.utils.onThrottleClick

class ItemRvSellListAdapter : RecyclerView.Adapter<ItemRvSellListAdapter.CustomViewHolder>() {

    interface ClickListener {
        fun onClClick(position: Int)
        fun onReservationClick(position: Int)
        fun onSoldoutClick(position: Int)
    }

    var itemList = listOf<ItemObject>()
    var listener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_rv_selllist,
                parent, false
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

    inner class CustomViewHolder(val binding: ItemRvSelllistBinding, val listener: ClickListener?) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(inputItem: ItemObject) {
            binding.apply {
                item = inputItem

                itemRvSelllistCl1.onThrottleClick { listener?.onClClick(adapterPosition) }
                itemRvSelllistTvReservation.onThrottleClick {
                    listener?.onReservationClick(
                        adapterPosition
                    )
                }
                itemRvSelllistTvSoldout.onThrottleClick { listener?.onSoldoutClick(adapterPosition) }

                if (inputItem.status == "reservation") {
                    itemRvSelllistTvReservation.text = "판매중으로 변경"
                } else if (inputItem.status == "sell") {
                    itemRvSelllistTvReservation.text = "예약중으로 변경"
                }
            }
        }
    }
}