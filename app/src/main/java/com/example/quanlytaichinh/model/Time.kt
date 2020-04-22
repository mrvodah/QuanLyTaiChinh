package com.example.quanlytaichinh.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "time_table")
data class Time(

    var month: Int = 0,

    var year: Int = 0,

    var total: Long = 0L,

    var paid: Long = 0L
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}