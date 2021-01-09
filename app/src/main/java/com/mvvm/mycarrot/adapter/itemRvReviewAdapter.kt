package com.mvvm.mycarrot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import kotlinx.android.synthetic.main.item_rv_review.view.*

class ItemRvReviewAdapter : RecyclerView.Adapter<ItemRvReviewAdapter.CustomViewHolder>() {

    var reviewList = listOf<String>()

    override fun getItemCount() = reviewList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_review, parent,false)
        return CustomViewHolder(view)
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.itemView.item_rv_review_tv.text = reviewList[position]
    }

    fun setList(inputList: List<String>) {
        reviewList = inputList
    }


    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

}