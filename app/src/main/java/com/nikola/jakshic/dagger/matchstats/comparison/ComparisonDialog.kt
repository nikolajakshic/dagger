package com.nikola.jakshic.dagger.matchstats.comparison

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.databinding.DialogComparisonBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ComparisonDialog : BottomSheetDialogFragment(R.layout.dialog_comparison) {
    companion object {
        private const val KEY_RESULT = "result"
        private const val EXTRA_PLAYER_INDEX = "player-index"

        fun newInstance(args: ComparisonDialogArgs): ComparisonDialog {
            return ComparisonDialog().apply {
                arguments = args.toBundle()
            }
        }

        fun setOnClickListener(
            childFragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: (playerIndex: Int) -> Unit
        ) {
            childFragmentManager.setFragmentResultListener(
                KEY_RESULT,
                lifecycleOwner
            ) { _, result ->
                listener(result.getInt(EXTRA_PLAYER_INDEX))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = DialogComparisonBinding.bind(view)
        val viewModel = ViewModelProvider(this)[ComparisonViewModel::class.java]
        val args = ComparisonDialogArgs.fromBundle(requireArguments())

        for (i in 0 until binding.root.childCount) {
            val imgHero = binding.root[i] as ImageView

            if (args.leftPlayerIndex == i || args.rightPlayerIndex == i) {
                imgHero.alpha = 0.5F
                continue
            }
            imgHero.setOnClickListener {
                parentFragmentManager.setFragmentResult(
                    KEY_RESULT,
                    bundleOf(EXTRA_PLAYER_INDEX to i)
                )
                dismiss()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.heroes.collectLatest {
                    it.forEachIndexed { index, imagePath ->
                        (binding.root[index] as ImageView).load(imagePath)
                    }
                }
            }
        }
    }
}
