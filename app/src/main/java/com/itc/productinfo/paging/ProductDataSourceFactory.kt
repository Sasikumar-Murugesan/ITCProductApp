package com.itc.productinfo.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.itc.productinfo.model.ProductModel
import com.itc.productinfo.retrofit.ApiInterface
import io.reactivex.disposables.CompositeDisposable

class ProductDataSourceFactory(private val compositeDisposable: CompositeDisposable,
                               private val apiInterface: ApiInterface) : DataSource.Factory<Int, ProductModel.Product>() {
    val productsDataSourceLiveData = MutableLiveData<ProductDataSource>()
    override fun create(): DataSource<Int, ProductModel.Product> {
        val productDataSource = ProductDataSource(apiInterface, compositeDisposable)
        productsDataSourceLiveData.postValue(productDataSource)
        return productDataSource
    }

}