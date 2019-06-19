package com.itc.productinfo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.itc.productinfo.R
import com.itc.productinfo.databinding.ActivityProductDetailBinding
import com.itc.productinfo.model.ProductModel
import com.itc.productinfo.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.row_view_product_item.*
import kotlinx.android.synthetic.main.row_view_product_item.view.*

class ProductDetailActivity : AppCompatActivity() {
    var productItem: ProductModel.Product? = null
    lateinit var binding: ActivityProductDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail)
        if (intent.hasExtra("productInfo")) {
            productItem = intent.getSerializableExtra("productInfo") as ProductModel.Product?
            if(productItem!=null)
            {
                binding.tvProductName.setText(productItem?.productName)
                binding.tvPrice.setText(productItem?.price)
                binding.tvLongDescrption.setText(Html.fromHtml(productItem?.longDescription))
                binding.ratingBar.rating=productItem?.reviewRating?.toFloat()?:0.toFloat()
                Glide.with(this)
                        .load(RetrofitClient.BASE_URL+productItem?.productImage)
                        .placeholder(R.drawable.ic_place_holder_image)
                        .into(binding.ivProductImg)
            }


        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
