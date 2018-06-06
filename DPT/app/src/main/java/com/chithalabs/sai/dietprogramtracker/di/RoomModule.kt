package com.chithalabs.sai.dietprogramtracker.di

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.room.Room
import com.chithalabs.sai.dietprogramtracker.data.repository.DPTRepository
import com.chithalabs.sai.dietprogramtracker.data.repository.IDPTRepository
import com.chithalabs.sai.dietprogramtracker.data.room.DPTDatabase
import com.chithalabs.sai.dietprogramtracker.data.room.LogDao
import com.chithalabs.sai.dietprogramtracker.data.room.WeightLogDao
import com.chithalabs.sai.dietprogramtracker.viewmodel.DPTViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module class RoomModule(application: Application) {

    private val dptDatabase: DPTDatabase = Room.databaseBuilder(
            application,
            DPTDatabase::class.java,
            "DPT.db"
    ).build()

    @Provides @Singleton fun provideDPTRepository(logDao: LogDao, weightLogDao: WeightLogDao): IDPTRepository {
        return DPTRepository(logDao, weightLogDao)
    }

    @Provides @Singleton fun provideLogDao(dptDatabase: DPTDatabase): LogDao {
        return dptDatabase.logDao()
    }

    @Provides @Singleton fun provideWeightLogDao(dptDatabase: DPTDatabase): WeightLogDao {
        return dptDatabase.weightLogDao()
    }

    @Provides @Singleton fun provideDPTDatabase(): DPTDatabase {
        return dptDatabase
    }

    @Provides @Singleton fun provideViewModelFactory(dptRepository: IDPTRepository): ViewModelProvider.Factory {
        return DPTViewModelFactory(dptRepository)
    }
}
