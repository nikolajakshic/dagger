package com.nikola.jakshic.dagger.common

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

fun Activity.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun Fragment.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(activity, msg, duration).show()
}

fun Fragment.show(transition: Int = FragmentTransaction.TRANSIT_NONE) {
    fragmentManager?.beginTransaction()
            ?.show(this)
            ?.setTransition(transition)
            ?.commit()
}

fun Fragment.hide(transition: Int = FragmentTransaction.TRANSIT_NONE) {
    fragmentManager?.beginTransaction()
            ?.hide(this)
            ?.setTransition(transition)
            ?.commit()
}

fun ViewGroup.inflate(resource: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(this.context).inflate(resource, this, attachToRoot)
}

fun Activity.hasNetworkConnection(): Boolean {
    val connectivityManager: ConnectivityManager? = (applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)

    val activeNetwork = connectivityManager?.activeNetworkInfo
    return (activeNetwork?.isConnected == true)
}

fun Fragment.hasNetworkConnection(): Boolean {
    val connectivityManager: ConnectivityManager? = (context?.applicationContext
            ?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)

    val activeNetwork = connectivityManager?.activeNetworkInfo
    return (activeNetwork?.isConnected == true)
}