package com.mvvm.mycarrot.adapter.recyclerView

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizonSpacingItemDecoration(private val verticalSpaceHeight: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right = verticalSpaceHeight / 2
        outRect.left = verticalSpaceHeight / 2
    }
}