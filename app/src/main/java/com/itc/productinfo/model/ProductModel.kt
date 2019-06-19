package com.itc.productinfo.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductModel(
    @SerializedName("pageNumber")
    var pageNumber: Int = 0,
    @SerializedName("pageSize")
    var pageSize: Int = 0,
    @SerializedName("products")
    var products: List<Product> = listOf(),
    @SerializedName("statusCode")
    var statusCode: Int = 0,
    @SerializedName("totalProducts")
    var totalProducts: Int = 0
) :Serializable{
    data class Product(
        @SerializedName("inStock")
        var inStock: Boolean = false,
        @SerializedName("longDescription")
        var longDescription: String = "",
        @SerializedName("price")
        var price: String = "",
        @SerializedName("productId")
        var productId: String = "",
        @SerializedName("productImage")
        var productImage: String = "",
        @SerializedName("productName")
        var productName: String = "",
        @SerializedName("reviewCount")
        var reviewCount: Int = 0,
        @SerializedName("reviewRating")
        var reviewRating: Double = 0.0,
        @SerializedName("shortDescription")
        var shortDescription: String = ""
    ):Serializable
}