@file:Suppress("UNCHECKED_CAST")

package com.chithalabs.sai.dietprogramtracker.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.chithalabs.sai.dietprogramtracker.data.repository.IDPTRepository
import com.chithalabs.sai.dietprogramtracker.home.HomeActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class DPTViewModelFactory @Inject constructor(private val repository: IDPTRepository): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LogCollectionViewModel::class.java) -> LogCollectionViewModel(repository) as T
            modelClass.isAssignableFrom(AddLogViewModel::class.java) -> AddLogViewModel(repository) as T
            else -> throw Throwable("ViewModel not found for class" + modelClass.simpleName)
        }
    }
}
