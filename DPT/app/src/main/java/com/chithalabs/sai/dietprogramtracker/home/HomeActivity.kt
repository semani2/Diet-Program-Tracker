package com.chithalabs.sai.dietprogramtracker.home

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.chithalabs.sai.dietprogramtracker.*
import com.chithalabs.sai.dietprogramtracker.adapters.LogAdapter
import com.chithalabs.sai.dietprogramtracker.add_log.AddLogActivity
import com.chithalabs.sai.dietprogramtracker.data.room.Log
import com.chithalabs.sai.dietprogramtracker.di.DPTApplication
import com.chithalabs.sai.dietprogramtracker.viewmodel.LogCollectionViewModel
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.net.Uri
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.TextUtils
import android.view.ContextThemeWrapper
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.chithalabs.sai.dietprogramtracker.data.room.ILog
import com.chithalabs.sai.dietprogramtracker.log_details.LogDetailsActivity
import com.chithalabs.sai.dietprogramtracker.services.SettingsService
import com.chithalabs.sai.dietprogramtracker.weight_details.WeightDetailsActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


class HomeActivity : AppCompatActivity() {

    private val TAG: String = HomeActivity::class.java.simpleName

    private val listOfLogs: MutableList<ILog> = mutableListOf()

    @Inject
    lateinit var viewmodelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var settingsService: SettingsService

    private lateinit var viewmodel: LogCollectionViewModel
    private lateinit var adapter: LogAdapter

    private var mDate = Date().dptDate()

