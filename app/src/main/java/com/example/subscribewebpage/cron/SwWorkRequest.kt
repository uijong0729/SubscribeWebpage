package com.example.subscribewebpage.cron

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import androidx.work.ExistingPeriodicWorkPolicy.REPLACE
import com.example.subscribewebpage.common.AppNotification
import com.example.subscribewebpage.common.Const
import com.example.subscribewebpage.common.Const.MINIMUM_INTERVAL
import com.example.subscribewebpage.vm.WebInfoViewModel
import java.time.Duration

object SwWorkRequest {


    fun run(context: AppCompatActivity){
        val workManager = WorkManager.getInstance(context)

        WebInfoViewModel.allWebInfo.observe(context, { list ->
            workManager.cancelAllWork()
            list.stream().forEach { webInfo ->
                if (webInfo != null) {
                    if (webInfo.interval!! < MINIMUM_INTERVAL){
                        webInfo.interval = MINIMUM_INTERVAL
                    }
                    val inputData = Data.Builder()
                        .putString("aa", "bb")
                        .build()

                    val workRequest = PeriodicWorkRequest
                        //.Builder(SwWorker::class.java, Duration.ofMinutes(webInfo.interval!!))
                        .Builder(SwWorker::class.java, Duration.ofSeconds(webInfo.interval!!))
                        .setInputData(inputData)
                        .build()

                    workManager.enqueueUniquePeriodicWork(webInfo.date, REPLACE, workRequest)
                }
            }
        })
    }

    class SwWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
        private val con :Context = appContext
        override fun doWork(): Result {
            val data : String? = inputData.getString("aa")
            if (data != null) {
                Log.d(Const.DEBUG_TAG, data)
                // 알림
                AppNotification.createNotification(con, 1, "title", "content")
            }
            return Result.success()
        }
    }
}