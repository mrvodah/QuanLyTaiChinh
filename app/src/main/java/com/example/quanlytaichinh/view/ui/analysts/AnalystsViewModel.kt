package com.example.quanlytaichinh.view.ui.analysts

import androidx.lifecycle.ViewModel
import com.example.quanlytaichinh.model.dao.TimeDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class AnalystsViewModel(val database: TimeDao): ViewModel() {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    var times = database.getAllTimes(2020)



    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}