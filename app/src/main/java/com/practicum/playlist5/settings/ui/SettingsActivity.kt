package com.practicum.playlist5.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practicum.playlist5.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel by viewModel<SettingsViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.lightMode.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        viewModel.themeSettings.observe(this) {settings ->
            binding.switchBtn.isChecked = settings.darkTheme
        }



        binding.switchBtn.setOnCheckedChangeListener{_,
        isChecked ->
            viewModel.updateThemeSettings(isChecked)
        }

        binding.vector.setOnClickListener {
            viewModel.shareApp()
        }

        binding.support.setOnClickListener {
            viewModel.openSupport()
        }

        binding.arrowForward.setOnClickListener {
            viewModel.openTerms()
        }

    }}