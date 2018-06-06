package com.chithalabs.sai.dietprogramtracker.data.repository

import android.arch.lifecycle.LiveData
import com.chithalabs.sai.dietprogramtracker.data.room.Log
import com.chithalabs.sai.dietprogramtracker.data.room.LogDao
import com.chithalabs.sai.dietprogramtracker.data.room.WeightLog
import com.chithalabs.sai.dietprogramtracker.data.room.WeightLogDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class DPTRepository
@Inject constructor(private val logDao: LogDao,
                    private val weightLogDao: WeightLogDao): IDPTRepository{

    override fun getAllLogs(date: String): LiveData<List<Log>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLog(id: Long): LiveData<Log> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addLog(log: Log) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteLog(log: Log) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllWeightLogs(date: String): LiveData<List<WeightLog>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addWeightLog(weightLog: WeightLog) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteWeightLog(weightLog: WeightLog) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
