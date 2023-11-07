package com.example.subscribewebpage.logic

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.subscribewebpage.SwThreadPool
import com.example.subscribewebpage.common.Const
import java.util.concurrent.Callable

class SwUpdateWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    private val context: Context = appContext

    override fun doWork(): Result {

        val getResult : Result? = SwThreadPool.es.submit(Callable {
            Log.d(Const.DEBUG_TAG, "Exit SwUpdate Worker")
            return@Callable Result.success()
        }).get()

        if (getResult == null){
            return Result.failure()
        }else{
            return getResult
        }
    }
}
