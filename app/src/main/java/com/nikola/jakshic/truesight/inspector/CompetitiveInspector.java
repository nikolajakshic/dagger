package com.nikola.jakshic.truesight.inspector;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.TextView;

import com.nikola.jakshic.truesight.R;
import com.nikola.jakshic.truesight.model.Competitive;

import java.util.concurrent.TimeUnit;

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
    public boolean isRadiantWin(){
        return competitive.isRadiantWin();
    }

    public String getRadiantName() {
        return competitive.getRadiantName();
    }

    public String getRadiantScore() {
        return String.valueOf(competitive.getRadiantScore());
    }

    public String getDireName() {
        return competitive.getDireName();
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

    @BindingAdapter("setTrophy")
    public static void setTextDrawable(TextView view, boolean radiantWin) {
        if (radiantWin)
            view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_trophy, 0, 0, 0);
        else
            view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_trophy_invisible, 0, 0, 0);
    }
}