package com.chithalabs.sai.dietprogramtracker.di

import android.app.Application
import com.chithalabs.sai.dietprogramtracker.services.AnalyticsService
import com.chithalabs.sai.dietprogramtracker.services.SettingsService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module class ApplicationModule(private val dptApplication: DPTApplication) {

    @Provides @Singleton fun provideApplication() : Application {
        return dptApplication
    }

    @Provides @Singleton fun provideSettingsService(): SettingsService {
        return SettingsService(dptApplication)
    }

    @Provides @Singleton fun provideAnalyticsService(): AnalyticsService {
        return AnalyticsService(dptApplication)
    }
}
