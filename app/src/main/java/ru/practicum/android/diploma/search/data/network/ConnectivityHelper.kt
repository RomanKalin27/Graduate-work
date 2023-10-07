package ru.practicum.android.diploma.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


class ConnectivityHelper(private val context: Context) {
    fun isInternetAvailable(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val networkConnection =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (networkConnection != null) {
            when {
                networkConnection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                networkConnection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                networkConnection.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
                networkConnection.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> return true
            }
        }
        return false
    }
}