package com.example.subscribewebpage.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class WebInfo (
    @PrimaryKey
    val title: String = "",
    @ColumnInfo
    val keyword: String?,
    @ColumnInfo
    val url: String?,
    @ColumnInfo
    val interval: Int?,
    @ColumnInfo
    val inputDate: Long = 19700101000000,
)