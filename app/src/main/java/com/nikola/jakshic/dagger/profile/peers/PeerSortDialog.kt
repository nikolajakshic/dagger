package com.nikola.jakshic.dagger.profile.peers

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.nikola.jakshic.dagger.R

class PeerSortDialog : DialogFragment() {
    companion object {
        private const val KEY_RESULT = "result"
        private const val EXTRA_SORT_INDEX = "sort-index"

        fun setOnSortListener(
            fragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: (sortBy: SortBy) -> Unit
        ) {
            fragmentManager.setFragmentResultListener(
                KEY_RESULT,
                lifecycleOwner
            ) { _, result ->
                val sortBy = when (result.getInt(EXTRA_SORT_INDEX)) {
                    0 -> SortBy.GAMES
                    1 -> SortBy.WINRATE
                    else -> throw IllegalArgumentException()
                }
                listener(sortBy)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.sort_by))
            .setItems(R.array.sort_peer_options) { _, which ->
                parentFragmentManager.setFragmentResult(
                    /* requestKey */ KEY_RESULT,
                    /* result */ bundleOf(EXTRA_SORT_INDEX to which)
                )
            }.create()
    }
}
