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
        dialog?.window?.setLayout(
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
        val note = requireArguments().getString("note")
        val matchId = requireArguments().getLong("match_id")
        val characterLimit = resources.getInteger(R.integer.match_note_character_limit)
        etMatchNote.setText(note)
        tvCharacterCounter.text = "${etMatchNote.text.length}/$characterLimit"
        etMatchNote.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val totalCharacters = s?.length ?: 0
                tvCharacterCounter.text = "$totalCharacters/$characterLimit"
            }
        })

        btnSaveNote.setOnClickListener {
            (targetFragment as OnNoteSavedListener).onNoteSaved(etMatchNote.text.toString(), matchId)
            dismiss()
        }
    }

    interface OnNoteSavedListener {
        fun onNoteSaved(note: String?, matchId: Long)
    }
}