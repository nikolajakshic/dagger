package com.nikola.jakshic.dagger.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.nikola.jakshic.dagger.R

class MedalDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(context!!)
                .setTitle("Medals")
                .setView(R.layout.dialog_medal)
                .setPositiveButton("Close", null)
                .create()
    }
}