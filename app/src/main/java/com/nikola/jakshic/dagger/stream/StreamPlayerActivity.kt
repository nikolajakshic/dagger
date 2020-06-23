package com.nikola.jakshic.dagger.stream

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
}