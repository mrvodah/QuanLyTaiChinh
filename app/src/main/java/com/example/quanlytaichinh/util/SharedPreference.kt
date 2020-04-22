package com.example.quanlytaichinh.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(val context: Context) {

    private val PREFS_NAME = "manager"
    val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun save(KEY_NAME: String, value: Long) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putLong(KEY_NAME, value)
        editor.apply()
    }

    fun getValueLong(KEY_NAME: String): Long {
        return sharedPref.getLong(KEY_NAME, 0L)
    }

    companion object {
        const val TOTAL = "TOTAL"
    }

}