package com.practicum.playlist5.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
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
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
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