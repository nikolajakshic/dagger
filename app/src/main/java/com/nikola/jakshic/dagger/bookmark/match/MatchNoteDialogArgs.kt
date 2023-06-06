package com.nikola.jakshic.dagger.bookmark.match

import android.os.Bundle
import androidx.core.os.bundleOf

private const val EXTRA_MATCH_ID = "match-id"
private const val EXTRA_NOTE = "note"

data class MatchNoteDialogArgs(val matchId: Long, val note: String?) {
    fun toBundle(): Bundle {
        return bundleOf(
            EXTRA_MATCH_ID to matchId,
            EXTRA_NOTE to note,
        )
    }

    companion object {
        fun fromBundle(bundle: Bundle): MatchNoteDialogArgs {
            if (!bundle.containsKey(EXTRA_MATCH_ID)) {
                throw IllegalArgumentException("""Required argument "$EXTRA_MATCH_ID" is missing.""")
            }
            if (!bundle.containsKey(EXTRA_NOTE)) {
                throw IllegalArgumentException("""Required argument "$EXTRA_NOTE" is missing.""")
            }
            return MatchNoteDialogArgs(
                bundle.getLong(EXTRA_MATCH_ID),
                bundle.getString(EXTRA_NOTE),
            )
        }
    }
}
