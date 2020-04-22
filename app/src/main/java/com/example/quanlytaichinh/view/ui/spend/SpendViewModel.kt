package com.example.quanlytaichinh.view.ui.spend

import androidx.lifecycle.ViewModel
import com.example.quanlytaichinh.model.dao.SpendDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class SpendViewModel(val database: SpendDao): ViewModel() {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    val spends = database.getAllSpends()



    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}