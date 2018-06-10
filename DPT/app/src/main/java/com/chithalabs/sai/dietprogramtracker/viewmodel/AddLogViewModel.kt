package com.chithalabs.sai.dietprogramtracker.viewmodel

import android.arch.lifecycle.ViewModel
import com.chithalabs.sai.dietprogramtracker.data.repository.IDPTRepository
import com.chithalabs.sai.dietprogramtracker.data.room.Log
import com.chithalabs.sai.dietprogramtracker.data.room.WeightLog

class AddLogViewModel constructor(private val dptRepository: IDPTRepository): ViewModel(){

    fun addNewLog(log: Log) {
        dptRepository.addLog(log)
    }

    fun addNewWeightLog(weightLog: WeightLog) {
        dptRepository.addWeightLog(weightLog)
    }
}
