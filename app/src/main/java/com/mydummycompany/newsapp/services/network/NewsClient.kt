package com.mydummycompany.newsapp.services.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mydummycompany.newsapp.App
import com.mydummycompany.newsapp.BuildConfig
import com.mydummycompany.newsapp.utils.PreferencesUtil
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.AccessController.getContext
import java.util.concurrent.TimeUnit

object NewsClient {
    var logging: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    var cacheSize = 10 * 1024 * 1024 // 10 MB
    var fragmentContext: Context? = null
    var cache: Cache? = null
    private val authInterceptor = Interceptor { chain ->
        val newUrl = chain.request().url()
            .newBuilder()
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Token " + PreferencesUtil.getToken())
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }
    private val client =
        OkHttpClient().newBuilder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

    private fun retrofit() =
        Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.Base_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    fun apiService(): ApiService {
        return retrofit().create(ApiService::class.java)
    }

    fun hasNetwork(context: Context): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

    fun setMyContext(context: Context) {
        fragmentContext = context
        cache = Cache(fragmentContext!!.getCacheDir(), cacheSize.toLong())
    }
}