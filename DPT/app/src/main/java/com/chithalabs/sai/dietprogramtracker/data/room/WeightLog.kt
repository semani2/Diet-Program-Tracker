package com.chithalabs.sai.dietprogramtracker.data.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.chithalabs.sai.dietprogramtracker.dptDate
import com.chithalabs.sai.dietprogramtracker.dptTime
import java.util.*

@Entity
data class WeightLog(
        @PrimaryKey(autoGenerate = true) val id: Long,
        var date: String = Date().dptDate(),
        var time: String = Date().dptTime(),
        var weightInKgs: Float,
        var weightInLbs: Float
): ILog
