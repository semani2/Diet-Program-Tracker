package com.chithalabs.sai.dietprogramtracker.home

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.chithalabs.sai.dietprogramtracker.R
import com.chithalabs.sai.dietprogramtracker.R.id.add_log_fab
import com.chithalabs.sai.dietprogramtracker.add_log.AddLogActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val TAG: String = HomeActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    private fun goToAddLog(view: View) {
        val intent = Intent(this, AddLogActivity::class.java)
        startActivity(intent)
    }
}
