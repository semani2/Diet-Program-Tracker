package com.chithalabs.sai.dietprogramtracker.di

import android.app.Application
import com.facebook.stetho.Stetho

class DPTApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initializeStetho()
    }

    private fun initializeStetho() {
        Stetho.initializeWithDefaults(this)
    }
}
