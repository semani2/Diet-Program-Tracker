package com.chithalabs.sai.dietprogramtracker

import java.text.SimpleDateFormat
import java.util.*

val dptDateFormat: SimpleDateFormat = SimpleDateFormat("mm-dd-yyyy", Locale.getDefault())

fun Date.dptDate(): String? {
    return dptDateFormat.format(this)
}
