package com.nikola.jakshic.dagger.di

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.module.AppGlideModule
import com.nikola.jakshic.dagger.DaggerApp
import okhttp3.OkHttpClient
import java.io.InputStream
import javax.inject.Inject

@GlideModule
class DaggerGlideModule : AppGlideModule() {

    @Inject lateinit var client: OkHttpClient

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDefaultTransitionOptions(Drawable::class.java, DrawableTransitionOptions.withCrossFade())
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        (context.applicationContext as DaggerApp).appComponent.inject(this)
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(client))
    }
}