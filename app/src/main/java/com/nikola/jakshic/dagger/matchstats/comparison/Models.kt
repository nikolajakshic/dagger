package com.nikola.jakshic.dagger.matchstats.comparison

import com.nikola.jakshic.dagger.common.sqldelight.SelectHeroImagesById

fun SelectHeroImagesById.mapToUi(): List<String?> {
    val items = mutableListOf<String?>()
    items += heroImage0
    items += heroImage1
    items += heroImage2
    items += heroImage3
    items += heroImage4
    items += heroImage5
    items += heroImage6
    items += heroImage7
    items += heroImage8
    items += heroImage9
    return items
}
