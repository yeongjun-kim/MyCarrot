package com.mvvm.mycarrot.adapter.viewPager

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mvvm.mycarrot.R
import kotlinx.android.synthetic.main.item_vp_write.view.*

/**
 *
 * Image Uri List 이용
 *
 */
class WriteVpAdapter(var uriList: ArrayList<Uri>) :
    RecyclerView.Adapter<WriteVpAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_vp_write, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(uriList[position])
            .into(holder.itemView.item_vp_iv)
    }


    override fun getItemCount() = uriList.size

    fun setList(inputList: ArrayList<Uri>) {
        uriList = inputList
        notifyDataSetChanged()
    }
}