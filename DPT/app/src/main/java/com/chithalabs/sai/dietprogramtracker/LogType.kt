package com.chithalabs.sai.dietprogramtracker

import android.support.annotation.StringDef

const val FOOD = "food"
const val LIQUID = "liquid"
const val FAT = "fat"
const val WATER = "water"
const val LIME = "lime"
const val MULTIVITAMINS = "multi_vitamins"

@Retention(AnnotationRetention.SOURCE)
@StringDef(FOOD, LIQUID, FAT, WATER, LIME, MULTIVITAMINS)
annotation class LogType


