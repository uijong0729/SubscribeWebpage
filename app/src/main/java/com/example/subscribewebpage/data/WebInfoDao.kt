package com.example.subscribewebpage.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WebInfoDao {

    @Query("SELECT * FROM WebInfo")
    fun getAll(): MutableList<WebInfoEntity>

    @Query("SELECT * FROM WebInfo WHERE id = :infoKey")
    fun getWebInfoById(infoKey: Int): WebInfoEntity

    @Insert
    fun insert(info: WebInfoEntity)

    @Update
    fun update(info: WebInfoEntity)
}