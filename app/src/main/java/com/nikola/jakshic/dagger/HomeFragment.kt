package com.nikola.jakshic.dagger

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.nikola.jakshic.dagger.bookmark.BookmarkFragment
import com.nikola.jakshic.dagger.common.extensions.getFragmentByTag
import com.nikola.jakshic.dagger.competitive.CompetitiveFragment
import com.nikola.jakshic.dagger.databinding.FragmentHomeBinding
import com.nikola.jakshic.dagger.leaderboard.LeaderboardFragment
import com.nikola.jakshic.dagger.stream.StreamFragment

private const val TAG_COMPETITIVE = "competitive"
private const val TAG_LEADERBOARD = "leaderboard"
private const val TAG_BOOKMARK = "bookmark"
private const val TAG_STREAM = "stream"

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private lateinit var competitive: CompetitiveFragment
    private lateinit var leaderboard: LeaderboardFragment
    private lateinit var bookmark: BookmarkFragment
    private lateinit var stream: StreamFragment

    enum class Key { COMPETITIVE, LEADERBOARD, BOOKMARK, STREAM }

    companion object {
        fun setOnReselectListener(
            fragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            key: Key,
            listener: () -> Unit
        ) {
            fragmentManager.setFragmentResultListener(key.name, lifecycleOwner) { _, _ ->
                listener.invoke()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            competitive = CompetitiveFragment()
            leaderboard = LeaderboardFragment()
            bookmark = BookmarkFragment()
            stream = StreamFragment()

            childFragmentManager.beginTransaction()
                .add(R.id.fragment_container_view, competitive, TAG_COMPETITIVE)
                .add(R.id.fragment_container_view, leaderboard, TAG_LEADERBOARD)
                .add(R.id.fragment_container_view, bookmark, TAG_BOOKMARK)
                .add(R.id.fragment_container_view, stream, TAG_STREAM)
                .detach(leaderboard)
                .detach(bookmark)
                .detach(stream)
                .commit()
        } else {
            competitive = childFragmentManager.getFragmentByTag(TAG_COMPETITIVE)
            leaderboard = childFragmentManager.getFragmentByTag(TAG_LEADERBOARD)
            bookmark = childFragmentManager.getFragmentByTag(TAG_BOOKMARK)
            stream = childFragmentManager.getFragmentByTag(TAG_STREAM)
        }

        onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            binding.btmNavigation.selectedItemId = R.id.action_competitive
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        binding.btmNavigation.setOnItemSelectedListener {
            onBackPressedCallback.isEnabled = it.itemId != R.id.action_competitive
            if (it.itemId == R.id.action_competitive) selectFragment(competitive)
            if (it.itemId == R.id.action_leaderboard) selectFragment(leaderboard)
            if (it.itemId == R.id.action_bookmark) selectFragment(bookmark)
            if (it.itemId == R.id.action_stream) selectFragment(stream)
            return@setOnItemSelectedListener true
        }

        binding.btmNavigation.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.action_competitive -> {
                    childFragmentManager.setFragmentResult(Key.COMPETITIVE.name, bundleOf())
                }
                R.id.action_leaderboard -> {
                    childFragmentManager.setFragmentResult(Key.LEADERBOARD.name, bundleOf())
                }
                R.id.action_bookmark -> {
                    childFragmentManager.setFragmentResult(Key.BOOKMARK.name, bundleOf())
                }
                R.id.action_stream -> {
                    childFragmentManager.setFragmentResult(Key.STREAM.name, bundleOf())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // Get BottomNavigationView's selectedItemId up-to-date value
        onBackPressedCallback.isEnabled =
            binding.btmNavigation.selectedItemId != R.id.action_competitive
    }

    private fun selectFragment(selectedFragment: Fragment) {
        val fragments = arrayOf(competitive, leaderboard, bookmark, stream)
        val transaction = childFragmentManager.beginTransaction()
        fragments.forEach {
            if (selectedFragment == it) {
                transaction.attach(selectedFragment)
            } else {
                transaction.detach(it)
            }
        }
        transaction.commit()
    }
}
