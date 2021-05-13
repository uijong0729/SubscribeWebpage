package com.example.subscribewebpage.data

import android.content.Context
import android.database.Cursor
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteQuery

@Database(entities = [WebInfoEntity::class], version = 7)
abstract class Transaction: RoomDatabase() {
    abstract fun webInfoDao(): WebInfoDao

    companion object {
        private var INSTANCE: Transaction? = null

        fun getInstance(context: Context): Transaction? {
            if (INSTANCE == null) {
                synchronized(Transaction::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        Transaction::class.java,
                        "webroom.db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return this.INSTANCE
        }
    }
}