package com.example.subscribewebpage.model.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AutoFillDao {

    @Query("SELECT * FROM AutoFill group by keyword order by count(keyword) DESC")
    fun getAll(): List<AutoFillEntity>

    @Query("SELECT * FROM AutoFill WHERE keyword = :key")
    fun getWebInfoByKeyword(key: String): AutoFillEntity

    @Query("INSERT INTO AutoFill(keyword) VALUES(:key)")
    fun insert(key: String)
}