package com.nikola.jakshic.dagger.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class FollowDialog extends DialogFragment {

    private static AlertDialog.OnClickListener mListener; // temporary solution

    public FollowDialog() {
    }

    public static FollowDialog newInstance(AlertDialog.OnClickListener listener) {
        FollowDialog followDialog = new FollowDialog();
        mListener = listener;
        return followDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to unfollow?")
                .setPositiveButton("Confirm", mListener)
                .setNegativeButton("Cancel", null)
                .create();
    }
}