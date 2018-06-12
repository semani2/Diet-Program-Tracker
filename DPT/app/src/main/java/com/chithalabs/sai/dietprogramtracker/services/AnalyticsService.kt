package com.chithalabs.sai.dietprogramtracker.services

import android.content.Context
import android.os.Bundle
import com.chithalabs.sai.dietprogramtracker.LogType
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class AnalyticsService @Inject constructor(private val mContext: Context) {

    private fun getFirebaseAnalytis(): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(mContext)
    }

    fun logEventLogAdded(@LogType logType: String) {
        val bundle = Bundle()
        bundle.putString("log_type", logType)

        getFirebaseAnalytis().logEvent("log_added", bundle)
    }

    fun logEventLogDeleted() {
        getFirebaseAnalytis().logEvent("log_deleted", null)
    }

    fun logEventSignUp() {
        getFirebaseAnalytis().logEvent(FirebaseAnalytics.Event.SIGN_UP, null)
    }

    fun logEventSettingsChanged() {
        getFirebaseAnalytis().logEvent("settings_changed", null)
    }

    fun logEventFAQOpened() {
        getFirebaseAnalytis().logEvent("faq_opened", null)
    }

    fun logEventMyWeightOpened() {
        getFirebaseAnalytis().logEvent("my_weight_openend", null)
    }

    fun logEventClearAllLogs() {
        getFirebaseAnalytis().logEvent("logs_cleared", null)
    }
}
