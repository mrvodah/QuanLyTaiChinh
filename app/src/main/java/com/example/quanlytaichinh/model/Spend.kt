package com.example.quanlytaichinh.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "spend_table")
@Parcelize
data class Spend(

    var time: String = "",

    var type: Int = 0,

    var content: String = "",

    var money: Long = 0L,

    var sectionName: String = ""
): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var spendId: Long = 0L
}