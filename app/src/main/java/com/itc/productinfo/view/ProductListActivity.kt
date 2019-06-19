package com.itc.productinfo.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itc.productinfo.R
import com.itc.productinfo.adapter.ProductListAdapter
import com.itc.productinfo.databinding.ActivityProductListBinding
import com.itc.productinfo.model.ProductModel
import com.itc.productinfo.paging.NetworkState
import com.itc.productinfo.paging.Status
import com.itc.productinfo.viewmodel.ProductListVM
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.row_view_network_state.*

class ProductListActivity : AppCompatActivity() {
    val viewModel by lazy {
        ViewModelProviders.of(this).get(ProductListVM::class.java)
    }
    lateinit var productListAdapter: ProductListAdapter

    lateinit var binding: ActivityProductListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_product_list)
        binding.viewModel=viewModel
        initAdapter()
        initSwipeToRefresh()
    }


    private fun initAdapter() {
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        productListAdapter = ProductListAdapter {
            viewModel.retry()
        }
        binding.rvProductList.layoutManager = linearLayoutManager
        binding.rvProductList.adapter = productListAdapter
        viewModel.productItemPagedList?.observe(this, Observer<PagedList<ProductModel.Product>> {
            productListAdapter.submitList(it)
        })
        viewModel.getNetworkState().observe(this, Observer<NetworkState> {
            productListAdapter.setNetworkState(it)
        })
    }
    /**
     * Init swipe to refresh and enable pull to refresh only when there are items in the adapter
     */
    private fun initSwipeToRefresh() {
        viewModel.getRefreshState().observe(this, Observer { networkState ->
            if (productListAdapter.currentList != null) {
                if (productListAdapter.currentList!!.size > 0) {
                    binding.srPullToRefresh.isRefreshing = networkState?.status == NetworkState.LOADING.status
                } else {
                    //setInitialLoadingState(networkState)
                }
            } else {
                //setInitialLoadingState(networkState)
            }
        })
        binding.srPullToRefresh.setOnRefreshListener({ viewModel.refresh() })
    }

    /**
     * Show the current network state for the first load when the user list
     * in the adapter is empty and disable swipe to scroll at the first loading
     *
     * @param networkState the new network state
     */
    private fun setInitialLoadingState(networkState: NetworkState?) {
        //error message
        errorMessageTextView.visibility = if (networkState?.message != null) View.VISIBLE else View.GONE
        if (networkState?.message != null) {
            errorMessageTextView.text = networkState.message
        }

        //loading and retry
        retryLoadingButton.visibility = if (networkState?.status == Status.FAILED) View.VISIBLE else View.GONE
        loadingProgressBar.visibility = if (networkState?.status == Status.RUNNING) View.VISIBLE else View.GONE

        binding.srPullToRefresh.isEnabled = networkState?.status == Status.SUCCESS
        retryLoadingButton.setOnClickListener { viewModel.retry() }
    }

}
