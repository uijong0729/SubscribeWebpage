package com.example.subscribewebpage.model.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WebInfoEntity::class, AutoFillEntity::class], version = 4, exportSchema = false)
abstract class Transaction: RoomDatabase() {
    abstract fun webInfoDao(): WebInfoDao
    abstract fun autoFillDao(): AutoFillDao

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
            return INSTANCE
        }
    }
}