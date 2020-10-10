package com.nikola.jakshic.dagger.stream

import android.app.PictureInPictureParams
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nikola.jakshic.dagger.R
import kotlinx.android.synthetic.main.activity_stream_player.*

class StreamPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream_player)

        val userName = intent.getStringExtra("user-name")

        // WebView's layout_width & layout_height is set to match_parent
        // and it's blocking the background, we made it transparent
        // so we can show the "loading" background before the WebView starts.
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.loadUrl("https://player.twitch.tv/?channel=$userName&parent=twitch.tv&player=popout")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val userName = intent?.getStringExtra("user-name")
            ?: throw IllegalArgumentException("Intent with user-name extra must be passed.")
        webView.loadUrl("https://player.twitch.tv/?channel=$userName&parent=twitch.tv&player=popout")
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            enterPictureInPictureMode(PictureInPictureParams.Builder().build())
        }
    }

    override fun onBackPressed() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val isInPictureInPictureMode = enterPictureInPictureMode(PictureInPictureParams.Builder().build())
            if (isInPictureInPictureMode) {
                return
            }
        }
        super.onBackPressed()
    }
}