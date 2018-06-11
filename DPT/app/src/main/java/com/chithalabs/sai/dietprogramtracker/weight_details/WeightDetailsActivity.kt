package com.chithalabs.sai.dietprogramtracker.weight_details

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.Toast
import com.chithalabs.sai.dietprogramtracker.R
import com.chithalabs.sai.dietprogramtracker.UNIT_KGS
import com.chithalabs.sai.dietprogramtracker.adapters.LogAdapter
import com.chithalabs.sai.dietprogramtracker.data.room.ILog
import com.chithalabs.sai.dietprogramtracker.data.room.WeightLog
import com.chithalabs.sai.dietprogramtracker.di.DPTApplication
import com.chithalabs.sai.dietprogramtracker.services.SettingsService
import com.chithalabs.sai.dietprogramtracker.viewmodel.WeightDetailsViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_weight_details.*
import javax.inject.Inject

class WeightDetailsActivity : AppCompatActivity() {

    private val TAG = WeightDetailsActivity::class.java.simpleName

    private val listOfLogs: MutableList<ILog> = mutableListOf()

    @Inject
    lateinit var viewmodelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var settingsService: SettingsService

    private lateinit var viewmodel: WeightDetailsViewModel
    private lateinit var adapter: LogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight_details)

        (application as DPTApplication).getApplicationComponent().inject(this)

        viewmodel = ViewModelProviders.of(this, viewmodelFactory)
                .get(WeightDetailsViewModel::class.java)

        title = getString(R.string.weight_details_activity_title)

        initRecyclerView()

        loadLogs()
    }

    private fun loadLogs() {
        viewmodel.getWeightLogs()
                .observe(this, Observer { list -> setData(list) })
    }

    private fun setData(list: List<WeightLog>?) {
        setCurrentWeight(list)
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

            weight_log_recycler_view.layoutManager = LinearLayoutManager(this)
            toggleListVisibility(true)
        }
    }

    private fun setCurrentWeight(list: List<WeightLog>?) {
        if (list == null || list.isEmpty()) {
            weight_progress_layout.visibility = View.GONE
            return
        }

        list.let {
            weight_progress_layout.visibility = View.VISIBLE
            val unit = settingsService.getWeightUnit()
            weight_progress_text_view.text = String.format("%.2f %s", if (unit.contentEquals(UNIT_KGS)) list.reversed()[0].weightInKgs else list.reversed()[0].weightInLbs, unit)
        }
    }

    private fun toggleListVisibility(listVisible: Boolean) {
        empty_weight_logs_layout_details.visibility = if (listVisible) View.GONE else View.VISIBLE
        weight_log_recycler_view.visibility = if (listVisible) View.VISIBLE else View.GONE
    }

    private fun initRecyclerView() {
        adapter = LogAdapter(listOfLogs, settingsService)
        weight_log_recycler_view.adapter = adapter

        ItemTouchHelper(createHelperCallback()).attachToRecyclerView(weight_log_recycler_view)
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
                    viewmodel.deleteWeightLog(
                            listOfLogs[position] as WeightLog
                    )
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            listOfLogs.removeAt(position)
                            adapter.notifyItemRemoved(position)
                            Toast.makeText(this@WeightDetailsActivity, getString(R.string.str_log_deleted),
                                    Snackbar.LENGTH_LONG).show()
                        })
            }
        }
    }
}
