package com.practicum.playlist5.sharing.domain

import com.practicum.playlist5.sharing.domain.api.SharingRepository
import com.practicum.playlist5.sharing.domain.models.EmailData



class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val sharingRep: SharingRepository
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return sharingRep.getShareData()
    }

    private fun getSupportEmailData(): EmailData {
        return sharingRep.getMailData()
    }

    private fun getTermsLink(): String {
        return sharingRep.getTermsData()
    }
}