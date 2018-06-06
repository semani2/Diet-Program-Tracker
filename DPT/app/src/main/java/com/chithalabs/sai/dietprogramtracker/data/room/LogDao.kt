package com.chithalabs.sai.dietprogramtracker.data.room

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao interface LogDao {

    /**
     * Gets all log data for date
     */
    @Query("SELECT * FROM Log WHERE date = :date")
    fun getAllLogs(date: String): Flowable<List<Log>>

    /**
     * Inserts a new log item
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLog(log: Log): Long

    /**
     * Deletes a log
     */
    @Delete
    fun deleteLog(log: Log)
}
