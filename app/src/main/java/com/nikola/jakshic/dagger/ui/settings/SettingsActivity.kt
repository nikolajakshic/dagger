package com.nikola.jakshic.dagger.ui.settings

import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.jakewharton.rxbinding2.view.RxView
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.data.local.SearchHistoryDao
import com.nikola.jakshic.dagger.toast
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.settings_item_history.*
import javax.inject.Inject

class SettingsActivity : AppCompatActivity() {

    @Inject lateinit var searchHistoryDao: SearchHistoryDao
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as DaggerApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Enable up navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Delete search history
        val disposable = RxView.clicks(settingsHistoryClear)
                .observeOn(Schedulers.io())
                .subscribe(
                        {
                            searchHistoryDao.deleteHistory()
                            runOnUiThread { toast("Search history cleared") }
                        },
                        { error -> })

        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Clear all the subscriptions
        compositeDisposable.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
        // Navigate up to parent activity
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}