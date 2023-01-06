package com.example.myfoodapp.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class NetworkListener : ConnectivityManager.NetworkCallback() {

    private val isNetworkAvailable = MutableStateFlow(false)

    fun checkNetworkAvailability(context: Context): MutableStateFlow<Boolean> {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(this)

        var isConnected = false
        // allNetworks Deprecated
        connectivityManager.allNetworks.forEach { network ->
            val networkCapability = connectivityManager.getNetworkCapabilities(network)
            networkCapability?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    isConnected = true
                    return@forEach
                }
            }
        }

        isNetworkAvailable.value = isConnected

        return isNetworkAvailable

    }

    override fun onAvailable(network: Network) {
        isNetworkAvailable.value = true

    }

    override fun onLost(network: Network) {
        isNetworkAvailable.value = false
    }
}


/**   private fun isNetworkAvailable(): Boolean {
val connectivityManager =
getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
val network = connectivityManager.activeNetwork // network is currently in a high power state for performing data transmission.
Log.d("Network", "active network $network")
network ?: return false // return false if network is null
val actNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false // return false if Network Capabilities is null
return when {
actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> { // check if wifi is connected
Log.d("Network", "wifi connected")
true
}
actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> { // check if mobile dats is connected
Log.d("Network", "cellular network connected")
true
}
else -> {
Log.d("Network", "internet not connected")
false
}
}
}
return false
}   */  //  future me ise Implement krna hai Line NO 14 pr function se