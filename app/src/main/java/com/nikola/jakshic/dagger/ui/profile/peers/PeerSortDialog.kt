package com.nikola.jakshic.dagger.ui.profile.peers

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.nikola.jakshic.dagger.R

class PeerSortDialog : DialogFragment() {

    interface OnSortListener {
        fun onSort(sort: Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!)
                .setTitle(getString(R.string.sort_by))
                .setItems(R.array.sort_peer_options) { _, which ->
                    (targetFragment as OnSortListener).onSort(which)
                }.create()
    }
}