package com.mvvm.mycarrot.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mvvm.mycarrot.R
import kotlinx.android.synthetic.main.item_vp_write.view.*

class WriteVpAdapter(var imageList: ArrayList<Uri>) :
    RecyclerView.Adapter<WriteVpAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_vp_write, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(imageList[position])
            .into(holder.itemView.item_vp_iv)
    }


    override fun getItemCount() = imageList.size

    fun setList(inputList: ArrayList<Uri>){
        imageList = inputList
        notifyDataSetChanged()
    }
}