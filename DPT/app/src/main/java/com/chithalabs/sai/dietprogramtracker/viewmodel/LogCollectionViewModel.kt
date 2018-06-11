package com.chithalabs.sai.dietprogramtracker.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.chithalabs.sai.dietprogramtracker.data.repository.IDPTRepository
import com.chithalabs.sai.dietprogramtracker.data.room.ILog
import com.chithalabs.sai.dietprogramtracker.data.room.Log
import com.chithalabs.sai.dietprogramtracker.data.room.WeightLog
import com.chithalabs.sai.dietprogramtracker.dptDate
import java.util.*

class LogCollectionViewModel(private val logRepository: IDPTRepository): ViewModel() {

    fun getLogsForToday(): LiveData<List<Log>> {
        return getLogsForDate(Date().dptDate())
    }

    fun getLogsForDate(date: String): LiveData<List<Log>> {
        return logRepository.getAllLogs(date)
    }

    fun deleteLogItem(log: ILog) {
        if (log is Log) {
            return logRepository.deleteLog(log)
        } else if (log is WeightLog){
            return logRepository.deleteWeightLog(log)
        }
    }

    fun deleteAllLogs() {
        logRepository.deleteAllLogs()
    }
}
