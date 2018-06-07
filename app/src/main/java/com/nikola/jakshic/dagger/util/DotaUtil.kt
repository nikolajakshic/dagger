package com.nikola.jakshic.dagger.util

import android.content.Context
import android.util.SparseArray

object DotaUtil {

    private const val item = "ic_item_"
    private const val recipe = "ic_item_recipe"
    private const val hero = "ic_hero_"
    private const val medal = "ic_rank_"
    private const val stars = "ic_rank_star_"

    val mode = SparseArray<String>()
    val lobby = SparseArray<String>()
    val skill = SparseArray<String>()

    private val recipes = intArrayOf(
            35, 47, 64, 72, 74, 76, 78, 85, 89, 91, 93, 97, 245, 99, 233,
            101, 103, 105, 109, 111, 115, 118, 122, 255, 258, 266, 243, 136, 140, 142,
            144, 146, 148, 150, 248, 157, 161, 169, 173, 177, 184, 189, 230, 205, 207, 238)

    init {
        with(mode) {
            append(0, "Unknown")
            append(1, "All Pick")
            append(2, "Captains Mode")
            append(3, "Random Draft")
            append(4, "Single Draft")
            append(5, "All Random")
            append(6, "Intro")
            append(7, "Diretide")
            append(8, "Reverse Captains Mode")
            append(9, "Greeviling")
            append(10, "Tutorial")
            append(11, "Mid Only")
            append(12, "Least Played")
            append(13, "Limited Heroes")
            append(14, "Compendium Matchmaking")
            append(15, "Custom")
            append(16, "Captains Draft")
            append(17, "Balanced Draft")
            append(18, "Ability Draft")
            append(19, "Event")
            append(20, "All Random Deathmatch")
            append(21, "1v1 Mid")
            append(22, "All Draft")
            append(23, "Turbo")
            append(24, "Mutation")
        }
        with(lobby) {
            append(0, "Normal")
            append(1, "Practice")
            append(2, "Tournament")
            append(3, "Tutorial")
            append(4, "Coop Bots")
            append(5, "Ranked Team MM")
            append(6, "Ranked Solo MM")
            append(7, "Ranked")
            append(8, "1v1 Mid")
            append(9, "Battle Cup")
        }
        with(skill) {
            append(1, "Normal")
            append(2, "High")
            append(3, "Very High")
        }
    }

    fun getItem(context: Context, id: Int): Int {
        val resource = context.resources
        val packageName = context.packageName

        return when (id) {
            in recipes -> resource.getIdentifier(recipe, "drawable", packageName)
            else -> resource.getIdentifier(item + id, "drawable", packageName)
        }
    }

    fun getHero(context: Context, id: Int): Int {
        val resource = context.resources
        val packageName = context.packageName

        return resource.getIdentifier(hero + id, "drawable", packageName)
    }

    fun getMedal(context: Context, rankTier: Int, leaderBoardRank: Int): Int {
        val resource = context.resources
        val packageName = context.packageName

        val medalName = when {
            rankTier == 0 -> medal + "0"
            leaderBoardRank in 1..10 -> medal + "7c"
            leaderBoardRank in 11..100 -> medal + "7b"
            leaderBoardRank in 101..1000 -> medal + "7a"
            else -> medal + rankTier / 10
        }

        return resource.getIdentifier(medalName, "drawable", packageName)
    }

    fun getStars(context: Context, rankTier: Int, leaderBoardRank: Int): Int {
        val resource = context.resources
        val packageName = context.packageName

        val starName = when {
        // Resources.getIdentifier throws Exception if name param is null,
        // so we need to return empty String
            leaderBoardRank in 1..1000 -> "" // top 1000 players have special medals without stars
            else -> stars + rankTier % 10
        }

        return resource.getIdentifier(starName, "drawable", packageName)
    }
}