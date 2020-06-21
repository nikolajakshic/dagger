package com.nikola.jakshic.dagger.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.sqldelight.SearchHistoryQueries
import com.nikola.jakshic.dagger.common.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.settings_item_history.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity(), CoroutineScope {
    @Inject lateinit var searchHistoryQueries: SearchHistoryQueries
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Enable up navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        settingsHistoryClear.setOnClickListener {
            launch {
                withContext(Dispatchers.IO) { searchHistoryQueries.deleteAll() }
                toast(getString(R.string.info_search_history_cleared))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}