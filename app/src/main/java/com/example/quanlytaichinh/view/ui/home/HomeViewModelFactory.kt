package com.example.quanlytaichinh.view.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quanlytaichinh.model.dao.SectionDao

class HomeViewModelFactory(
    private val sectionDao: SectionDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(sectionDao) as T
    }
}