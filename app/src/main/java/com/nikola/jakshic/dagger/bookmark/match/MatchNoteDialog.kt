package com.nikola.jakshic.dagger.bookmark.match

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.databinding.DialogNoteMatchBinding

class MatchNoteDialog : DialogFragment(R.layout.dialog_note_match) {
    companion object {
        private const val KEY_RESULT = "result"
        private const val EXTRA_MATCH_ID = "match-id"
        private const val EXTRA_NOTE = "note"

        fun newInstance(args: MatchNoteDialogArgs): MatchNoteDialog {
            return MatchNoteDialog().apply {
                arguments = args.toBundle()
            }
        }

        fun setOnNoteSavedListener(
            fragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: (matchId: Long, note: String?) -> Unit
        ) {
            fragmentManager.setFragmentResultListener(KEY_RESULT, lifecycleOwner) { key, result ->
                listener.invoke(result.getLong(EXTRA_MATCH_ID), result.getString(EXTRA_NOTE))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = DialogNoteMatchBinding.bind(view)

        requireDialog().window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        val args = MatchNoteDialogArgs.fromBundle(requireArguments())

        binding.etMatchNote.setText(args.note)
        val characterLimit = resources.getInteger(R.integer.match_note_character_limit)
        //noinspection SetTextI18n
        binding.tvCharacterCounter.text = "${binding.etMatchNote.text.length}/$characterLimit"
        binding.etMatchNote.doOnTextChanged { text, _, _, _ ->
            val totalCharacters = text?.length ?: 0
            //noinspection SetTextI18n
            binding.tvCharacterCounter.text = "$totalCharacters/$characterLimit"
        }
        binding.btnSaveNote.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                KEY_RESULT,
                bundleOf(
                    EXTRA_MATCH_ID to args.matchId,
                    EXTRA_NOTE to binding.etMatchNote.text.toString()
                )
            )
            dismiss()
        }
    }
}
