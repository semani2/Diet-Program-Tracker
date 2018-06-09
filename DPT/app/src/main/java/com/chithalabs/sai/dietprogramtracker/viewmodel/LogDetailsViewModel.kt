package com.chithalabs.sai.dietprogramtracker.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.chithalabs.sai.dietprogramtracker.data.repository.IDPTRepository
import com.chithalabs.sai.dietprogramtracker.data.room.Log

class LogDetailsViewModel(private val logRepository: IDPTRepository): ViewModel() {

    fun getLogsForDateAndType(date: String, logType: String): LiveData<List<Log>> {
        return logRepository.getAllLogs(date, logType)
    }
}
