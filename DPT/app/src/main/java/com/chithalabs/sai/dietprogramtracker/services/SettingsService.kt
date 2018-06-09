package com.chithalabs.sai.dietprogramtracker.services

import android.content.Context
import android.content.SharedPreferences
import com.chithalabs.sai.dietprogramtracker.DEFAULT_FAT_GOAL
import com.chithalabs.sai.dietprogramtracker.DEFAULT_LIME_GOAL
import com.chithalabs.sai.dietprogramtracker.DEFAULT_MULTIVITAMIN_GOAL
import com.chithalabs.sai.dietprogramtracker.DEFAULT_WATER_GOAL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class SettingsService @Inject constructor(private val context: Context){

    private val SP_NAME = "com.chithalabs.sai.buttery.settings"

    private val WATER_GOAL_KEY = "water_goal_key"
    private val FAT_GOAL_KEY = "fat_goal_key"
    private val LIME_GOAL_KEY = "lime_goal_key"
    private val MULTIVITAMIN_GOAL_KEY = "multivitamin_goal_key"

    fun getWaterGoal(): String {
        return String.format("%d", getShredPreferences().getLong(WATER_GOAL_KEY, DEFAULT_WATER_GOAL))
    }

    fun getFatGoal(): String {
        return String.format("%d", getShredPreferences().getLong(FAT_GOAL_KEY, DEFAULT_FAT_GOAL))
    }

    fun getLimeGoal(): Int {
        return getShredPreferences().getInt(LIME_GOAL_KEY, DEFAULT_LIME_GOAL)
    }

    fun getMultiVitaminGoal(): Int {
        return getShredPreferences().getInt(MULTIVITAMIN_GOAL_KEY, DEFAULT_MULTIVITAMIN_GOAL)
    }

    fun saveSettings(waterGoal: Long, fatGoal: Long,
                     limeGoal: Int, multivitaminGoal: Int) {
        with (getShredPreferences().edit()) {
            putLong(WATER_GOAL_KEY, waterGoal)
            putLong(FAT_GOAL_KEY, fatGoal)
            putInt(LIME_GOAL_KEY, limeGoal)
            putInt(MULTIVITAMIN_GOAL_KEY, multivitaminGoal)
            apply()
        }
    }

    private fun getShredPreferences(): SharedPreferences {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }
}
