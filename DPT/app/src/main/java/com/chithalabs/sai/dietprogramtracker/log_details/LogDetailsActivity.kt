package com.chithalabs.sai.dietprogramtracker.log_details

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chithalabs.sai.dietprogramtracker.*
import com.chithalabs.sai.dietprogramtracker.adapters.LogAdapter
import com.chithalabs.sai.dietprogramtracker.data.room.Log
import com.chithalabs.sai.dietprogramtracker.di.DPTApplication
import com.chithalabs.sai.dietprogramtracker.viewmodel.LogDetailsViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_log_details.*
import java.util.*
import javax.inject.Inject

class LogDetailsActivity : AppCompatActivity() {

    private val TAG = LogDetailsActivity::class.java.simpleName

    private val listOfLogs: MutableList<Log> = mutableListOf()

    @Inject
    lateinit var viewmodelFactory: ViewModelProvider.Factory

    private lateinit var viewmodel: LogDetailsViewModel
    private lateinit var adapter: LogAdapter

    private lateinit var mDate: String
    private lateinit var mLogType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_details)

        (application as DPTApplication).getApplicationComponent().inject(this)

        if (intent != null && intent.hasExtra(PARAM_DATE) && intent.hasExtra(PARAM_LOG_TYPE)) {
            mDate = intent.getStringExtra(PARAM_DATE)
            mLogType = intent.getStringExtra(PARAM_LOG_TYPE)
        } else {
            android.util.Log.e(TAG, "No intent data, finishing activity")
            finish()
            return
        }

        viewmodel = ViewModelProviders.of(this, viewmodelFactory)
                .get(LogDetailsViewModel::class.java)

        log_details_image_view.setImageDrawable(when (mLogType) {
            FOOD -> getDrawable(R.drawable.meal)
            LIQUID -> getDrawable(R.drawable.soup)
            WATER -> getDrawable(R.drawable.water)
            FAT -> getDrawable(R.drawable.olive_oil)
            MULTIVITAMINS -> getDrawable(R.drawable.proteins)
            LIME -> getDrawable(R.drawable.lime)
            else -> getDrawable(R.drawable.ic_buttery_round)
        })

        title = mLogType.capitalize() + " Progress on " + mDate

        initRecyclerView()

        loadLogs(mDate, mLogType)
    }

    private fun loadLogs(date: String, type: String) {
        viewmodel.getLogsForDateAndType(date, type)
                .observe(this, Observer { list -> setData(list) })
    }

    private fun setData(list: List<Log>?) {
        setProgressData(list)
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

            log_details_recycler_view.layoutManager = LinearLayoutManager(this)
            toggleListVisibility(true)
        }
    }

    private fun setProgressData(list: List<Log>?) {
        when (mLogType) {
            FOOD -> {
                progress_layout.visibility = View.GONE
            }
            LIQUID -> {
                progress_layout.visibility = View.GONE
            }
            WATER -> {
                progress_layout.visibility = View.VISIBLE
                val totalQuantity = "4000 ml"
                var progress = 0f

                list?.let {
                    for (log in it) {
                        progress += log.quantity!!
                    }
                }
                progress_text_view.text = String.format("%.2f ml / %s", progress, totalQuantity)
            }
            FAT -> {
                progress_layout.visibility = View.VISIBLE
                val totalQuantity = "70 mg"
                var progress = 0f

                list?.let {
                    for (log in it) {
                        progress += log.quantity!!
                    }
                }
                progress_text_view.text = String.format("%.2f mg / %s", progress, totalQuantity)
            }
            LIME -> {
                progress_layout.visibility = View.VISIBLE
                val totalQuantity = "4"
                var progress = 0f

                list?.let {
                    for (log in it) {
                        progress += log.quantity!!
                    }
                }
                progress_text_view.text = String.format("%.2f / %s", progress, totalQuantity)
            }
            MULTIVITAMINS -> {
                progress_layout.visibility = View.VISIBLE
                val totalQuantity = "1"
                var progress = 0

                list?.let {
                    for (log in it) {
                        progress += log.quantity!!.toInt()
                    }
                }
                progress_text_view.text = String.format("%d / %s", progress, totalQuantity)
            }
        }
    }

    private fun toggleListVisibility(listVisible: Boolean) {
        empty_logs_layout_details.visibility = if (listVisible) View.GONE else View.VISIBLE
        log_details_recycler_view.visibility = if (listVisible) View.VISIBLE else View.GONE

        if (!listVisible) {
            empty_log__details_text_view.text = getString(R.string.home_no_logs, mDate)
        }
    }

    private fun initRecyclerView() {
        adapter = LogAdapter(listOfLogs)
        log_details_recycler_view.adapter = adapter
    }
}
