package com.practicum.playlist5.media

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlist5.R
import com.practicum.playlist5.databinding.FragmentMediaBinding

class MediaFragment: Fragment() {
    private var _binding: FragmentMediaBinding?= null
    private val binding get() = _binding!!

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter= MediaAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle,)

        tabMediator= TabLayoutMediator(binding.tabLayout,binding.viewPager) {
                tab,position ->
            when (position) {
                0 -> tab.text = getString(R.string.favourite_tracks)
                1 -> tab.text = getString(R.string.playlist)
            }
            for (i in 0 until binding.tabLayout.tabCount) {
                val tab = (binding.tabLayout.getTabAt(i)?.view as? ViewGroup)?.getChildAt(1) as? TextView
                tab?.setTypeface(null, Typeface.BOLD)}

        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
        _binding=null
    }

}