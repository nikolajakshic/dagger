package com.nikola.jakshic.dagger.stream

import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.nikola.jakshic.dagger.databinding.ActivityStreamPlayerBinding

private const val EXTRA_USER_NAME = "user-name"

class StreamPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStreamPlayerBinding

    companion object {
        fun createIntent(context: Context, userName: String): Intent {
            val intent = Intent(context, StreamPlayerActivity::class.java)
            intent.putExtra(EXTRA_USER_NAME, userName)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreamPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra(EXTRA_USER_NAME)

        // WebView's layout_width & layout_height is set to match_parent
        // and it's blocking the background, we made it transparent
        // so we can show the "loading" background before the WebView starts.
        binding.webView.setBackgroundColor(Color.TRANSPARENT)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.loadUrl("https://player.twitch.tv/?channel=$userName&parent=twitch.tv&player=popout")

        onBackPressedDispatcher.addCallback(this) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val isInPictureInPictureMode =
                    enterPictureInPictureMode(PictureInPictureParams.Builder().build())
                if (isInPictureInPictureMode) {
                    return@addCallback
                }
            }
            isEnabled = false
            onBackPressedDispatcher.onBackPressed()
            isEnabled = true
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val userName = intent.getStringExtra(EXTRA_USER_NAME)
            ?: throw IllegalArgumentException("Intent with user-name extra must be passed.")
        binding.webView.loadUrl("https://player.twitch.tv/?channel=$userName&parent=twitch.tv&player=popout")
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            enterPictureInPictureMode(PictureInPictureParams.Builder().build())
        }
    }
}
