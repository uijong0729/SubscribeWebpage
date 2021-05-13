package com.example.subscribewebpage.common

import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object AppDateUtils {
    fun getStringDate():String{
        ZonedDateTime.now(ZoneId.of("Asia/Tokyo")).also {
            val sb = StringBuilder()
            sb.append(it.year)
            sb.append(plusZero(it.monthValue))
            sb.append(plusZero(it.dayOfMonth))
            sb.append(plusZero(it.hour))
            sb.append(plusZero(it.minute))
            sb.append(plusZero(it.second))
            return sb.toString()
        }
    }

    private fun plusZero(i :Int) :String{
        if (i < 0) {
            return ""
        }
        return if (i < 10) "0$i" else "$i"
    }
}