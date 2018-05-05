package com.nikola.jakshic.dagger.vo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlayerStats(
        @SerializedName("account_id") @Expose var id: Long = 0,
        @SerializedName("name") @Expose var name: String? = null,
        @SerializedName("personaname") @Expose var personaName: String? = null,
        @SerializedName("player_slot") @Expose var playerSlot: Long = 0,
        @SerializedName("assists") @Expose var assists: Long = 0,
        @SerializedName("backpack_0") @Expose var backpack0: Long = 0,
        @SerializedName("backpack_1") @Expose var backpack1: Long = 0,
        @SerializedName("backpack_2") @Expose var backpack2: Long = 0,
        @SerializedName("deaths") @Expose var deaths: Long = 0,
        @SerializedName("denies") @Expose var denies: Long = 0,
        @SerializedName("gold_per_min") @Expose var goldPerMin: Long = 0,
        @SerializedName("hero_damage") @Expose var heroDamage: Long = 0,
        @SerializedName("hero_healing") @Expose var heroHealing: Long = 0,
        @SerializedName("hero_id") @Expose var heroId: Long = 0,
        @SerializedName("item_0") @Expose var item0: Long = 0,
        @SerializedName("item_1") @Expose var item1: Long = 0,
        @SerializedName("item_2") @Expose var item2: Long = 0,
        @SerializedName("item_3") @Expose var item3: Long = 0,
        @SerializedName("item_4") @Expose var item4: Long = 0,
        @SerializedName("item_5") @Expose var item5: Long = 0,
        @SerializedName("kills") @Expose var kills: Long = 0,
        @SerializedName("last_hits") @Expose var lastHits: Long = 0,
        @SerializedName("level") @Expose var level: Long,
        @SerializedName("tower_damage") @Expose var towerDamage: Long,
        @SerializedName("xp_per_min") @Expose var xpPerMin: Long,
        @SerializedName("purchase_ward_observer") @Expose var purchaseWardObserver: Long,
        @SerializedName("purchase_ward_sentry") @Expose var purchaseWardSentry: Long
)