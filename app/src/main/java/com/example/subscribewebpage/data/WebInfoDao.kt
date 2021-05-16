package com.example.subscribewebpage.data

import androidx.room.*

@Dao
interface WebInfoDao {

    @Query("SELECT * FROM WebInfo")
    fun getAll(): MutableList<WebInfoEntity>

    @Query("SELECT * FROM WebInfo WHERE id = :infoKey")
    fun getWebInfoById(infoKey: Int): WebInfoEntity

    @Query("SELECT * FROM WebInfo WHERE date = :date")
    fun getWebInfoByDate(date: String): WebInfoEntity

    @Insert
    fun insert(info: WebInfoEntity)

    @Update
    fun update(info: WebInfoEntity)

    @Delete
    fun delete(info: WebInfoEntity)
}