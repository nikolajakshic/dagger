package com.nikola.jakshic.dagger.matchstats.comparison

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.util.DotaUtil

class ComparisonDialog : BottomSheetDialogFragment() {

    interface ComparisonClickListener {
        fun onClick(playerIndex: Int)
    }

    companion object {
        fun newInstance(player1: Int, player2: Int, heroes: LongArray): ComparisonDialog {
            val dialog = ComparisonDialog()
            val args = Bundle()
            args.putInt("player1", player1)
            args.putInt("player2", player2)
            args.putLongArray("heroes", heroes)
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_comparison, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val player1 = requireArguments().getInt("player1", 0)
        val player2 = requireArguments().getInt("player2", 5)
        val heroes = requireArguments().getLongArray("heroes")!!

        val root = view as ViewGroup

        for (i in 0 until root.childCount) {
            val imgHero = root.getChildAt(i) as ImageView
            val heroId = heroes[i]
            imgHero.load(DotaUtil.getHero(requireContext(), heroId))
            if (player1 == i || player2 == i) {
                imgHero.alpha = 0.5F
                continue
            }
            imgHero.setOnClickListener {
                (targetFragment as ComparisonClickListener).onClick(i)
                dismiss()
            }
        }
    }
}