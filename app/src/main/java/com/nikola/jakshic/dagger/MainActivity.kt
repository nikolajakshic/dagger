package com.nikola.jakshic.dagger

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_home) {
    @Inject lateinit var assetsViewModel: AssetsViewModel
}
