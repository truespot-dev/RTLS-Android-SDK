package com.example.truespotrtlsandroidsdk

import android.view.View
import androidx.recyclerview.widget.RecyclerView

typealias ClickHandler = (view: View, position: Int, type: Int) -> Unit

abstract class BaseRecyclerViewAdapter: RecyclerView.Adapter<BaseViewHolder>() {
    var onItemClickListener: ClickHandler? = null

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it1 -> it1(it, position, 0) }
        }
    }
}