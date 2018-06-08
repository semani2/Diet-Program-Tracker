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
        initializeDagger()
    }

    private fun initializeDagger() {
        appComponent =DaggerAppComponent.builder()
                .applicationModule(ApplicationModule(this))
                .roomModule(RoomModule(this))
                .build()
    }

    private fun initializeStetho() {
        Stetho.initializeWithDefaults(this)
    }

    fun getApplicationComponent(): AppComponent {
        return appComponent
    }
}
