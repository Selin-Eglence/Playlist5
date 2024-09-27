package com.practicum.playlist5.sharing.data

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlist5.R
import com.practicum.playlist5.sharing.domain.api.SharingRepository
import com.practicum.playlist5.sharing.domain.models.EmailData

class SharingRepositoryImpl(private val context: Context):SharingRepository {
    override fun getShareData():String {
        return context.getString(R.string.share_message)
    }

    override fun getMailData(): EmailData {
        return EmailData (
            context.getString(R.string.contact),
            context.getString(R.string.topic),
            context.getString(R.string.context),
        )


    }

    override fun getTermsData():String {
        return context.getString(R.string.agreement)
    }

}