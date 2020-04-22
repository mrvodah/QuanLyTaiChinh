package com.example.quanlytaichinh.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "section_table")
data class Section(

    @PrimaryKey(autoGenerate = false)
    var name: String = "",

    var description: String = "",

    var percent: Int = 0,

    var paid: Long = 0L,

    var total: Long = 0L
)