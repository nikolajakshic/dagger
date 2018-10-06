package com.nikola.jakshic.dagger.vo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "player_stats",
        primaryKeys = ["match_id", "account_id", "player_slot"],
        foreignKeys = [(ForeignKey(
                entity = MatchStats::class,
                parentColumns = ["match_id"],
                childColumns = ["match_id"],
                onDelete = ForeignKey.CASCADE))])
data class PlayerStats(
        @ColumnInfo(name = "account_id") @SerializedName("account_id") @Expose var id: Long,
        @ColumnInfo(name = "match_id") @SerializedName("match_id") @Expose var matchId: Long,
        @ColumnInfo(name = "name") @SerializedName("name") @Expose var name: String?,
        @ColumnInfo(name = "persona_name") @SerializedName("personaname") @Expose var personaName: String?,
        @ColumnInfo(name = "player_slot") @SerializedName("player_slot") @Expose var playerSlot: Int,
        @ColumnInfo(name = "assists") @SerializedName("assists") @Expose var assists: Int,
        @ColumnInfo(name = "backpack_0") @SerializedName("backpack_0") @Expose var backpack0: Int,
        @ColumnInfo(name = "backpack_1") @SerializedName("backpack_1") @Expose var backpack1: Int,
        @ColumnInfo(name = "backpack_2") @SerializedName("backpack_2") @Expose var backpack2: Int,
        @ColumnInfo(name = "deaths") @SerializedName("deaths") @Expose var deaths: Int,
        @ColumnInfo(name = "denies") @SerializedName("denies") @Expose var denies: Int,
        @ColumnInfo(name = "gpm") @SerializedName("gold_per_min") @Expose var goldPerMin: Int,
        @ColumnInfo(name = "hero_damage") @SerializedName("hero_damage") @Expose var heroDamage: Int,
        @ColumnInfo(name = "hero_healing") @SerializedName("hero_healing") @Expose var heroHealing: Int,
        @ColumnInfo(name = "hero_id") @SerializedName("hero_id") @Expose var heroId: Int,
        @ColumnInfo(name = "item_0") @SerializedName("item_0") @Expose var item0: Int,
        @ColumnInfo(name = "item_1") @SerializedName("item_1") @Expose var item1: Int,
        @ColumnInfo(name = "item_2") @SerializedName("item_2") @Expose var item2: Int,
        @ColumnInfo(name = "item_3") @SerializedName("item_3") @Expose var item3: Int,
        @ColumnInfo(name = "item_4") @SerializedName("item_4") @Expose var item4: Int,
        @ColumnInfo(name = "item_5") @SerializedName("item_5") @Expose var item5: Int,
        @ColumnInfo(name = "kills") @SerializedName("kills") @Expose var kills: Int,
        @ColumnInfo(name = "last_hits") @SerializedName("last_hits") @Expose var lastHits: Int,
        @ColumnInfo(name = "level") @SerializedName("level") @Expose var level: Int,
        @ColumnInfo(name = "tower_damage") @SerializedName("tower_damage") @Expose var towerDamage: Int,
        @ColumnInfo(name = "xpm") @SerializedName("xp_per_min") @Expose var xpPerMin: Int,
        @ColumnInfo(name = "observers") @SerializedName("purchase_ward_observer") @Expose var purchaseWardObserver: Int,
        @ColumnInfo(name = "sentries") @SerializedName("purchase_ward_sentry") @Expose var purchaseWardSentry: Int
)