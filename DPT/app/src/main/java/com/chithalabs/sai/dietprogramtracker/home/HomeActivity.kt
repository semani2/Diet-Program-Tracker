package com.chithalabs.sai.dietprogramtracker.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chithalabs.sai.dietprogramtracker.*
import com.chithalabs.sai.dietprogramtracker.adapters.LogAdapter
import com.chithalabs.sai.dietprogramtracker.add_log.AddLogActivity
import com.chithalabs.sai.dietprogramtracker.data.room.Log
import com.chithalabs.sai.dietprogramtracker.di.DPTApplication
import com.chithalabs.sai.dietprogramtracker.viewmodel.LogCollectionViewModel
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    private val TAG: String = HomeActivity::class.java.simpleName

    private val listOfLogs: MutableList<Log> = mutableListOf()

    @Inject
    lateinit var viewmodelFactory: ViewModelProvider.Factory

    private lateinit var viewmodel: LogCollectionViewModel
    private lateinit var adapter: LogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        (application as DPTApplication).getApplicationComponent().inject(this)

        viewmodel = ViewModelProviders.of(this, viewmodelFactory)
                .get(LogCollectionViewModel::class.java)

        viewmodel.getLogsForToday().observe(this, Observer {
            list -> setListData(list)
        })

        initRecyclerView()

        action_meal.setOnClickListener({ goToAddLog(FOOD) })
        action_liquid.setOnClickListener({ goToAddLog(LIQUID) })
        action_fat.setOnClickListener({ goToAddLog(FAT) })
        action_water.setOnClickListener({ goToAddLog(WATER) })
        action_lime.setOnClickListener({ goToAddLog(LIME) })
        action_multi_vitamin.setOnClickListener({ goToAddLog(MULTIVITAMINS) })
    }

    private fun initRecyclerView() {
        adapter = LogAdapter(listOfLogs)
        log_recycler_view.adapter = adapter
    }

    private fun setListData(list: List<Log>?) {
        android.util.Log.d(TAG, "Log list observer called")

        list?.let {
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
    }

    private fun goToAddLog(@LogType logType: String) {
        add_fab.collapse()

        val intent = Intent(this, AddLogActivity::class.java)
        intent.putExtra(PARAM_LOG_TYPE, logType)

        startActivity(intent)
    }
}
