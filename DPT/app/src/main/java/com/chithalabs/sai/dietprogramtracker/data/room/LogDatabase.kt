package com.chithalabs.sai.dietprogramtracker.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [(Log::class), (WeightLog::class)], version = 1)
abstract class LogDatabase: RoomDatabase() {

    abstract fun logDao(): LogDao

    abstract fun weightLogDao(): WeightLogDao
}
