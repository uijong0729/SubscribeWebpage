package com.example.subscribewebpage.model.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

//https://developer.android.com/training/data-storage/room/accessing-data
@Dao
interface WebInfoDao {

    @Query("SELECT title, searchKeyword, url, interval, enable, date, id, substr(previousHtml, 0, 8191) as previousHtml, substr(currentHtml, 0, 8191) as currentHtml, cssQuery, tagAttr  FROM WebInfo")
    fun getAll(): MutableList<WebInfoEntity>

    @Query("SELECT title, searchKeyword, url, interval, enable, date, id, substr(previousHtml, 0, 8191) as previousHtml, substr(currentHtml, 0, 8191) as currentHtml, cssQuery, tagAttr  FROM WebInfo")
    fun getAllBack(): Flow<List<WebInfoEntity>>

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