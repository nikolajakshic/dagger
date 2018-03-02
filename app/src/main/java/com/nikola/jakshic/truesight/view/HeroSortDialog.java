package com.nikola.jakshic.truesight.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.nikola.jakshic.truesight.R;

public class HeroSortDialog extends DialogFragment {

    public HeroSortDialog() {
    }

    public interface OnSortListener {
        void onSort(int sortOption);
    }

    public static HeroSortDialog newInstance() {
        HeroSortDialog dialog = new HeroSortDialog();
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle("Sort by")
                .setItems(R.array.sort_hero_options, (dialog, which) ->
                    ((OnSortListener) getTargetFragment()).onSort(which))
                .create();
    }
}