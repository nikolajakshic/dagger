package com.nikola.jakshic.dagger.common.extensions

import android.content.res.Resources.NotFoundException
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

@ColorInt
fun Fragment.getColor(@ColorRes id: Int): Int {
    return ContextCompat.getColor(requireContext(), id)
}

fun Fragment.getDrawable(@DrawableRes id: Int): Drawable {
    return ContextCompat.getDrawable(requireContext(), id) ?: throw NotFoundException()
}
