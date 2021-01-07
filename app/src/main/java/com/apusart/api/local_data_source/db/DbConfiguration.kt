package com.apusart.api.local_data_source.db

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.apusart.api.Test
import com.apusart.api.local_data_source.db.dao.IEventDao

@Database(entities = [Test::class], version = 1)
abstract class EventlyDatabase: RoomDatabase() {
    abstract fun eventDao(): IEventDao

    companion object {
        private lateinit var applicationContext: Context

        val db by lazy {
            databaseBuilder(applicationContext, EventlyDatabase::class.java, "evently.db")
                .build()
        }

        //to musi być wywołane przez pierwszą odpalaną aktywność
        fun initialize(appContext: Context) {
            applicationContext = appContext
        }
    }
}
