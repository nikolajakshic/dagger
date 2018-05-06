package com.nikola.jakshic.dagger.vo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlayerStats(
        @SerializedName("account_id") @Expose var id: Long,
        @SerializedName("name") @Expose var name: String?,
        @SerializedName("personaname") @Expose var personaName: String?,
        @SerializedName("player_slot") @Expose var playerSlot: Int,
        @SerializedName("assists") @Expose var assists: Int,
        @SerializedName("backpack_0") @Expose var backpack0: Int,
        @SerializedName("backpack_1") @Expose var backpack1: Int,
        @SerializedName("backpack_2") @Expose var backpack2: Int,
        @SerializedName("deaths") @Expose var deaths: Int,
        @SerializedName("denies") @Expose var denies: Int,
        @SerializedName("gold_per_min") @Expose var goldPerMin: Int,
        @SerializedName("hero_damage") @Expose var heroDamage: Int,
        @SerializedName("hero_healing") @Expose var heroHealing: Int,
        @SerializedName("hero_id") @Expose var heroId: Int,
        @SerializedName("item_0") @Expose var item0: Int,
        @SerializedName("item_1") @Expose var item1: Int,
        @SerializedName("item_2") @Expose var item2: Int,
        @SerializedName("item_3") @Expose var item3: Int,
        @SerializedName("item_4") @Expose var item4: Int,
        @SerializedName("item_5") @Expose var item5: Int,
        @SerializedName("kills") @Expose var kills: Int,
        @SerializedName("last_hits") @Expose var lastHits: Int,
        @SerializedName("level") @Expose var level: Int,
        @SerializedName("tower_damage") @Expose var towerDamage: Int,
        @SerializedName("xp_per_min") @Expose var xpPerMin: Int,
        @SerializedName("purchase_ward_observer") @Expose var purchaseWardObserver: Int,
        @SerializedName("purchase_ward_sentry") @Expose var purchaseWardSentry: Int
)