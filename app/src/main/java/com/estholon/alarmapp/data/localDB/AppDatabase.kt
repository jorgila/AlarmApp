package com.estholon.alarmapp.data.localDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.estholon.alarmapp.domain.model.Alarm

// Annotation
@Database(entities = [Alarm::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Global variables

    // Function

    abstract fun alarmDao() : AlarmDao

    companion object {


        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Functions

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "alarmapp_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}
