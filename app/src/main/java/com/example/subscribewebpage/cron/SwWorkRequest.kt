package com.example.subscribewebpage.cron

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import androidx.work.ExistingPeriodicWorkPolicy.REPLACE
import com.example.subscribewebpage.common.Const
import com.example.subscribewebpage.data.WebInfoEntity
import com.example.subscribewebpage.vm.WebInfoViewModel
import kotlinx.coroutines.delay
import java.time.Duration

// https://beomseok95.tistory.com/193
object SwWorkRequest {
    private const val MINIMUM_INTERVAL :Long = 15

    fun run(context: AppCompatActivity){
        val workManager = WorkManager.getInstance(context)

        WebInfoViewModel.allWebInfo.observe(context, Observer<MutableList<WebInfoEntity>>{ list ->
            list.stream().forEach { webInfo ->
                if (webInfo != null) {
                    if (webInfo.interval!! < MINIMUM_INTERVAL){
                        webInfo.interval = MINIMUM_INTERVAL
                    }
                    val inputData = Data.Builder()
                        .putString("aa", "bb")
                        .build()

                    val workRequest = PeriodicWorkRequest
                        .Builder(SwWorker::class.java, Duration.ofMinutes(webInfo.interval!!))
                        .setInputData(inputData)
                        .build()

                    workManager.enqueueUniquePeriodicWork(webInfo.date, REPLACE, workRequest)
                }

            }
        })

    }

    // 참고 : sealed interface : 상속 제한 인터페이스
    class SwWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
        override fun doWork(): Result {
            val data : String? = inputData.getString("aa")
            if (data != null) {
                Log.d(Const.DEBUG_TAG, data)
            }
            return Result.success()
        }
    }
}