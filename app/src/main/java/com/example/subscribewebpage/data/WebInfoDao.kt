package com.example.subscribewebpage.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WebInfoDao {

    @Query("SELECT * FROM WebInfo")
    fun getAll(): List<WebInfoEntity>

    @Query("SELECT * FROM WebInfo WHERE id = :infoKey")
    fun getWebInfoById(infoKey: Int): WebInfoEntity

    @Insert
    fun insertAll(vararg info: WebInfoEntity)
}