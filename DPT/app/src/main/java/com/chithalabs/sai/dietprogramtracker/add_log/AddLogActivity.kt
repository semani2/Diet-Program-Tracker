package com.chithalabs.sai.dietprogramtracker.add_log

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.NonNull
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.chithalabs.sai.dietprogramtracker.*
import com.chithalabs.sai.dietprogramtracker.di.DPTApplication
import com.chithalabs.sai.dietprogramtracker.viewmodel.AddLogViewModel
import kotlinx.android.synthetic.main.activity_add_log.*
import java.util.*
import javax.inject.Inject

class AddLogActivity : AppCompatActivity() {

    @Inject lateinit var viewmodelFactory: ViewModelProvider.Factory

    private lateinit var addLogViewModel : AddLogViewModel

    private lateinit var mLogType: String

    private val MG_UNIT = "mg"
    private val ML_UNIT = "ml"
    private val WATER = "water"
    private val LIME = "lime"
    private val MULTIVITAMIN = "multivitamin"
    private val UNIT = "unit"

    @Suppress("PrivatePropertyName")
    private val TAG: String = AddLogActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_log)

        if (intent != null && intent.hasExtra(PARAM_LOG_TYPE)) {
            mLogType = intent.getStringExtra(PARAM_LOG_TYPE)
        }

        if (TextUtils.isEmpty(mLogType)) {
            Log.e(TAG, "No log type selected, finishing activity")
            finish()
            return
        }

        title = getString(R.string.add_new_log)

        (application as DPTApplication).getApplicationComponent().inject(this)

        addLogViewModel = ViewModelProviders.of(this, viewmodelFactory)
                .get(AddLogViewModel::class.java)

        setupUI(mLogType)
    }

    private fun setupUI(@NonNull @LogType mLogType: String) {
        when (mLogType) {
            FOOD -> {
                add_log_what_text_view.visible()
                add_log_what_edit_text.visible()
                add_log_what_edit_text.hint = getString(R.string.add_log_meal_hint)
                add_log_what_text_view.text = getString(R.string.add_log_what_did_u_eat)

                add_log_how_text_view.gone()
                add_log_how_edit_text.gone()

                icon_image_view.setImageDrawable(resources.getDrawable(R.drawable.meal, null))
            }
            LIQUID -> {
                add_log_what_text_view.visible()
                add_log_what_edit_text.visible()
                add_log_what_edit_text.hint = getString(R.string.add_log_liquid_hint)
                add_log_what_text_view.text = getString(R.string.add_log_what_did_u_drink)

                add_log_how_text_view.gone()
                add_log_how_edit_text.gone()

                icon_image_view.setImageDrawable(resources.getDrawable(R.drawable.soup, null))
            }
            FAT -> {
                add_log_what_text_view.visible()
                add_log_what_edit_text.visible()
                add_log_what_edit_text.hint = getString(R.string.add_log_fat_hint)
                add_log_what_text_view.text = getString(R.string.add_log_what_did_u_consume)

                add_log_how_text_view.visible()
                add_log_how_edit_text.visible()
                add_log_how_text_view.text = getString(R.string.add_log_how_much_did_u_consume)
                add_log_how_edit_text.hint = getString(R.string.add_log_fat_hint_quantity)

                icon_image_view.setImageDrawable(resources.getDrawable(R.drawable.olive_oil, null))
            }
            WATER -> {
                add_log_what_text_view.gone()
                add_log_what_edit_text.gone()

                add_log_how_text_view.visible()
                add_log_how_edit_text.visible()
                add_log_how_text_view.text = getString(R.string.add_log_how_much_did_u_drink)
                add_log_how_edit_text.hint = getString(R.string.add_log_water_hint)

                icon_image_view.setImageDrawable(resources.getDrawable(R.drawable.water, null))
            }
            LIME -> {
                add_log_what_text_view.gone()
                add_log_what_edit_text.gone()

                add_log_how_text_view.visible()
                add_log_how_edit_text.visible()
                add_log_how_text_view.text = getString(R.string.add_log_how_many_did_u_eat)
                add_log_how_edit_text.hint = getString(R.string.add_log_quantity_hint)

                icon_image_view.setImageDrawable(resources.getDrawable(R.drawable.lime, null))
            }
            MULTIVITAMINS -> {
                add_log_what_text_view.gone()
                add_log_what_edit_text.gone()

                add_log_how_text_view.visible()
                add_log_how_edit_text.visible()
                add_log_how_text_view.text = getString(R.string.add_log_how_many_did_u_take)
                add_log_how_edit_text.hint = getString(R.string.add_log_quantity_hint)

                icon_image_view.setImageDrawable(resources.getDrawable(R.drawable.proteins, null))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_add_log, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.menu_save_action -> {
                saveLog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveLog() {
        val log: com.chithalabs.sai.dietprogramtracker.data.room.Log? = when (mLogType) {
            FOOD -> {
                if (add_log_what_edit_text.isEmpty()) {
                    add_log_what_edit_text.error = getString(R.string.add_log_food_error)
                    add_log_what_edit_text.requestFocus()
                    return
                }

                com.chithalabs.sai.dietprogramtracker.data.room.Log(0,
                        Date().dptDate(),
                        add_log_what_edit_text.text.toString().trim(),
                        0f,
                        null,
                        mLogType)
            }
            LIQUID -> {
                if (add_log_what_edit_text.isEmpty()) {
                    add_log_what_edit_text.error = getString(R.string.add_log_food_error)
                    add_log_what_edit_text.requestFocus()
                    return
                }

                com.chithalabs.sai.dietprogramtracker.data.room.Log(0,
                        Date().dptDate(),
                        add_log_what_edit_text.text.toString().trim(),
                        0f,
                        null,
                        mLogType)
            }
            FAT -> {
                if (add_log_what_edit_text.isEmpty()) {
                    add_log_what_edit_text.error = getString(R.string.add_log_fat_error)
                    add_log_what_edit_text.requestFocus()
                    return
                }

                if (!add_log_how_edit_text.isValidNumber()) {
                    add_log_how_edit_text.error = getString(R.string.add_log_number_quantity_error)
                    add_log_how_edit_text.requestFocus()
                    return
                }

                com.chithalabs.sai.dietprogramtracker.data.room.Log(0,
                        Date().dptDate(),
                        add_log_what_edit_text.text.toString().trim(),
                        add_log_how_edit_text.text.toString().trim().toFloatOrNull(),
                        MG_UNIT,
                        mLogType)
            }
            WATER -> {
                if (!add_log_how_edit_text.isValidNumber()) {
                    add_log_how_edit_text.error = getString(R.string.add_log_number_quantity_error_ml)
                    add_log_how_edit_text.requestFocus()
                    return
                }

                com.chithalabs.sai.dietprogramtracker.data.room.Log(0,
                        Date().dptDate(),
                        WATER,
                        add_log_how_edit_text.text.toString().trim().toFloatOrNull(),
                        ML_UNIT,
                        mLogType)
            }
            LIME -> {
                if (!add_log_how_edit_text.isValidNumber()) {
                    add_log_how_edit_text.error = getString(R.string.add_log_number_quantity_error)
                    add_log_how_edit_text.requestFocus()
                    return
                }

                com.chithalabs.sai.dietprogramtracker.data.room.Log(0,
                        Date().dptDate(),
                        LIME,
                        add_log_how_edit_text.text.toString().trim().toFloatOrNull(),
                        UNIT,
                        mLogType)
            }
            MULTIVITAMINS -> {
                if (!add_log_how_edit_text.isValidNumber()) {
                    add_log_how_edit_text.error = getString(R.string.add_log_number_quantity_error)
                    add_log_how_edit_text.requestFocus()
                    return
                }

                com.chithalabs.sai.dietprogramtracker.data.room.Log(0,
                        Date().dptDate(),
                        MULTIVITAMIN,
                        add_log_how_edit_text.text.toString().trim().toFloatOrNull(),
                        UNIT,
                        mLogType)
            }
            else -> {
                null
            }
        }

        log?.let {
            addLogViewModel.addNewLog(it)
            showToast(getString(R.string.log_added))
            finish()
        }
    }
}