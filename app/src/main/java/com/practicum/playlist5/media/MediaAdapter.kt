package com.practicum.playlist5.media

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import compackage.FavouriteFragment

class MediaAdapter(
    fragmentManager: FragmentManager,
    lifecycle: androidx.lifecycle.Lifecycle
) : FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
       return when(position) {
           0 -> FavouriteFragment.newInstance()
           else -> PlaylistFragment.newInstance()
       }
    }

}