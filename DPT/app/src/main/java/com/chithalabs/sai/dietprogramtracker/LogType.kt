package com.chithalabs.sai.dietprogramtracker

import android.support.annotation.StringDef

const val FOOD = "food"
const val LIQUID = "liquid"
const val FAT = "fat"
const val WATER = "water"
const val LIME = "lime"
const val MULTIVITAMINS = "multi_vitamins"
const val WEIGHT = "weight"

@Retention(AnnotationRetention.SOURCE)
@StringDef(FOOD, LIQUID, FAT, WATER, LIME, MULTIVITAMINS, WEIGHT)
annotation class LogType


