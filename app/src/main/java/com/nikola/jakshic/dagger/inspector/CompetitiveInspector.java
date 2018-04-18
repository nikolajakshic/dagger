package com.nikola.jakshic.dagger.inspector;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.TextView;

import com.nikola.jakshic.dagger.R;
import com.nikola.jakshic.dagger.model.Competitive;

import java.util.concurrent.TimeUnit;

/**
 * Inspector objects are used in XML files for Data Binding
 * If any changes needs to be done with Value Objects (models)
 * e.g. date is in milliseconds and needs to be converted to String
 * that is done in Inspector classes.
 */
public class CompetitiveInspector {

    private Context context;
    private Competitive competitive;

    public CompetitiveInspector(Context context, Competitive competitive) {
        this.context = context;
        this.competitive = competitive;
    }

    public String getLeagueName() {
        if (TextUtils.isEmpty(competitive.getLeagueName()))
            return "Unknown League";
        else
            return competitive.getLeagueName();
    }

    public boolean isRadiantWin() {
        return competitive.isRadiantWin();
    }

    public String getRadiantName() {
        String name = competitive.getRadiantName();
        if (TextUtils.isEmpty(name))
            name = "Unknown Team";
        return name;
    }

    public String getRadiantScore() {
        return String.valueOf(competitive.getRadiantScore());
    }

    public String getDireName() {
        String name = competitive.getDireName();
        if (TextUtils.isEmpty(name))
            name = "Unknown Team";
        return name;
    }

    public String getDireScore() {
        return String.valueOf(competitive.getDireScore());
    }

    public String getEndTime() {
        long SECOND_IN_MILLIS = 1000;
        long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
        long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
        long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;
        long MONTH_IN_MILLIS = DAY_IN_MILLIS * 30;
        long YEAR_IN_MILLIS = MONTH_IN_MILLIS * 12;

        long time = System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(competitive.getStartTime()) - TimeUnit.SECONDS.toMillis(competitive.getDuration());
        long minute = time / MINUTE_IN_MILLIS;
        long hour = time / HOUR_IN_MILLIS;
        long day = time / DAY_IN_MILLIS;
        long month = time / MONTH_IN_MILLIS;
        long year = time / YEAR_IN_MILLIS;

        if (year > 0)
            return context.getResources().getQuantityString(R.plurals.year, (int) year, year);
        else if (month > 0)
            return context.getResources().getQuantityString(R.plurals.month, (int) month, month);
        else if (day > 0)
            return context.getResources().getQuantityString(R.plurals.day, (int) day, day);
        else if (hour > 0)
            return context.getResources().getQuantityString(R.plurals.hour, (int) hour, hour);
        else
            return context.getResources().getQuantityString(R.plurals.minute, (int) minute, minute);
    }

    // TODO should not be in this file
    @BindingAdapter("setTrophy")
    public static void setTextDrawable(TextView view, boolean radiantWin) {
        if (radiantWin)
            view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_trophy, 0, 0, 0);
        else
            view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_trophy_invisible, 0, 0, 0);
    }
}