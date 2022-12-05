package com.nikola.jakshic.dagger.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import coil.load
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.databinding.DialogMedalBinding

class MedalDialog : DialogFragment(R.layout.dialog_medal) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DialogMedalBinding.bind(view)

        binding.imgHerald.load(R.drawable.ic_rank_1)
        binding.imgGuardian.load(R.drawable.ic_rank_2)
        binding.imgCrusader.load(R.drawable.ic_rank_3)
        binding.imgArchon.load(R.drawable.ic_rank_4)
        binding.imgLegend.load(R.drawable.ic_rank_5)
        binding.imgAncient.load(R.drawable.ic_rank_6)
        binding.imgDivine.load(R.drawable.ic_rank_7)
        binding.imgImmortal.load(R.drawable.ic_rank_8)
        binding.imgImmortalTop100.load(R.drawable.ic_rank_7a)
        binding.imgImmortalTop10.load(R.drawable.ic_rank_7b)
    }
}
