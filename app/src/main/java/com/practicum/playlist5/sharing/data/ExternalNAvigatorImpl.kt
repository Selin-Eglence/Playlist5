package com.practicum.playlist5.sharing.data

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.practicum.playlist5.R
import com.practicum.playlist5.sharing.domain.ExternalNavigator
import com.practicum.playlist5.sharing.domain.models.EmailData

class ExternalNavigatorImpl(val context:Context): ExternalNavigator {
    override fun shareLink(shareText: String) {
        val shareintent = Intent(Intent.ACTION_SEND)
        shareintent.putExtra(Intent.EXTRA_TEXT, shareText)
        shareintent.setType("text/plain")
        shareintent.setFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareintent)
    }

    override fun openLink(termsLink: String) {
        val agreeIntent=Intent(Intent.ACTION_VIEW)
        agreeIntent.data= Uri.parse(termsLink)
        agreeIntent.setFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(agreeIntent)
    }

    override fun openEmail(supportEmailData: EmailData) {
        val shareIntent= Intent().apply {
            action = Intent.ACTION_SENDTO
           data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmailData.email))
            putExtra(Intent.EXTRA_SUBJECT, supportEmailData.subject)
            putExtra(Intent.EXTRA_TEXT, supportEmailData.text)
        }
        shareIntent.setFlags(FLAG_ACTIVITY_NEW_TASK)
       context.startActivity(shareIntent)
    }


}