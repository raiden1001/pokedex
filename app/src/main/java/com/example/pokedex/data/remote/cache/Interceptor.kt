package com.example.pokedex.data.remote.cache

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class Interceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = if (isNetworkAvailable())
            request
                .newBuilder()
                .cacheControl(
                    CacheControl.Builder()
                        .maxAge(30, TimeUnit.MINUTES)
                        .build()
                )
                .build()
        else
            request
                .newBuilder()
                .cacheControl(
                    CacheControl.Builder()
                        .maxStale(1, TimeUnit.DAYS)
                        .build()
                )
                .build()

        val response: Response = chain.proceed(request)
        val cacheControl = CacheControl.Builder().maxAge(10, TimeUnit.DAYS).build()
        return response.newBuilder().header("Cache-control", cacheControl.toString()).build()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

}