package com.chithalabs.sai.dietprogramtracker.home

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.chithalabs.sai.dietprogramtracker.*
import com.chithalabs.sai.dietprogramtracker.add_log.AddLogActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val TAG: String = HomeActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        action_meal.setOnClickListener({ goToAddLog(FOOD) })
        action_liquid.setOnClickListener({ goToAddLog(LIQUID) })
        action_fat.setOnClickListener({ goToAddLog(FAT) })
        action_water.setOnClickListener({ goToAddLog(WATER) })
        action_lime.setOnClickListener({ goToAddLog(LIME) })
        action_multi_vitamin.setOnClickListener({ goToAddLog(MULTIVITAMINS) })
    }

    private fun goToAddLog(@LogType logType: String) {
        val intent = Intent(this, AddLogActivity::class.java)
        intent.putExtra(PARAM_LOG_TYPE, logType)

        startActivity(intent)
    }
}
