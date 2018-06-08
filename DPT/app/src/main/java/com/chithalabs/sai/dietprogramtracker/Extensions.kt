package com.chithalabs.sai.dietprogramtracker

import android.app.Activity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

val dptDateFormat: SimpleDateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
val dptTimeFormat: SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

fun Date.dptDate(): String {
    return dptDateFormat.format(this)
}

fun Date.dptTime(): String {
    return dptTimeFormat.format(this)
}
/* Extensions for Views */
fun View.visible(): Unit {
    this.visibility = View.VISIBLE
}

fun View.gone(): Unit {
    this.visibility = View.GONE
}

fun EditText.isEmpty(): Boolean = this.text.isEmpty()
fun EditText.toString(): String = this.text.toString().trim()
fun EditText.isValidNumber(): Boolean {
    val v = this.text.toString().toIntOrNull()
    return when (v) {
        null -> false
        else -> true
    }
}


/* Extensions on Activity */
fun Activity.showToast(msg: String): Unit {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
