package com.chithalabs.sai.dietprogramtracker.data.repository

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.chithalabs.sai.dietprogramtracker.data.room.Log
import com.chithalabs.sai.dietprogramtracker.data.room.LogDao
import com.chithalabs.sai.dietprogramtracker.data.room.WeightLog
import com.chithalabs.sai.dietprogramtracker.data.room.WeightLogDao
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class DPTRepository
@Inject constructor(private val logDao: LogDao,
                    private val weightLogDao: WeightLogDao): IDPTRepository{

    private val TAG = DPTRepository::class.java.simpleName

    @SuppressLint("CheckResult")
    override fun getAllLogs(date: String): LiveData<List<Log>> {
        val mutableLiveData = MutableLiveData<List<Log>>()
        logDao.getAllLogs(date).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ logList -> mutableLiveData.value = logList}, {
                    t: Throwable? -> android.util.Log.e(TAG, "Error getting logs for datePickerListener", t)
                })

        return mutableLiveData
    }

    @SuppressLint("CheckResult")
    override fun getLog(id: Long): LiveData<Log> {
        val mutableLiveData = MutableLiveData<Log>()
        logDao.getLog(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ log -> mutableLiveData.value = log}, {
                    t: Throwable? -> android.util.Log.e(TAG, "Error getting logs for datePickerListener", t)
                })

        return mutableLiveData
    }

    @SuppressLint("CheckResult")
    override fun addLog(log: Log) {
       Completable.fromAction({logDao.insertLog(log)})
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe({ android.util.Log.d(TAG, "New log inserted!") })
    }

    @SuppressLint("CheckResult")
    override fun deleteLog(log: Log) {
        Completable.fromAction({logDao.deleteLog(log)})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({android.util.Log.d(TAG, "Log deleted successfully!")})
    }

    @SuppressLint("CheckResult")
    override fun deleteAllLogs() {
        Completable.fromAction({
            logDao.deleteAllLogs()
            logDao.deleteAllWeightLogs()
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({android.util.Log.d(TAG, "Log deleted successfully!")})
    }

    @SuppressLint("CheckResult")
    override fun getAllLogs(date: String, logType: String): LiveData<List<Log>> {
        val mutableLiveData = MutableLiveData<List<Log>>()
        logDao.getAllLogs(date, logType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ logList -> mutableLiveData.value = logList}, {
                    t: Throwable? -> android.util.Log.e(TAG, "Error getting logs for datePickerListener", t)
                })

        return mutableLiveData
    }

    override fun getAllWeightLogs(): LiveData<List<WeightLog>> {
        val mutableLiveData = MutableLiveData<List<WeightLog>>()
        weightLogDao.getAllWeightLogs().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ logList -> mutableLiveData.value = logList}, {
                    t: Throwable? -> android.util.Log.e(TAG, "Error getting weight logs", t)
                })

        return mutableLiveData
    }

    @SuppressLint("CheckResult")
    override fun addWeightLog(weightLog: WeightLog) {
        Completable.fromAction({weightLogDao.insertLog(weightLog)})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ android.util.Log.d(TAG, "New weight log inserted")})
    }

    override fun deleteWeightLog(weightLog: WeightLog) {
        Completable.fromAction({weightLogDao.deleteLog(weightLog)})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({android.util.Log.d(TAG, "Weight Log deleted successfully!")})
    }
}
