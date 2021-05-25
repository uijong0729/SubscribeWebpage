package com.example.subscribewebpage.cron

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.subscribewebpage.SwThreadPool
import com.example.subscribewebpage.common.AppNotification
import com.example.subscribewebpage.common.Const
import com.example.subscribewebpage.data.Transaction
import com.example.subscribewebpage.vm.WebInfoViewModel

class SwNoticeWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val con: Context = appContext

    override fun doWork(): Result {

        SwThreadPool.es.submit {
            //=========== Data 취득 =============
            val dao = Transaction.getInstance(con)?.webInfoDao()
            WebInfoViewModel.list = dao?.getAll()
            Log.d(Const.DEBUG_TAG, "Start Notice Worker. do work update size : ${WebInfoViewModel.list!!.size}")

            //=========== 값 비교를 위한 루프 =============
            WebInfoViewModel.list?.stream()?.forEach { webInfo ->
                var isUpdated: Boolean = false
                if (webInfo != null) {
                    with(webInfo) {
                        // == : 값 비교
                        // === : 참조 비교
                        if (this.previousHtml != this.currentHtml) {
                            if (currentHtml.indexOf(this.searchKeyword, 0) > 0) {
                                isUpdated = true
                            }
                        }

                        Log.d(Const.DEBUG_TAG, "Notification Status $isUpdated")

                        if (isUpdated) {
                            AppNotification.createNotification(
                                con,
                                this.id,
                                title,
                                Const.NOTICE_UPDATE
                            )
                        }
                    }
                }
            }
        }
        return Result.success()
    }
}