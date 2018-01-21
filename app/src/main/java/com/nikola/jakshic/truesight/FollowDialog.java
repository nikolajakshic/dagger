package com.nikola.jakshic.truesight;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.nikola.jakshic.truesight.model.Player;

public class FollowDialog extends DialogFragment {

    private static long playerId;

    public FollowDialog() {
    }

    public static FollowDialog newInstance(long id) {
        FollowDialog followDialog = new FollowDialog();
        playerId = id;
        return followDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to unfollow?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                            Singletons.getDb(getContext()).playerDao().deletePlayer(playerId);
                        })
                .setNegativeButton("Cancel", null)
                .create();
    }
}