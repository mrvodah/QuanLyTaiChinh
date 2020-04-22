package com.example.quanlytaichinh.view.ui.spend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quanlytaichinh.model.dao.SpendDao

class SpendViewModelFactory(
    private val database: SpendDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SpendViewModel(database) as T
    }
}