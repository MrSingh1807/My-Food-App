package com.example.myfoodapp.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.flow.MutableStateFlow

class NetworkListener : ConnectivityManager.NetworkCallback() {

    private val isNetworkAvailable = MutableStateFlow(false)

    fun checkNetworkAvailability(context: Context): MutableStateFlow<Boolean> {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(this)

        val network = connectivityManager.activeNetwork
        if (network == null){
            isNetworkAvailable.value = false
            return isNetworkAvailable
        }

        val netCapabilities = connectivityManager.getNetworkCapabilities(network)
        if ( netCapabilities == null){
            isNetworkAvailable.value = false
            return isNetworkAvailable
        }

        return when {
            netCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                isNetworkAvailable.value = true
                isNetworkAvailable
            }
            netCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                isNetworkAvailable.value = true
                isNetworkAvailable
            }
            else -> {
                isNetworkAvailable.value = false
                isNetworkAvailable
            }
        }


    }

    override fun onAvailable(network: Network) {
        isNetworkAvailable.value = true
    }

    override fun onLost(network: Network) {
        isNetworkAvailable.value = false
    }
}