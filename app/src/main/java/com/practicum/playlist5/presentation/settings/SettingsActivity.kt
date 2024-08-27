package com.practicum.playlist5.presentation.settings

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.Button
import com.practicum.playlist5.APP
import com.practicum.playlist5.PLAYLIST_MAKER_PREFERENCES
import com.practicum.playlist5.R
import com.practicum.playlist5.THEME_KEY


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val arrow = findViewById<Button>(R.id.light_mode)
        arrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val switch = findViewById<Switch>(R.id.switchBtn)
        switch.isChecked = (applicationContext as APP).darkTheme
        val sharedPreferences = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        switch.setOnCheckedChangeListener { _, checked ->
            (applicationContext as APP).switchTheme(checked)
            sharedPreferences.edit()
                .putBoolean(THEME_KEY,checked)
                .apply()
        }


        val share = findViewById<Button>(R.id.vector)
        share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message))
            shareIntent.type = "text/plain"
            startActivity(shareIntent)
        }



        val support = findViewById<Button>(R.id.support)
        support.setOnClickListener{
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.contact)))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.context))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.topic))
            startActivity(supportIntent)
        }

        val agreement = findViewById<Button>(R.id.arrow_forward)
        agreement.setOnClickListener {
            val agreeIntent=Intent(Intent.ACTION_VIEW)
            agreeIntent.data= Uri.parse(getString(R.string.agreement))
            startActivity(agreeIntent)
        }

    }}