package com.nikola.jakshic.dagger.common

import android.content.Context
import com.nikola.jakshic.dagger.R
import java.util.concurrent.TimeUnit

fun timeElapsed(context: Context, endTime: Long): String {
    val timePassed = System.currentTimeMillis() - endTime

    val years = TimeUnit.MILLISECONDS.toDays(timePassed) / 365
    val months = TimeUnit.MILLISECONDS.toDays(timePassed) / 30
    val days = TimeUnit.MILLISECONDS.toDays(timePassed)
    val hours = TimeUnit.MILLISECONDS.toHours(timePassed)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timePassed)

    return when {
        years > 0 -> context.resources.getQuantityString(R.plurals.year, years.toInt(), years)
        months > 0 -> context.resources.getQuantityString(R.plurals.month, months.toInt(), months)
        days > 0 -> context.resources.getQuantityString(R.plurals.day, days.toInt(), days)
        hours > 0 -> context.resources.getQuantityString(R.plurals.hour, hours.toInt(), hours)
        else -> context.resources.getQuantityString(R.plurals.minute, minutes.toInt(), minutes)
    }
}