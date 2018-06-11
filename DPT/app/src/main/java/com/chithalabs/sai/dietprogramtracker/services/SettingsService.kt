package com.chithalabs.sai.dietprogramtracker.services

import android.content.Context
import android.content.SharedPreferences
import com.chithalabs.sai.dietprogramtracker.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class SettingsService @Inject constructor(private val context: Context){

    private val SP_NAME = "com.chithalabs.sai.buttery.settings"

    private val WATER_GOAL_KEY = "water_goal_key"
    private val FAT_GOAL_KEY = "fat_goal_key"
    private val LIME_GOAL_KEY = "lime_goal_key"
    private val MULTIVITAMIN_GOAL_KEY = "multivitamin_goal_key"
    private val WEIGHT_UNIT_GOAL_KEY = "weight_unit_goal_key"
    private val NAME_KEY = "name_key"
    private val WELCOME_KEY = "welcome_key"

    private val AD_KEY = "ad_key"

    fun getWaterGoal(): Long {
        return getShredPreferences().getLong(WATER_GOAL_KEY, DEFAULT_WATER_GOAL)
    }

    fun getFatGoal(): Long {
        return getShredPreferences().getLong(FAT_GOAL_KEY, DEFAULT_FAT_GOAL)
    }

    fun getLimeGoal(): Int {
        return getShredPreferences().getInt(LIME_GOAL_KEY, DEFAULT_LIME_GOAL)
    }

    fun getMultiVitaminGoal(): Int {
        return getShredPreferences().getInt(MULTIVITAMIN_GOAL_KEY, DEFAULT_MULTIVITAMIN_GOAL)
    }

    fun getWeightUnit(): String {
        return getShredPreferences().getString(WEIGHT_UNIT_GOAL_KEY, UNIT_KGS)
    }

    fun shouldShowAd(): Boolean {
        return getShredPreferences().getInt(AD_KEY, 1) % 3 == 0
    }

    fun resetAdCounter() {
        with (getShredPreferences().edit()) {
            putInt(AD_KEY, 0)
            apply()
        }
    }

    fun incrementAdCounter() {
        val counter = getShredPreferences().getInt(AD_KEY, 0)
        with (getShredPreferences().edit()) {
            putInt(AD_KEY, counter + 1)
            apply()
        }

    }

    fun saveName(name: String) {
        with (getShredPreferences().edit()) {
            putString(NAME_KEY, name)
            apply()
        }
    }

    fun getName(): String {
        return getShredPreferences().getString(NAME_KEY, "")
    }

    fun shouldShowWelcome(): Boolean {
        return getShredPreferences().getBoolean(WELCOME_KEY, true)
    }

    fun welcomeDisplayedToUser() {
        with (getShredPreferences().edit()) {
            putBoolean(WELCOME_KEY, false)
            apply()
        }
    }

    fun saveSettings(waterGoal: Long, fatGoal: Long,
                     limeGoal: Int, multivitaminGoal: Int,
                     weight_unit: String, name: String) {
        with (getShredPreferences().edit()) {
            putLong(WATER_GOAL_KEY, waterGoal)
            putLong(FAT_GOAL_KEY, fatGoal)
            putInt(LIME_GOAL_KEY, limeGoal)
            putInt(MULTIVITAMIN_GOAL_KEY, multivitaminGoal)
            putString(WEIGHT_UNIT_GOAL_KEY, weight_unit)
            putString(NAME_KEY, name)
            apply()
        }
    }

    private fun getShredPreferences(): SharedPreferences {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }
}
