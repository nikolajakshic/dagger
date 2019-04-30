package com.nikola.jakshic.dagger.vo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StreamBox(@SerializedName("data") @Expose val stream: List<Stream>)

data class Stream(
        @SerializedName("user_name") @Expose val userName: String,
        @SerializedName("title") @Expose val title: String,
        @SerializedName("viewer_count") @Expose val viewerCount: Long,
        @SerializedName("thumbnail_url") @Expose val thumbnailUrl: String)