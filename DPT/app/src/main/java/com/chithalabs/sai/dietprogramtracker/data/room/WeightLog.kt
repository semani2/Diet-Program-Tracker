package com.chithalabs.sai.dietprogramtracker.data.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class WeightLog(
        @PrimaryKey(autoGenerate = true) val id: Long,
        var date: String,
        var weightInKgs: Float,
        var weightInLbs: Float
)
