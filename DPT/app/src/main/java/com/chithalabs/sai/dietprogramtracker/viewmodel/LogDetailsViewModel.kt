package com.chithalabs.sai.dietprogramtracker.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.chithalabs.sai.dietprogramtracker.data.repository.IDPTRepository
import com.chithalabs.sai.dietprogramtracker.data.room.ILog
import com.chithalabs.sai.dietprogramtracker.data.room.Log
import com.chithalabs.sai.dietprogramtracker.data.room.WeightLog

class LogDetailsViewModel(private val logRepository: IDPTRepository): ViewModel() {

    fun getLogsForDateAndType(date: String, logType: String): LiveData<List<Log>> {
        return logRepository.getAllLogs(date, logType)
    }

    fun deleteLogItem(log: ILog) {
        if (log is Log) {
            return logRepository.deleteLog(log)
        } else if (log is WeightLog){
            return logRepository.deleteWeightLog(log)
        }
    }
}
