package com.chithalabs.sai.dietprogramtracker.data.repository

import android.arch.lifecycle.LiveData
import com.chithalabs.sai.dietprogramtracker.data.room.Log
import com.chithalabs.sai.dietprogramtracker.data.room.WeightLog

interface IDPTRepository {

    fun getAllLogs(date: String): LiveData<List<Log>>

    fun getLog(id: Long): LiveData<Log>

    fun addLog(log: Log)

    fun deleteLog(log: Log)

    fun deleteAllLogs()

    fun getAllLogs(date: String, logType: String): LiveData<List<Log>>

    fun getAllWeightLogs(): LiveData<List<WeightLog>>

    fun addWeightLog(weightLog: WeightLog)

    fun deleteWeightLog(weightLog: WeightLog)
}
