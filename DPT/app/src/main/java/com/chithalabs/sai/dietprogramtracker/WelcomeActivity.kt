package com.chithalabs.sai.dietprogramtracker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.chithalabs.sai.dietprogramtracker.di.DPTApplication
import com.chithalabs.sai.dietprogramtracker.home.HomeActivity
import com.chithalabs.sai.dietprogramtracker.services.SettingsService
import kotlinx.android.synthetic.main.activity_welcome.*
import javax.inject.Inject

class WelcomeActivity : AppCompatActivity() {

    @Inject lateinit var mSettingsService: SettingsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        (application as DPTApplication).getApplicationComponent().inject(this)

        if (!mSettingsService.shouldShowWelcome()) {
            goHome()
            return
        }

        get_started_button.setOnClickListener {
            val name = name_edit_text.text.toString()

            if (TextUtils.isEmpty(name)) {
                name_edit_text.error = getString(R.string.str_please_enter_a_valid_name)
                return@setOnClickListener
            }

            mSettingsService.saveName(name)
            mSettingsService.welcomeDisplayedToUser()

            goHome()

            showToast(getString(R.string.str_welcome_name, name))
        }
    }

    private fun goHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
