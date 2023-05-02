package com.nikola.jakshic.dagger

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.transition.TransitionManager
import com.nikola.jakshic.dagger.assets.AssetsViewModel
import com.nikola.jakshic.dagger.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var assetsViewModel: AssetsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                assetsViewModel.statusMessage.collect { statusMessage ->
                    if (statusMessage != null) {
                        binding.tvDownloadingAssets.text = statusMessage
                    }
                    val textViewHeight = when {
                        statusMessage != null -> LinearLayout.LayoutParams.WRAP_CONTENT
                        else -> 0
                    }
                    if (textViewHeight == binding.tvDownloadingAssets.layoutParams.height) {
                        return@collect
                    }
                    if (statusMessage == null) {
                        delay(3000)
                    }
                    TransitionManager.beginDelayedTransition(binding.root)
                    binding.tvDownloadingAssets.updateLayoutParams<LinearLayout.LayoutParams> {
                        height = textViewHeight
                    }
                }
            }
        }
    }
}
