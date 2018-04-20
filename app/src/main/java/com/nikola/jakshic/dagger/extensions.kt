package com.nikola.jakshic.dagger

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.widget.Toast

fun Activity.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun Fragment.show(transition: Int = FragmentTransaction.TRANSIT_FRAGMENT_FADE) {
    fragmentManager?.beginTransaction()
            ?.show(this)
            ?.setTransition(transition)
            ?.commit()
}

fun Fragment.hide(transition: Int = FragmentTransaction.TRANSIT_FRAGMENT_FADE) {
    fragmentManager?.beginTransaction()
            ?.hide(this)
            ?.setTransition(transition)
            ?.commit()
}