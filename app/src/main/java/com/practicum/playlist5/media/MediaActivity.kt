package com.practicum.playlist5.media

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlist5.R
import com.practicum.playlist5.databinding.ActivityMediaBinding
import com.practicum.playlist5.databinding.ActivitySettingsBinding

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.lightMode.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


    }
}