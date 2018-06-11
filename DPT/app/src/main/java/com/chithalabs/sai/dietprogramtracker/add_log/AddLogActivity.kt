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
import com.chithalabs.sai.dietprogramtracker.data.room.WeightLog
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

        actionBar?.setDisplayHomeAsUpEnabled(true)

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
            WEIGHT -> {
                add_log_what_text_view.gone()
                add_log_what_edit_text.gone()

                select_unit_layout.visible()
                add_log_how_text_view.visible()
                add_log_how_edit_text.visible()

                add_log_how_text_view.text = getString(R.string.add_log_weight_title)

                icon_image_view.setImageDrawable(resources.getDrawable(R.drawable.scale, null))
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

                com.chithalabs.sai.dietprogramtracker.data.room.Log(
                        id = 0,
                        desc = add_log_what_edit_text.text.toString().trim(),
                        type = mLogType
                )
            }
            LIQUID -> {
                if (add_log_what_edit_text.isEmpty()) {
                    add_log_what_edit_text.error = getString(R.string.add_log_food_error)
                    add_log_what_edit_text.requestFocus()
                    return
                }

                com.chithalabs.sai.dietprogramtracker.data.room.Log(
                        id = 0,
                        desc = add_log_what_edit_text.text.toString().trim(),
                        type = mLogType
                )
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

                com.chithalabs.sai.dietprogramtracker.data.room.Log(
                        id = 0,
                        desc = add_log_what_edit_text.text.toString().trim(),
                        quantity = add_log_how_edit_text.text.toString().trim().toFloatOrNull(),
                        unit = MG_UNIT,
                        type = mLogType
                )
            }
            WATER -> {
                if (!add_log_how_edit_text.isValidNumber()) {
                    add_log_how_edit_text.error = getString(R.string.add_log_number_quantity_error_ml)
                    add_log_how_edit_text.requestFocus()
                    return
                }

                com.chithalabs.sai.dietprogramtracker.data.room.Log(
                        id = 0,
                        desc = WATER,
                        quantity = add_log_how_edit_text.text.toString().trim().toFloatOrNull(),
                        unit = ML_UNIT,
                        type = mLogType
                )
            }
            LIME -> {
                if (!add_log_how_edit_text.isValidNumber()) {
                    add_log_how_edit_text.error = getString(R.string.add_log_number_quantity_error)
                    add_log_how_edit_text.requestFocus()
                    return
                }

                com.chithalabs.sai.dietprogramtracker.data.room.Log(
                        id = 0,
                        desc = LIME,
                        quantity = add_log_how_edit_text.text.toString().trim().toFloatOrNull(),
                        unit = UNIT,
                        type = mLogType
                )
            }
            MULTIVITAMINS -> {
                if (!add_log_how_edit_text.isValidNumber()) {
                    add_log_how_edit_text.error = getString(R.string.add_log_number_quantity_error)
                    add_log_how_edit_text.requestFocus()
                    return
                }

                com.chithalabs.sai.dietprogramtracker.data.room.Log(
                        id = 0,
                        desc = MULTIVITAMIN,
                        quantity = add_log_how_edit_text.text.toString().trim().toFloatOrNull(),
                        unit = UNIT,
                        type = mLogType
                )
            }

            WEIGHT -> {
                if (!add_log_how_edit_text.isValidWeight()) {
                    add_log_how_edit_text.error = getString(R.string.add_log_weight_invalid)
                    add_log_how_edit_text.requestFocus()
                    return
                }

                val enteredWeight = add_log_how_edit_text.text.toString().trim().toFloat()

                var convertedWeight = if (kg_radio_button.isChecked) {
                    enteredWeight.convertToPounds()
                } else {
                    enteredWeight.convertToKg()
                }

                val weightLog = WeightLog (
                        id = 0,
                        weightInKgs = if (kg_radio_button.isChecked) enteredWeight else convertedWeight,
                        weightInLbs = if (kg_radio_button.isChecked) convertedWeight else enteredWeight
                )
                addLogViewModel.addNewWeightLog(weightLog)
                showToast(getString(R.string.str_weight_log_added))
                null
            }
            else -> {
                null
            }
        }

        log?.let {
            addLogViewModel.addNewLog(it)
            showToast(getString(R.string.log_added))
        }
        finish()
    }
}
