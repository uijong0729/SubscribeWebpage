package com.example.subscribewebpage.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WebInfoDao {

    @Query("SELECT * FROM WebInfo")
    fun getAll(): List<WebInfo>

    @Insert
    fun insertAll(vararg info: WebInfo)
}