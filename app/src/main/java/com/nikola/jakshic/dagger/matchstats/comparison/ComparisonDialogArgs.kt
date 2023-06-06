package com.nikola.jakshic.dagger.matchstats.comparison

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle

private const val EXTRA_LEFT_PLAYER_INDEX = "left-player-index"
private const val EXTRA_RIGHT_PLAYER_INDEX = "right-player-index"
private const val EXTRA_HERO_IDS = "hero-ids"

data class ComparisonDialogArgs(
    val leftPlayerIndex: Int,
    val rightPlayerIndex: Int,
    val heroIds: List<Long>,
) {
    fun toBundle(): Bundle {
        return bundleOf(
            EXTRA_LEFT_PLAYER_INDEX to leftPlayerIndex,
            EXTRA_RIGHT_PLAYER_INDEX to rightPlayerIndex,
            EXTRA_HERO_IDS to heroIds.toLongArray(),
        )
    }

    companion object {
        fun fromBundle(bundle: Bundle): ComparisonDialogArgs {
            if (!bundle.containsKey(EXTRA_LEFT_PLAYER_INDEX)) {
                throw IllegalArgumentException("""Required argument "$EXTRA_LEFT_PLAYER_INDEX" is missing.""")
            }
            if (!bundle.containsKey(EXTRA_RIGHT_PLAYER_INDEX)) {
                throw IllegalArgumentException("""Required argument "$EXTRA_RIGHT_PLAYER_INDEX" is missing.""")
            }
            if (!bundle.containsKey(EXTRA_HERO_IDS)) {
                throw IllegalArgumentException("""Required argument "$EXTRA_HERO_IDS" is missing.""")
            }
            return ComparisonDialogArgs(
                bundle.getInt(EXTRA_LEFT_PLAYER_INDEX),
                bundle.getInt(EXTRA_RIGHT_PLAYER_INDEX),
                bundle.getLongArray(EXTRA_HERO_IDS)!!.toList(),
            )
        }

        fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): ComparisonDialogArgs {
            if (!savedStateHandle.contains(EXTRA_LEFT_PLAYER_INDEX)) {
                throw IllegalArgumentException("""Required argument "$EXTRA_LEFT_PLAYER_INDEX" is missing.""")
            }
            if (!savedStateHandle.contains(EXTRA_RIGHT_PLAYER_INDEX)) {
                throw IllegalArgumentException("""Required argument "$EXTRA_RIGHT_PLAYER_INDEX" is missing.""")
            }
            if (!savedStateHandle.contains(EXTRA_HERO_IDS)) {
                throw IllegalArgumentException("""Required argument "$EXTRA_HERO_IDS" is missing.""")
            }
            return ComparisonDialogArgs(
                savedStateHandle.get<Int>(EXTRA_LEFT_PLAYER_INDEX)!!,
                savedStateHandle.get<Int>(EXTRA_RIGHT_PLAYER_INDEX)!!,
                savedStateHandle.get<LongArray>(EXTRA_HERO_IDS)!!.toList(),
            )
        }
    }
}
