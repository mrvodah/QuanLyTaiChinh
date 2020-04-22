package com.example.quanlytaichinh.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quanlytaichinh.model.dao.SectionDao
import com.example.quanlytaichinh.model.dao.SpendDao
import com.example.quanlytaichinh.model.dao.TimeDao

@Database(
    entities = [Spend::class, Section::class, Time::class],
    version = 3,
    exportSchema = false
)
abstract class ManagerDatabase: RoomDatabase() {

    abstract val sectionDao: SectionDao
    abstract val spendDao: SpendDao
    abstract val timeDao: TimeDao

    companion object {

        @Volatile
        private var INSTANCE: ManagerDatabase? = null

        fun getInstance(context: Context): ManagerDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ManagerDatabase::class.java,
                        "manager_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}