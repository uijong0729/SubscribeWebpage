package com.example.subscribewebpage.cron

import android.content.Context
import androidx.work.*

// https://developer.android.com/topic/libraries/architecture/workmanager/basics?hl=ko
// https://beomseok95.tistory.com/193
class SwWorkManager(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    override fun doWork(): Result {
        return Result.success()
    }


}