package com.chithalabs.sai.dietprogramtracker

import android.view.View
import java.text.SimpleDateFormat
import java.util.*

val dptDateFormat: SimpleDateFormat = SimpleDateFormat("mm-dd-yyyy", Locale.getDefault())

fun Date.dptDate(): String {
    return dptDateFormat.format(this)
}

fun View.visible(): Unit {
    this.visibility = View.VISIBLE
}

fun View.gone(): Unit {
    this.visibility = View.GONE
}
