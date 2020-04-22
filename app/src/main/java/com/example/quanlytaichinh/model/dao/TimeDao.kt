package com.example.quanlytaichinh.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.quanlytaichinh.model.Time

@Dao
interface TimeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(time: Time)

    @Query("UPDATE time_table SET paid = (paid + :paid) WHERE month = :month AND year = :year")
    fun updatePaid(month: Int, year: Int, paid: Long)

    @Query("UPDATE time_table SET paid = (paid - :old + :paid) WHERE month = :month AND year = :year")
    fun updatePaid(month: Int, year: Int, old: Long, paid: Long)

    @Query("UPDATE time_table SET total = (total + :total) WHERE month = :month AND year = :year")
    fun updateTotal(month: Int, year: Int, total: Long)

    @Query("UPDATE time_table SET total = (total - :old + :total) WHERE month = :month AND year = :year")
    fun updateTotal(month: Int, year: Int, old: Long, total: Long)

    @Query("SELECT * from time_table WHERE month = :month AND year = :year")
    fun getTime(month: Int, year: Int): Time?

    @Query("select * from time_table where year = :year order by month")
    fun getAllTimes(year: Int): LiveData<List<Time>>

}