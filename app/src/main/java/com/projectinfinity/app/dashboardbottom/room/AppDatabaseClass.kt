package com.projectinfinity.app.dashboardbottom.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


    @Database(entities = [Todo::class],version = 3)
    abstract class AppDatabase:RoomDatabase() {
        abstract fun todoDao(): DataAccessObject

        companion object {
            // Singleton prevents multiple instances of database opening at the
            // same time.
            @Volatile
            private var INSTANCE: AppDatabase? = null
            fun getDatabase(context: Context): AppDatabase {
                // if the INSTANCE is not null, then return it,
                // if it is, then create the database
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "todo"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                    // return instance
                    instance
                }
            }
        }
    }

