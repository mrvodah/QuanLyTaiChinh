package com.example.quanlytaichinh.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.quanlytaichinh.model.Section

@Dao
interface SectionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(section: Section)

    @Update
    fun update(section: Section)

    @Query("UPDATE section_table SET total = (total + :total * percent / 100) WHERE name != :name")
    fun update(name: String, total: Long)

    @Query("UPDATE section_table SET total = (total - :old * percent / 100 + :total * percent / 100) WHERE name != :name")
    fun update(name: String, old: Long, total: Long)

    @Query("UPDATE section_table SET paid = (paid + :paid) WHERE name = :name")
    fun updatePaid(name: String, paid: Long)

    @Query("UPDATE section_table SET paid = (paid - :old + :paid) WHERE name = :name")
    fun updatePaid(name: String, old: Long, paid: Long)

    @Query("UPDATE section_table SET total = (total + :total) WHERE name = :name")
    fun updateTotal(name: String, total: Long)

    @Query("UPDATE section_table SET total = (total - :old + :total) WHERE name = :name")
    fun updateTotal(name: String, old: Long, total: Long)

    @Query("select * from section_table")
    fun getAllSections(): LiveData<List<Section>>

}