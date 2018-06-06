package com.chithalabs.sai.dietprogramtracker.data.room

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao interface WeightLogDao {

    /**
     * Gets all weight log data for date
     */
    @Query("SELECT * FROM WeightLog WHERE date = :date")
    fun getAllWeightLogs(date: String): Flowable<List<WeightLog>>

    /**
     * Inserts a new weight log item
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLog(log: WeightLog): Long

    /**
     * Deletes a weight log
     */
    @Delete
    fun deleteLog(log: WeightLog)
}
