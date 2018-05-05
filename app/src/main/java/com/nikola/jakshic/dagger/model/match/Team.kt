package com.nikola.jakshic.dagger.model.match

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Team(@SerializedName("name") @Expose var name: String?)