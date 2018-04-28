package com.nikola.jakshic.dagger.ui.profile.heroes

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.nikola.jakshic.dagger.R

class HeroSortDialog : DialogFragment() {

    interface OnSortListener {
        fun onSort(sort: Int)
    }

    companion object {
        fun newInstance() = HeroSortDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!)
                .setTitle("Sort by")
                .setItems(R.array.sort_hero_options, { _, which ->
                    (targetFragment as OnSortListener).onSort(which)
                }).create()
    }
}