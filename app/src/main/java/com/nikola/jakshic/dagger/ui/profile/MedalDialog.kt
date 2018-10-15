package com.nikola.jakshic.dagger.ui.profile

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.nikola.jakshic.dagger.R

class MedalDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(context!!)
                .setTitle(getString(R.string.medals))
                .setView(R.layout.dialog_medal)
                .setPositiveButton(getString(R.string.close), null)
                .create()
    }
}