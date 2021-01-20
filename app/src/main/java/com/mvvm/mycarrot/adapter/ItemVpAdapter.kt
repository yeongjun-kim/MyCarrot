package com.mvvm.mycarrot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mvvm.mycarrot.R
import kotlinx.android.synthetic.main.item_vp_write.view.*

/**
 *
 * Image Url List 이용
 *
 */
class ItemVpAdapter(var urlList: ArrayList<String>) :
    RecyclerView.Adapter<ItemVpAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_vp_write, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(urlList[position])
            .into(holder.itemView.item_vp_iv)
    }


    override fun getItemCount() = urlList.size

    fun setList(inputList: ArrayList<String>) {
        urlList = inputList
        notifyDataSetChanged()
    }
}