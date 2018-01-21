package com.nikola.jakshic.truesight;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.nikola.jakshic.truesight.model.Player;

public class FollowDialog extends DialogFragment {

    private static Player mPlayer;

    public FollowDialog() {
    }

    public static FollowDialog newInstance(Player player) {
        FollowDialog followDialog = new FollowDialog();
        mPlayer = player;
        return followDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to unfollow?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                            Singletons.getDb(getContext()).playerDao().deletePlayer(mPlayer);
                        })
                .setNegativeButton("Cancel", null)
                .create();
    }
}