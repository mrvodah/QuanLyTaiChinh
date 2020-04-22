package com.example.quanlytaichinh.view.ui.analysts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quanlytaichinh.model.dao.TimeDao

class AnalystsViewModelFactory(
    private val database: TimeDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AnalystsViewModel(database) as T
    }

}