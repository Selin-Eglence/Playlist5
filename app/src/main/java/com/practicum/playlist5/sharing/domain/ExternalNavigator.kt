package com.practicum.playlist5.sharing.domain

import com.practicum.playlist5.sharing.domain.models.EmailData

interface ExternalNavigator {
    fun shareLink(shareText: String)
    fun openLink(termsLink: String)
    fun openEmail(supportEmailData: EmailData)

}