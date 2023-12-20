package com.vladimir.capturer.internal.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vladimir.capturer.internal.data.entity.HttpTransaction

@Database(entities = [HttpTransaction::class], version = 9, exportSchema = false)
internal abstract class CapturerDatabase : RoomDatabase() {
    abstract fun transactionDao(): HttpTransactionDao

    companion object {
        private const val DB_NAME = "capturer.db"

        fun create(applicationContext: Context): CapturerDatabase {
            return Room.databaseBuilder(applicationContext, CapturerDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}