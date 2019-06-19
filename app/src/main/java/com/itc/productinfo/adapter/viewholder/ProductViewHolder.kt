package com.itc.productinfo.adapter.viewholder

import android.app.Activity
import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itc.productinfo.model.ProductModel
import com.itc.productinfo.retrofit.RetrofitClient
import com.itc.productinfo.view.ProductDetailActivity
import kotlinx.android.synthetic.main.row_view_product_item.view.*


class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bindTo(item: ProductModel.Product?) {
        itemView.tv_product_name.text = item?.productName
        if (!item?.shortDescription.isNullOrEmpty()) {
            itemView.tv_short_desc.setText(Html.fromHtml(item?.shortDescription))
        } else {
            itemView.tv_short_desc.visibility = View.GONE
        }

        itemView.tv_price.setText(item?.price)
        if (item?.reviewRating != null && !item.reviewRating.toString().equals("0.0")) {
            itemView.rating_bar.text = item?.reviewRating.toString()
        } else {
            itemView.rating_bar.text = "Not Available"
        }
        Glide.with(itemView.context)
                .load(RetrofitClient.BASE_URL + item?.productImage)
                .placeholder(com.itc.productinfo.R.drawable.ic_place_holder_image)
                .into(itemView.imageView)

        itemView.setOnClickListener {
            (itemView.context as Activity).startActivity(Intent(itemView.context, ProductDetailActivity::class.java).apply {
                putExtra("productInfo", item)
            })
        }
    }

    companion object {
        fun create(parent: ViewGroup): ProductViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(com.itc.productinfo.R.layout.row_view_product_item, parent, false)
            return ProductViewHolder(view)
        }
    }

}