    private val datePickerListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        loadLogs(String.format("%s-%s-%s", DECIMAL_FORMAT.format(dayOfMonth), DECIMAL_FORMAT.format(monthOfYear + 1), DECIMAL_FORMAT.format(year)))
    }

    private lateinit var mFullPageAd: InterstitialAd

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        (application as DPTApplication).getApplicationComponent().inject(this)

        initAds()

        action_meal.setOnClickListener({ goToAddLog(FOOD) })
        action_fat.setOnClickListener({ goToAddLog(FAT) })
        action_water.setOnClickListener({ goToAddLog(WATER) })
        action_lime.setOnClickListener({ goToAddLog(LIME) })
        action_multi_vitamin.setOnClickListener({ goToAddLog(MULTIVITAMINS) })
        action_weight.setOnClickListener({ goToAddLog(WEIGHT)})

        if (savedInstanceState != null && savedInstanceState.containsKey(PARAM_DATE)) {
            mDate = savedInstanceState.getString(PARAM_DATE)
        }

        viewmodel = ViewModelProviders.of(this, viewmodelFactory)
                .get(LogCollectionViewModel::class.java)

        initRecyclerView()

        adapter.onItemClickEvent().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ log -> goToLogDetails(mDate, (log as? Log)?.type ?: WEIGHT) })

        loadLogs(mDate)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(PARAM_DATE, mDate)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.menu_select_day_action -> {
                showDatePicker()
                true
            }

            R.id.menu_log_weight -> {
                val intent = Intent(this, WeightDetailsActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.menu_settings -> {
                showSettingsDialog()
                true
            }

            R.id.menu_send_feedback -> {
                launchFeedbackIntent()
                true
            }
            R.id.menu_clear_all_logs -> {
                showDeleteAllDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun initAds() {
        MobileAds.initialize(this, ADMOB_APP_ID)
        mFullPageAd = InterstitialAd(this)
        mFullPageAd.adUnitId = BuildConfig.FULL_PAGE_AD
        val adBuilder = AdRequest.Builder()
        if (BuildConfig.DEBUG) adBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        mFullPageAd.loadAd(adBuilder.build())
    }

    private fun showDeleteAllDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.str_are_you_sure))
        builder.setCancelable(false)
        builder.setPositiveButton(android.R.string.ok, { dialog, _ ->
            viewmodel.deleteAllLogs()
            dialog.dismiss()
        })
        builder.setNegativeButton(android.R.string.cancel, { dialog, _ ->
            dialog.dismiss()
        })

        val dialog = builder.create()
        dialog.show()
    }

    private fun launchFeedbackIntent() {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(FEEDBACK_EMAIL))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, FEEDBACK_SUBJECT)

        try {
            startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            showToast(getString(R.string.str_sorry_no_mail))
        }
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, android.R.style.Theme_Material_Light_Dialog))

        val view = layoutInflater.inflate(R.layout.layout_settings_dialog, null)
        val savedWaterGoal = String.format("%d", settingsService.getWaterGoal())
        view.findViewById<EditText>(R.id.water_goal_edit_text).setText(savedWaterGoal)
        view.findViewById<EditText>(R.id.water_goal_edit_text).setSelection(savedWaterGoal.length)

        val savedFatGoal = String.format("%d", settingsService.getFatGoal())
        view.findViewById<EditText>(R.id.fat_goal_edit_text).setText(savedFatGoal)
        view.findViewById<EditText>(R.id.fat_goal_edit_text).setSelection(savedFatGoal.length)

        val savedLimeGoal = settingsService.getLimeGoal().toString()
        view.findViewById<EditText>(R.id.lime_goal_edit_text).setText(savedLimeGoal)
        view.findViewById<EditText>(R.id.lime_goal_edit_text).setSelection(savedLimeGoal.length)

        val savedMVGoal = settingsService.getMultiVitaminGoal().toString()
        view.findViewById<EditText>(R.id.multivitamin_goal_edit_text).setText(savedMVGoal)
        view.findViewById<EditText>(R.id.multivitamin_goal_edit_text).setSelection(savedMVGoal.length)

        val savedUnit = settingsService.getWeightUnit()
        if (savedUnit.contentEquals(UNIT_KGS)) {
            view.findViewById<RadioButton>(R.id.settings_kg_radio_button).isChecked = true
        } else {
            view.findViewById<RadioButton>(R.id.settings_lb_radio_button).isChecked = true
        }

        builder.setView(view)
                .setTitle(getString(R.string.settings))
                .setCancelable(false)
                .setIcon(R.drawable.ic_buttery_round)
                .setPositiveButton(R.string.settings_save, { dialog, _ ->
                    val waterGoal = view.findViewById<EditText>(R.id.water_goal_edit_text).text.toString()
                    val finalWaterGoal = if (TextUtils.isEmpty(waterGoal)) DEFAULT_WATER_GOAL else waterGoal.toLong()

                    val fatGoal = view.findViewById<EditText>(R.id.fat_goal_edit_text).text.toString()
                    val finalFatGoal = if (TextUtils.isEmpty(waterGoal)) DEFAULT_FAT_GOAL else fatGoal.toLong()

                    val limeGoal = view.findViewById<EditText>(R.id.lime_goal_edit_text).text.toString()
                    val finalLimeGoal = if (TextUtils.isEmpty(limeGoal)) DEFAULT_LIME_GOAL else limeGoal.toInt()

                    val mvGoal = view.findViewById<EditText>(R.id.multivitamin_goal_edit_text).text.toString()
                    val finalMVGoal = if (TextUtils.isEmpty(limeGoal)) DEFAULT_MULTIVITAMIN_GOAL else mvGoal.toInt()

                    val weightUnit = if (view.findViewById<RadioButton>(R.id.settings_kg_radio_button).isChecked) UNIT_KGS else UNIT_LBS

                    settingsService.saveSettings(finalWaterGoal, finalFatGoal, finalLimeGoal, finalMVGoal, weightUnit)

                    dialog.cancel()
                })
                .setNegativeButton(R.string.settings_cancel, { dialog, _ -> dialog.cancel() })
        val dialog = builder.create()
        dialog.show()
    }

    private fun showDatePicker() {
        val myCalendar = Calendar.getInstance()
        DatePickerDialog(this, datePickerListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()

    }

    private fun loadLogs(date: String) {
        mDate = date

        viewmodel.getLogsForDate(date)
                .observe(this, Observer { list -> setListData(list) })

        val isToday = date.contentEquals(Date().dptDate())

        title_text_view.text = if (isToday) getString(R.string.home_title_text, getString(R.string.str_today))
        else getString(R.string.home_title_text, date)

        add_fab.visibility = if (isToday) View.VISIBLE else View.GONE
    }

    private fun initRecyclerView() {
        adapter = LogAdapter(listOfLogs, settingsService)
        log_recycler_view.adapter = adapter

        ItemTouchHelper(createHelperCallback()).attachToRecyclerView(log_recycler_view)
    }

    private fun setListData(list: List<Log>?) {
        android.util.Log.d(TAG, "Log list observer called")

        if (list == null) {
            toggleListVisibility(false)
            return
        }

        list.let {
            if (it.isEmpty()) {
                toggleListVisibility(false)
                return
            }

            listOfLogs.clear()
            listOfLogs.addAll(it.toList().reversed())

            log_recycler_view.layoutManager = LinearLayoutManager(this)
            toggleListVisibility(true)
        }

    }

    private fun toggleListVisibility(listVisible: Boolean) {
        empty_logs_layout.visibility = if (listVisible) View.GONE else View.VISIBLE
        log_recycler_view.visibility = if (listVisible) View.VISIBLE else View.GONE
        title_text_view.visibility = if (listVisible) View.VISIBLE else View.GONE

        val isToday = mDate.contentEquals(Date().dptDate())
        if (!listVisible && !isToday) {
            empty_log_text_view.text = getString(R.string.home_no_logs, mDate)
        }
    }

    private fun goToAddLog(@LogType logType: String) {
        add_fab.collapse()

        val intent = Intent(this, AddLogActivity::class.java)
        intent.putExtra(PARAM_LOG_TYPE, logType)

        startActivityForResult(intent, ADD_LOG_REQUEST_CODE)
    }

    private fun goToLogDetails(date:String, @LogType logType: String) {
        if (logType.contentEquals(WEIGHT)) {
            startActivity(Intent(this, WeightDetailsActivity::class.java))
            return
        }

        val intent = Intent(this, LogDetailsActivity::class.java)
        intent.putExtra(PARAM_LOG_TYPE, logType)
        intent.putExtra(PARAM_DATE, date)

        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ADD_LOG_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (settingsService.shouldShowAd()) {
                        if (mFullPageAd.isLoaded) {
                            mFullPageAd.show()
                            settingsService.resetAdCounter()
                        } else android.util.Log.d(TAG, "Full page ad wasn't loaded")
                    } else {
                        settingsService.incrementAdCounter()
                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun createHelperCallback(): ItemTouchHelper.Callback {
        return object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            @SuppressLint("CheckResult")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                Completable.fromAction({
                    viewmodel.deleteLogItem(
                            listOfLogs[position]
                    )
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            listOfLogs.removeAt(position)
                            adapter.notifyItemRemoved(position)
                            Toast.makeText(this@HomeActivity, getString(R.string.str_log_deleted),
                                    Snackbar.LENGTH_LONG).show()
                        })
            }
        }
    }
}
