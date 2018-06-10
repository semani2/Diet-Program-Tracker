package com.chithalabs.sai.dietprogramtracker.data.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.chithalabs.sai.dietprogramtracker.dptDate
import com.chithalabs.sai.dietprogramtracker.dptTime
import java.util.*

@Entity
data class Log(
        @PrimaryKey(autoGenerate = true) val id: Long,
        var date: String = Date().dptDate(),
        var time: String = Date().dptTime(),
        var desc: String,
        var quantity: Float? = null,
        var unit: String? = null,
        var type: String
): ILog



