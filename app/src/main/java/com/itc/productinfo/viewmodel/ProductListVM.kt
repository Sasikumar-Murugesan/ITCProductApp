package com.itc.productinfo.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.itc.productinfo.model.ProductModel
import com.itc.productinfo.paging.NetworkState
import com.itc.productinfo.paging.ProductDataSource
import com.itc.productinfo.paging.ProductDataSourceFactory
import com.itc.productinfo.retrofit.ApiInterface
import com.itc.productinfo.retrofit.RetrofitClient
import com.itc.productinfo.utils.ObservableViewModel
import io.reactivex.disposables.CompositeDisposable

class ProductListVM(application: Application) : ObservableViewModel(application) {

    var productItemPagedList: LiveData<PagedList<ProductModel.Product>>? = null
    private val compositeDisposable = CompositeDisposable()

    private val pageSize = 10

    private var sourceFactory: ProductDataSourceFactory? = null

    init {
        sourceFactory = ProductDataSourceFactory(compositeDisposable, RetrofitClient.create())
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(false)
                .build()
        sourceFactory?.let {
            productItemPagedList = LivePagedListBuilder<Int, ProductModel.Product>(it, config).build()
        }

    }
    fun retry() {
        sourceFactory?.productsDataSourceLiveData?.value!!.retry()
    }

    fun refresh() {
        sourceFactory!!.productsDataSourceLiveData.value!!.invalidate()
    }

    fun getNetworkState(): LiveData<NetworkState> = Transformations.switchMap<ProductDataSource, NetworkState>(
            sourceFactory!!.productsDataSourceLiveData, { it.networkState })

    fun getRefreshState(): LiveData<NetworkState> = Transformations.switchMap<ProductDataSource, NetworkState>(
            sourceFactory!!.productsDataSourceLiveData, { it.initialLoad })


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}