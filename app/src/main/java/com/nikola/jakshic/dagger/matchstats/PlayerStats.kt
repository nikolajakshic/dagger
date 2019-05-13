package com.nikola.jakshic.dagger.matchstats

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "player_stats",
    primaryKeys = ["match_id", "account_id", "player_slot"],
    foreignKeys = [(ForeignKey(
        entity = MatchStats::class,
        parentColumns = ["match_id"],
        childColumns = ["match_id"],
        onDelete = ForeignKey.CASCADE))])
@JsonClass(generateAdapter = true)
data class PlayerStats(
    @ColumnInfo(name = "account_id") @Json(name = "account_id") var id: Long,
    @ColumnInfo(name = "match_id") @Json(name = "match_id") var matchId: Long,
    @ColumnInfo(name = "name") @Json(name = "name") var name: String?,
    @ColumnInfo(name = "persona_name") @Json(name = "personaname") var personaName: String?,
    @ColumnInfo(name = "player_slot") @Json(name = "player_slot") var playerSlot: Int,
    @ColumnInfo(name = "assists") @Json(name = "assists") var assists: Int,
    @ColumnInfo(name = "backpack_0") @Json(name = "backpack_0") var backpack0: Int,
    @ColumnInfo(name = "backpack_1") @Json(name = "backpack_1") var backpack1: Int,
    @ColumnInfo(name = "backpack_2") @Json(name = "backpack_2") var backpack2: Int,
    @ColumnInfo(name = "deaths") @Json(name = "deaths") var deaths: Int,
    @ColumnInfo(name = "denies") @Json(name = "denies") var denies: Int,
    @ColumnInfo(name = "gpm") @Json(name = "gold_per_min") var goldPerMin: Int,
    @ColumnInfo(name = "hero_damage") @Json(name = "hero_damage") var heroDamage: Int,
    @ColumnInfo(name = "hero_healing") @Json(name = "hero_healing") var heroHealing: Int,
    @ColumnInfo(name = "hero_id") @Json(name = "hero_id") var heroId: Int,
    @ColumnInfo(name = "item_0") @Json(name = "item_0") var item0: Int,
    @ColumnInfo(name = "item_1") @Json(name = "item_1") var item1: Int,
    @ColumnInfo(name = "item_2") @Json(name = "item_2") var item2: Int,
    @ColumnInfo(name = "item_3") @Json(name = "item_3") var item3: Int,
    @ColumnInfo(name = "item_4") @Json(name = "item_4") var item4: Int,
    @ColumnInfo(name = "item_5") @Json(name = "item_5") var item5: Int,
    @ColumnInfo(name = "kills") @Json(name = "kills") var kills: Int,
    @ColumnInfo(name = "last_hits") @Json(name = "last_hits") var lastHits: Int,
    @ColumnInfo(name = "level") @Json(name = "level") var level: Int,
    @ColumnInfo(name = "tower_damage") @Json(name = "tower_damage") var towerDamage: Int,
    @ColumnInfo(name = "xpm") @Json(name = "xp_per_min") var xpPerMin: Int,
    @ColumnInfo(name = "observers") @Json(name = "purchase_ward_observer") var purchaseWardObserver: Int = 0,
    @ColumnInfo(name = "sentries") @Json(name = "purchase_ward_sentry") var purchaseWardSentry: Int = 0
)