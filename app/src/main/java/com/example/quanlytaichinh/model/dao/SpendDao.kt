package com.example.quanlytaichinh.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.quanlytaichinh.model.Spend

@Dao
interface SpendDao {

    @Insert
    fun insert(spend: Spend)

    @Update
    fun update(spend: Spend)

    @Delete
    fun delete(spend: Spend)

    @Query("select * from spend_table")
    fun getAllSpends(): LiveData<List<Spend>>

    @Query("SELECT * from spend_table WHERE time = :time AND type = :type AND sectionName = :sectionName")
    fun getSpends(time: String, type: Int, sectionName: String): LiveData<List<Spend>>

}