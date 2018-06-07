package com.chithalabs.sai.dietprogramtracker.data.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Log(
        @PrimaryKey(autoGenerate = true) val id: Long,
        var date: String,
        var desc: String,
        var quantity: Float?,
        var unit: String?,
        var type: String
)



