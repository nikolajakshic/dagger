package com.nikola.jakshic.dagger.common.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

inline fun <reified T : Fragment?> FragmentManager.getFragmentByTag(tag: String): T {
    return findFragmentByTag(tag) as T
}
