package com.nikola.jakshic.dagger.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class _Profile(@Expose val profile: Profile) {
}

data class Profile(@SerializedName("account_id") @Expose val id: Long,
                   @SerializedName("personaname") @Expose val name: String,
                   @SerializedName("avatarfull") @Expose val avatarUrl: String)