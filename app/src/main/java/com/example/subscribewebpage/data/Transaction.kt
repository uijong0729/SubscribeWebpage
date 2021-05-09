package com.example.subscribewebpage.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WebInfo::class], version = 1)
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
                        .build()
                }
            }
            return this.INSTANCE
        }
    }
}