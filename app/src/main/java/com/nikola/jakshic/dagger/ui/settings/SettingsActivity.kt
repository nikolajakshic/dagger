package com.nikola.jakshic.dagger.ui.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.Dispatcher.IO
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.data.local.SearchHistoryDao
import com.nikola.jakshic.dagger.toast
import kotlinx.android.synthetic.main.settings_item_history.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject

class SettingsActivity : AppCompatActivity() {

    @Inject lateinit var searchHistoryDao: SearchHistoryDao

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as DaggerApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Enable up navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        settingsHistoryClear.setOnClickListener {
            launch(UI) {
                withContext(IO) { searchHistoryDao.deleteHistory() }
                toast("Search history cleared")
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
}