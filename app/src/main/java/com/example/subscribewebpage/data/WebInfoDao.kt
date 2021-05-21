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

    @Query("UPDATE WebInfo SET previousHtml = :html WHERE id = :id")
    fun updateByIdToPreviousHtml(html: String, id: Int)

    @Query("UPDATE WebInfo SET currentHtml = :html WHERE id = :id")
    fun updateByIdToCurrentHtml(html: String, id: Int)

    @Query("UPDATE WebInfo SET enable = :httpOk WHERE id = :id")
    fun updateEnableRequest(httpOk: Int, id: Int)

    @Delete
    fun delete(info: WebInfoEntity)
}