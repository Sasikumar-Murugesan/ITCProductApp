package com.itc.productinfo.paging

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PageKeyedDataSource
import com.itc.productinfo.model.ProductModel
import com.itc.productinfo.retrofit.ApiInterface
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class ProductDataSource(val apiInterface: ApiInterface, val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int, ProductModel.Product>() {
    companion object{
        var pageNo: Int = 1
    }
    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()
    /**
     * Keep Completable reference for the retry event
     */
    private var retryCompletable: Completable? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, ProductModel.Product>) {
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        //get the initial users from the api
        compositeDisposable.add(apiInterface.getProductsList(pageNo, params.requestedLoadSize).subscribe({ products ->
            // clear retry since last request succeeded
            setRetry(null)
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            pageNo+=1
            products?.body()?.products?.let { callback.onResult(it,null,pageNo) }
        }, { throwable ->
            // keep a Completable for future retry
            setRetry(Action { loadInitial(params, callback) })
            val error = NetworkState.error(throwable.message)
            // publish the error
            networkState.postValue(error)
            initialLoad.postValue(error)
        }))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ProductModel.Product>) {

        // set network value to loading.
        networkState.postValue(NetworkState.LOADING)

        //get the users from the api after id
        compositeDisposable.add(apiInterface.getProductsList(params.key, params.requestedLoadSize).subscribe({ products ->
            // clear retry since last request succeeded
            setRetry(null)
            networkState.postValue(NetworkState.LOADED)
            pageNo+=1
            products?.body()?.products?.let { callback.onResult(it,pageNo) }
        }, { throwable ->
            // keep a Completable for future retry
            setRetry(Action { loadAfter(params, callback) })
            // publish the error
            networkState.postValue(NetworkState.error(throwable.message))
        }))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ProductModel.Product>) {

    }
    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ }, { throwable -> Log.e("Error:",throwable.message) }))
        }
    }
    private fun setRetry(action: Action?) {
        if (action == null) {
            this.retryCompletable = null
        } else {
            this.retryCompletable = Completable.fromAction(action)
        }
    }
 }

