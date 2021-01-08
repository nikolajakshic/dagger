package com.nikola.jakshic.dagger.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import coil.load
import com.nikola.jakshic.dagger.R
import kotlinx.android.synthetic.main.dialog_medal.*

class MedalDialog : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_medal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imgHerald.load(R.drawable.ic_rank_1)
        imgGuardian.load(R.drawable.ic_rank_2)
        imgCrusader.load(R.drawable.ic_rank_3)
        imgArchon.load(R.drawable.ic_rank_4)
        imgLegend.load(R.drawable.ic_rank_5)
        imgAncient.load(R.drawable.ic_rank_6)
        imgDivine.load(R.drawable.ic_rank_7)
        imgImmortal.load(R.drawable.ic_rank_8)
        imgImmortalTop100.load(R.drawable.ic_rank_7a)
        imgImmortalTop10.load(R.drawable.ic_rank_7b)
    }
}