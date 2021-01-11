package com.mvvm.mycarrot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import kotlinx.android.synthetic.main.item_rv_manner.view.*

class ItemRvMannerAdapter(var list: List<Pair<Int, String>>) :
    RecyclerView.Adapter<ItemRvMannerAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_manner, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount() = list.size
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.itemView.itemRvManner_tv_count.text = list[position].first.toString()
        holder.itemView.itemRvManner_tv_manner.text = list[position].second
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)
}