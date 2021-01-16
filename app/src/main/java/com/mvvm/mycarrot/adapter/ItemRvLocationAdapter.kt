package com.mvvm.mycarrot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.room.Location
import com.mvvm.mycarrot.utils.onThrottleClick
import kotlinx.android.synthetic.main.item_rv_location.view.*
import kotlinx.android.synthetic.main.item_rv_review.view.*

class ItemRvLocationAdatper : RecyclerView.Adapter<ItemRvLocationAdatper.CustomViewHolder>() {

    interface ClickListener{
        fun onClick(position:Int)
    }

    var locationList = listOf<Location>()
    var listener:ClickListener? = null

    override fun getItemCount() = locationList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_location, parent,false)
        return CustomViewHolder(view)
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.itemView.itemRvLocation_tv.text = locationList[position].str
        holder.itemView.onThrottleClick {
            listener?.onClick(position)
        }
    }

    fun setList(inputList: List<Location>) {
        locationList = inputList
        notifyDataSetChanged()
    }


    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

}