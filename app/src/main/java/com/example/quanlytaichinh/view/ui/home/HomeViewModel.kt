package com.example.quanlytaichinh.view.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.quanlytaichinh.model.Section
import com.example.quanlytaichinh.model.dao.SectionDao
import kotlinx.coroutines.*

class HomeViewModel(val sectionDao: SectionDao) : ViewModel() {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    val sections = sectionDao.getAllSections()

    fun addSections() {
        uiScope.launch { 
            insert()
        }
    }
    
    private suspend fun insert() {
        withContext(Dispatchers.IO) {
            sectionDao.insert(Section("NEC", "Nhu cầu thiết yếu", 55, 0, 0))
            sectionDao.insert(Section("EDUC", "Giáo dục đào tạo", 10, 0, 0))
            sectionDao.insert(Section("PLAY", "Giải trí", 10, 0, 0))
            sectionDao.insert(Section("LTSS", "Tiết kiệm dài hạn", 10, 0, 0))
            sectionDao.insert(Section("GIVE", "Cho đi", 5, 0, 0))
            sectionDao.insert(Section("FFA", "Quỹ tự do tài chính", 10, 0, 0))
            sectionDao.insert(Section("TT", "Tổng tiền", 0, 0, 0))
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}