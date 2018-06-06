package com.chithalabs.sai.dietprogramtracker.home

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.chithalabs.sai.dietprogramtracker.R

class HomeActivity : AppCompatActivity() {

    private val TAG: String = HomeActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}
