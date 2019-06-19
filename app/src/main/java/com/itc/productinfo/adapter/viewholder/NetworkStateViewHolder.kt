package com.itc.productinfo.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itc.productinfo.R
import com.itc.productinfo.paging.NetworkState
import kotlinx.android.synthetic.main.row_view_network_state.view.*

class NetworkStateViewHolder(view:View, private val retryCallback: () -> Unit):RecyclerView.ViewHolder(view) {
    init {
        itemView.retryLoadingButton.setOnClickListener { retryCallback() }
    }
    fun bindTo(networkState: NetworkState?) {

    }
    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.row_view_network_state, parent, false)
            return NetworkStateViewHolder(view, retryCallback)
        }
    }

}