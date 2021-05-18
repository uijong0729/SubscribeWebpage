package com.example.subscribewebpage.cron

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.subscribewebpage.common.Const
import java.util.concurrent.TimeUnit

object SwWorkRequest {

    fun updateData(context: Context) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWork()

        // DB 갱신용 Work
        val workRequest = PeriodicWorkRequest
            .Builder(SwUpdateWorker::class.java, Const.MINIMUM_INTERVAL, TimeUnit.MINUTES)
            .build()

        workManager.enqueueUniquePeriodicWork(
            Const.SW_UPDATE_WORKER_ID,
            ExistingPeriodicWorkPolicy.KEEP, workRequest
        )
    }


}

