package com.nikola.jakshic.truesight.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.nikola.jakshic.truesight.HeroComparator;
import com.nikola.jakshic.truesight.R;
import com.nikola.jakshic.truesight.viewModel.HeroViewModel;

public class HeroSortDialog extends DialogFragment {
    private static HeroViewModel mViewModel;

    public HeroSortDialog() {
    }

    public static HeroSortDialog newInstance(HeroViewModel viewModel) {
        HeroSortDialog dialog = new HeroSortDialog();
        mViewModel = viewModel;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle("Sort by")
                .setItems(R.array.sort_hero_options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            mViewModel.sort(new HeroComparator.ByGames());
                            break;
                        case 1:
                            mViewModel.sort(new HeroComparator.ByWinRate());
                            break;
                        case 2:
                            mViewModel.sort(new HeroComparator.ByWins());
                            break;
                        case 3:
                            mViewModel.sort(new HeroComparator.ByLosses());
                            break;
                    }
                })
                .create();
    }
}