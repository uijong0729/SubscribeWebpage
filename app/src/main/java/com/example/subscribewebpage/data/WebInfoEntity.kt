package com.example.subscribewebpage.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat

@Entity(tableName = "WebInfo")
data class WebInfoEntity(
    @ColumnInfo val title: String,
    @ColumnInfo val searchKeyword: String,
    @ColumnInfo val cssQuery: String,
    @ColumnInfo val url: String?,
    @ColumnInfo var interval: Long?,
    // 1 == true, 0 == false
    @ColumnInfo val enable: Int = 1,
    @ColumnInfo val date: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @ColumnInfo(defaultValue = "")
    var previousHtml: String = ""
    @ColumnInfo(defaultValue = "")
    var nextHtml: String = ""

    override fun equals(other: Any?): Boolean {
        if (other is WebInfoEntity){
            return this.id == other.id
        }
        return false
    }
}