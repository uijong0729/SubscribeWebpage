package com.example.subscribewebpage.cron

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

// https://beomseok95.tistory.com/193
class SwWorkRequest(val context: Context) {

    fun run(){
        val workRequest = PeriodicWorkRequest.Builder(SwWorkManager::class.java, 15, TimeUnit.MINUTES).build()
        val workManager = WorkManager.getInstance(context)
        workManager.enqueue(workRequest)
    }

}