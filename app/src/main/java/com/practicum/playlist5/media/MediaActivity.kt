package com.practicum.playlist5.media

import android.graphics.Typeface
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlist5.R
import com.practicum.playlist5.databinding.ActivityMediaBinding

class MediaActivity : AppCompatActivity() {

    private var _binding: ActivityMediaBinding?= null
    private val binding get() = _binding!!

    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        _binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter= MediaAdapter(
            supportFragmentManager,
            lifecycle)

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



        binding.lightMode.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

        override fun onDestroy() {
            super.onDestroy()
            tabMediator.detach()
            _binding=null
        }
}