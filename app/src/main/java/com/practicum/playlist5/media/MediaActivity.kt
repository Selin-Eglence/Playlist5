package com.practicum.playlist5.media

import android.os.Bundle
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
                0 -> tab.text = getString(R.string.favourites)
                1 -> tab.text = getString(R.string.empty_library)
            }
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