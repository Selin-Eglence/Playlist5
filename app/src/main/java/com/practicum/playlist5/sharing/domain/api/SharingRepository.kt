package com.practicum.playlist5.sharing.domain.api

import com.practicum.playlist5.sharing.domain.models.EmailData

interface SharingRepository {
    fun getShareData(): String

    fun getMailData(): EmailData

    fun getTermsData(): String

}