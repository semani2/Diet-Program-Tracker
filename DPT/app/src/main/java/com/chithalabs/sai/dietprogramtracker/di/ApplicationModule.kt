package com.chithalabs.sai.dietprogramtracker.di

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module class ApplicationModule(private val dptApplication: DPTApplication) {

    @Provides @Singleton fun provideApplication() : Application {
        return dptApplication
    }
}
