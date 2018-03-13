package com.nikola.jakshic.truesight.inspector;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.TextView;

import com.nikola.jakshic.truesight.R;
import com.nikola.jakshic.truesight.model.Competitive;

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

    @BindingAdapter("setTrophy")
    public static void setTextDrawable(TextView view, boolean radiantWin) {
        if (radiantWin)
            view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_trophy, 0, 0, 0);
        else
            view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_trophy_invisible, 0, 0, 0);
    }
}