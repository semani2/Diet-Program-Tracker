package com.chithalabs.sai.dietprogramtracker.home

import android.annotation.SuppressLint
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
import com.chithalabs.sai.dietprogramtracker.log_details.LogDetailsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.*


class HomeActivity : AppCompatActivity() {

    private val TAG: String = HomeActivity::class.java.simpleName

    private val listOfLogs: MutableList<Log> = mutableListOf()

    @Inject
    lateinit var viewmodelFactory: ViewModelProvider.Factory

    private lateinit var viewmodel: LogCollectionViewModel
    private lateinit var adapter: LogAdapter

    private var mDate = Date().dptDate()

    val datePickerListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        loadLogs(String.format("%s-%s-%s", DECIMAL_FORMAT.format(dayOfMonth), DECIMAL_FORMAT.format(monthOfYear + 1), DECIMAL_FORMAT.format(year)))
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        (application as DPTApplication).getApplicationComponent().inject(this)

        action_meal.setOnClickListener({ goToAddLog(FOOD) })
        action_liquid.setOnClickListener({ goToAddLog(LIQUID) })
        action_fat.setOnClickListener({ goToAddLog(FAT) })
        action_water.setOnClickListener({ goToAddLog(WATER) })
        action_lime.setOnClickListener({ goToAddLog(LIME) })
        action_multi_vitamin.setOnClickListener({ goToAddLog(MULTIVITAMINS) })

        if (savedInstanceState != null && savedInstanceState.containsKey(PARAM_DATE)) {
            mDate = savedInstanceState.getString(PARAM_DATE)
        }

        viewmodel = ViewModelProviders.of(this, viewmodelFactory)
                .get(LogCollectionViewModel::class.java)

        initRecyclerView()

        adapter.onItemClickEvent().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ log -> goToLogDetails(mDate, log.type) })

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
            R.id.menu_send_feedback -> {
                // TODO:: Not implemented
                true
            }
            R.id.menu_clear_all_logs -> {
                // TODO:: Not implemented
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        adapter = LogAdapter(listOfLogs)
        log_recycler_view.adapter = adapter
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
            listOfLogs.addAll(it.toList())

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

        startActivity(intent)
    }

    private fun goToLogDetails(date:String, @LogType logType: String) {
        val intent = Intent(this, LogDetailsActivity::class.java)
        intent.putExtra(PARAM_LOG_TYPE, logType)
        intent.putExtra(PARAM_DATE, date)

        startActivity(intent)
    }
}
