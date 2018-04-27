package com.nikola.jakshic.dagger.ui.profile.peer

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.nikola.jakshic.dagger.R

class PeerSortDialog : DialogFragment() {

    interface OnSortListener {
        fun onSort(sort: Int)
    }

    companion object {
        fun newInstance() = PeerSortDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(context!!)
                .setTitle("Sort by")
                .setItems(R.array.sort_peer_options) { _, which ->
                    (targetFragment as OnSortListener).onSort(which)
                }
                .create()
    }
}