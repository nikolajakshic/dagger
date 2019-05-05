package com.nikola.jakshic.dagger.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.search.SearchHistoryDao
import com.nikola.jakshic.dagger.common.toast
import kotlinx.android.synthetic.main.settings_item_history.*
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SettingsActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    @Inject lateinit var searchHistoryDao: SearchHistoryDao

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as DaggerApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Enable up navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        settingsHistoryClear.setOnClickListener {
            launch {
                withContext(Dispatchers.IO) { searchHistoryDao.deleteHistory() }
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