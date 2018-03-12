package com.nikola.jakshic.truesight.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.nikola.jakshic.truesight.R;

public class PeerSortDialog extends DialogFragment {

    public interface OnSortListener {
        void onSort(int sortOption);
    }

    public static PeerSortDialog newInstance() {
        PeerSortDialog dialog = new PeerSortDialog();
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle("Sort by")
                .setItems(R.array.sort_peer_options, (dialog, which) ->
                        ((OnSortListener) getTargetFragment()).onSort(which))
                .create();
    }
}
