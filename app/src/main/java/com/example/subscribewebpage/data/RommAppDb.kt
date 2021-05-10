package com.example.subscribewebpage.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WebInfoEntity::class], version = 4)
abstract class RommAppDb: RoomDatabase() {
    abstract fun webInfoDao(): WebInfoDao

    companion object {
        private var INSTANCE: RommAppDb? = null

        fun getInstance(context: Context): RommAppDb? {
            if (INSTANCE == null) {
                synchronized(RommAppDb::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        RommAppDb::class.java,
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