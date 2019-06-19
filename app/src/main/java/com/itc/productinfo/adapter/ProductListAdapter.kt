package com.itc.productinfo.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.itc.productinfo.R
import com.itc.productinfo.adapter.viewholder.NetworkStateViewHolder
import com.itc.productinfo.adapter.viewholder.ProductViewHolder
import com.itc.productinfo.model.ProductModel
import com.itc.productinfo.paging.NetworkState

class ProductListAdapter(private val retryCallback: () -> Unit) : PagedListAdapter<ProductModel.Product, RecyclerView.ViewHolder>(ProductDiffCallback) {
    private var networkState: NetworkState? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.row_view_product_item -> ProductViewHolder.create(parent)
            R.layout.row_view_network_state -> NetworkStateViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.row_view_product_item -> (holder as ProductViewHolder).bindTo(getItem(position))
            R.layout.row_view_network_state -> (holder as NetworkStateViewHolder).bindTo(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.row_view_network_state
        } else {
            R.layout.row_view_product_item
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }
    /**
     * Set the current network state to the adapter
     * but this work only after the initial load
     * and the adapter already have list to add new loading raw to it
     * so the initial loading state the activity responsible for handle it
     *
     * @param newNetworkState the new network state
     */
    fun setNetworkState(newNetworkState: NetworkState?) {
        if (currentList != null) {
            if (currentList!!.size != 0) {
                val previousState = this.networkState
                val hadExtraRow = hasExtraRow()
                this.networkState = newNetworkState
                val hasExtraRow = hasExtraRow()
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (hasExtraRow && previousState !== newNetworkState) {
                    notifyItemChanged(itemCount - 1)
                }
            }
        }
    }
    companion object {
        val ProductDiffCallback = object : DiffUtil.ItemCallback<ProductModel.Product>() {
            override fun areItemsTheSame(oldItem: ProductModel.Product, newItem: ProductModel.Product): Boolean {
                return oldItem.productId == newItem.productId
            }

            override fun areContentsTheSame(oldItem: ProductModel.Product, newItem: ProductModel.Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}