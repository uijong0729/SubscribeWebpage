package com.example.subscribewebpage.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AutoFill")
data class AutoFillEntity(
    @ColumnInfo
    val keyword: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    override fun toString(): String {
        return this.keyword
    }
}