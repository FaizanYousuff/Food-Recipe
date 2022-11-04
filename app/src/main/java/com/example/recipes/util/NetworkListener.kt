package com.example.recipes.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
class NetworkListener : ConnectivityManager.NetworkCallback() {

    private val isNetworkAvailable = MutableStateFlow(false)



    @RequiresApi(Build.VERSION_CODES.N)
    fun checkNetworkAvailability(context: Context) : MutableStateFlow<Boolean> {
       val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
               as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(this)
        var isConnected = false

        connectivityManager.allNetworks.forEach { network ->
           val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            networkCapabilities?.let {
                if(it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)){
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