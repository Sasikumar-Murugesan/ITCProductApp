package com.itc.productinfo.retrofit

import android.util.Log
import com.itc.productinfo.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private val interceptor: HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    public val BASE_URL = "https://mobile-tha-server.firebaseapp.com/"
    var instance: Retrofit? = null

    fun create(): ApiInterface {
        return createRetrofitInstance().create(ApiInterface::class.java)
    }

    fun createRetrofitInstance(): Retrofit {
        synchronized(this) {
            if (instance == null) {
                instance = Retrofit.Builder()
                    .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.create()
                    )
                    .addConverterFactory(
                        GsonConverterFactory.create()
                    )
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .build()
                return instance as Retrofit
            }
        }

        return instance as Retrofit
    }

    val httpClient: OkHttpClient = OkHttpClient.Builder().apply {
        readTimeout(60, TimeUnit.SECONDS)
        connectTimeout(60, TimeUnit.SECONDS)
        if (BuildConfig.RETROFIT_LOG_INTERCEPTOR) {
            addInterceptor(interceptor)
        }
    }.build()
}