package com.chithalabs.sai.dietprogramtracker.di

import android.app.Application
import com.chithalabs.sai.dietprogramtracker.home.HomeActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [(ApplicationModule::class)])
@Singleton interface AppComponent {

    fun inject(activity: HomeActivity)

    fun application() : Application
}
