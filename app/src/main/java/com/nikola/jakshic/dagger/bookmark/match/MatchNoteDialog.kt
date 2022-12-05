package com.nikola.jakshic.dagger.bookmark.match

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.databinding.DialogNoteMatchBinding

private const val EXTRA_NOTE = "note"
private const val EXTRA_MATCH_ID = "match-id"

class MatchNoteDialog : DialogFragment(R.layout.dialog_note_match) {
    private var _binding: DialogNoteMatchBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(note: String, matchId: Long): MatchNoteDialog {
            val bundle = Bundle().apply {
                putString(EXTRA_NOTE, note)
                putLong(EXTRA_MATCH_ID, matchId)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DialogNoteMatchBinding.bind(view)

        val note = requireArguments().getString(EXTRA_NOTE)
        val matchId = requireArguments().getLong(EXTRA_MATCH_ID)
        val characterLimit = resources.getInteger(R.integer.match_note_character_limit)
        binding.etMatchNote.setText(note)
        binding.tvCharacterCounter.text = "${binding.etMatchNote.text.length}/$characterLimit"
        binding.etMatchNote.doOnTextChanged { text, start, before, count ->
            val totalCharacters = text?.length ?: 0
            binding.tvCharacterCounter.text = "$totalCharacters/$characterLimit"
        }
        binding.btnSaveNote.setOnClickListener {
            (targetFragment as OnNoteSavedListener).onNoteSaved(
                binding.etMatchNote.text.toString(),
                matchId
            )
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnNoteSavedListener {
        fun onNoteSaved(note: String?, matchId: Long)
    }
}
