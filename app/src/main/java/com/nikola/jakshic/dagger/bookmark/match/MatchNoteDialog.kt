package com.nikola.jakshic.dagger.bookmark.match

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.nikola.jakshic.dagger.R
import kotlinx.android.synthetic.main.dialog_note_match.*

class MatchNoteDialog : DialogFragment() {
    private val noteWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val totalCharacters = s?.length ?: 0
            tvCharacterCounter.text = "$totalCharacters/140"
        }
    }

    companion object {
        fun newInstance(note: String, matchId: Long): MatchNoteDialog {
            val bundle = Bundle().apply {
                putString("note", note)
                putLong("match_id", matchId)
            }
            val dialog = MatchNoteDialog()
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun onStart() {
        super.onStart()
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_note_match, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val note = arguments!!.getString("note")
        val matchId = arguments!!.getLong("match_id")
        etMatchNote.setText(note)
        tvCharacterCounter.text = "${etMatchNote.text.length}/140" // TODO 140 should be 3rd argument in bundle taken from resources
        etMatchNote.addTextChangedListener(noteWatcher)

        btnSaveNote.setOnClickListener {
            (targetFragment as OnNoteSavedListener).onNoteSaved(etMatchNote.text.toString(), matchId)
            dismiss()
        }
    }

    interface OnNoteSavedListener {
        fun onNoteSaved(note: String?, matchId: Long)
    }
}