package com.example.subscribewebpage.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WebInfo")
data class WebInfoEntity(
    @ColumnInfo val title: String,
    @ColumnInfo val description: String,
    @ColumnInfo val keyword: String,
    @ColumnInfo val url: String?,
    @ColumnInfo val interval: Int?,
    // 1 == true, 0 == false
    @ColumnInfo val enable: Int = 1
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}