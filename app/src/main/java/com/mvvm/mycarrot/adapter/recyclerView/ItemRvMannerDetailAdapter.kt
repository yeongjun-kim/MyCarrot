package com.mvvm.mycarrot.adapter.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ItemRvMannerDetailBinding
import com.mvvm.mycarrot.data.model.ReviewObject
import kotlinx.android.synthetic.main.item_rv_manner_detail.view.*

class ItemRvMannerDetailAdapter :
    RecyclerView.Adapter<ItemRvMannerDetailAdapter.CustomViewHolder>() {


    var itemList = listOf<ReviewObject>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_rv_manner_detail,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = itemList[position]
        val customViewHolder = holder as CustomViewHolder
        customViewHolder.bind(item)
    }

    fun setList(inputList: List<ReviewObject>) {
        itemList = inputList
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(val binding: ItemRvMannerDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(inputReview: ReviewObject) {
            binding.apply {
                inputReview.str = "${adapterPosition + 1}. ${inputReview.str}"
                review = inputReview

                if (adapterPosition == itemList.lastIndex) itemView.itemRvMannerDetail_ll.visibility =
                    View.INVISIBLE
            }
        }
    }
}