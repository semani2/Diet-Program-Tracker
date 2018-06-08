package com.chithalabs.sai.dietprogramtracker.di

import android.app.Application
import com.chithalabs.sai.dietprogramtracker.add_log.AddLogActivity
import com.chithalabs.sai.dietprogramtracker.home.HomeActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [(ApplicationModule::class), (RoomModule::class)])
@Singleton interface AppComponent {

    fun inject(activity: HomeActivity)

    fun inject(activity: AddLogActivity)

    fun application() : Application
}
