package com.practicum.playlist5

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val arrow = findViewById<Button>(R.id.light_mode)
        arrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val switch = findViewById<Switch>(R.id.switchBtn)
        switch.setOnClickListener {
            if (switch.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            else {
                AppCompatDelegate.setDefaultNightMode (AppCompatDelegate.MODE_NIGHT_NO)
            }


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