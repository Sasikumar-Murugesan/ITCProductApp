package com.itc.productinfo.retrofit

import com.itc.productinfo.model.ProductModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("/walmartproducts/{pageNumber}/{pageSize}")
    fun getProductsList(@Path("pageNumber")pageNumber:Int, @Path("pageSize")pageSize:Int) : Observable<Response<ProductModel>>
}
