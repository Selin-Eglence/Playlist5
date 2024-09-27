package com.practicum.playlist5.settings.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.Button
import androidx.activity.viewModels
import com.practicum.playlist5.utils.Creator
import com.practicum.playlist5.R
import com.practicum.playlist5.databinding.ActivitySettingsBinding
import com.practicum.playlist5.settings.domain.api.SettingsInteractor
import com.practicum.playlist5.utils.APP


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.getViewModelFactory()
    }


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
            (application as APP).switchTheme(isChecked)
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