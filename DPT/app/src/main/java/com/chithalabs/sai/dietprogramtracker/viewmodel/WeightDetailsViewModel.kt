package com.chithalabs.sai.dietprogramtracker.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.chithalabs.sai.dietprogramtracker.data.repository.IDPTRepository
import com.chithalabs.sai.dietprogramtracker.data.room.WeightLog

class WeightDetailsViewModel(private val logRepository: IDPTRepository): ViewModel() {

    fun getWeightLogs(): LiveData<List<WeightLog>> {
        return logRepository.getAllWeightLogs()
    }

    fun deleteWeightLog(weightLog: WeightLog) {
        return logRepository.deleteWeightLog(weightLog)
    }
}
