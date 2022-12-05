package com.nikola.jakshic.dagger.common

import android.content.Context
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(activity, msg, duration).show()
}

fun ViewGroup.inflate(resource: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(this.context).inflate(resource, this, attachToRoot)
}

fun Fragment.hasNetworkConnection(): Boolean {
    val connectivityManager: ConnectivityManager? = (
        context?.applicationContext
            ?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        )

    val activeNetwork = connectivityManager?.activeNetworkInfo
    return (activeNetwork?.isConnected == true)
}
