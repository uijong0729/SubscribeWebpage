package com.example.subscribewebpage.cron

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.subscribewebpage.common.AppNotification
import com.example.subscribewebpage.common.Const
import com.example.subscribewebpage.common.Const.NOTICE_IS_UPDATED
import com.example.subscribewebpage.common.Const.NOTICE_KEY_DATA
import com.example.subscribewebpage.common.Const.NOTICE_UPDATE
import com.example.subscribewebpage.common.SaveType
import com.example.subscribewebpage.data.WebInfoEntity
import com.example.subscribewebpage.vm.WebInfoViewModel
import org.jsoup.Jsoup
import java.time.Duration
import kotlin.concurrent.thread

object SwWorkRequest {

    fun run(context: AppCompatActivity) {
        val workManager = WorkManager.getInstance(context).also {
            it.cancelAllWork()
        }
        val viewModel = ViewModelProvider(context)[WebInfoViewModel::class.java]

        WebInfoViewModel.list?.stream()?.forEach { webInfo ->
            if (webInfo != null) {
                with(webInfo) {
                    if (this.cssQuery.isNotEmpty()) {
                        if (this.url?.indexOf("https://", 0)!! == -1) {
                            this.url = "https://" + this.url
                        }
                        thread {
                            //Jsoup
                            val doc = Jsoup.connect(this.url).get()
                            val elements = doc.select(this.cssQuery)
                            Log.d(Const.DEBUG_TAG, "cssQuery : " + this.cssQuery)
                            Log.d(Const.DEBUG_TAG, "tagAttr : " + this.tagAttr)
                            val sb = StringBuilder()
                            for (line in elements) {
                                if (this.tagAttr.isNotEmpty()) {
                                    sb.append(line.attr(this.tagAttr).trim())
                                    //Log.d(Const.DEBUG_TAG, "query result : $str")
                                } else {
                                    sb.append(line.text().trim())
                                    //Log.d(Const.DEBUG_TAG, "query result : $str")
                                }
                            }

                            val currentQueryResult = sb.toString()
                            Log.d(Const.DEBUG_TAG, currentQueryResult)
                            setWebInfoHtml(this, currentQueryResult)
                            var isUpdated: Boolean = false

                            // == : 값 비교
                            // === : 참조 비교
                            if (this.previousHtml != this.currentHtml) {
                                //if (currentQueryResult.indexOf(this.searchKeyword, 0) > 0) {
                                    isUpdated = true
                                //}
                            }

                            viewModel.updateWebInfo(webInfo, SaveType.PREVIOUS)

                            if (this.interval!! < Const.MINIMUM_INTERVAL) {
                                this.interval = Const.MINIMUM_INTERVAL
                            }

                            val inputData = Data.Builder()
                                .putInt("id", this.id)
                                .putBoolean(NOTICE_IS_UPDATED, isUpdated)
                                .build()

                            val workRequest = PeriodicWorkRequest
                                //.Builder(SwWorker::class.java, Duration.ofMinutes(webInfo.interval!!))
                                .Builder(SwWorker::class.java, Duration.ofSeconds(this.interval!!))
                                .setInputData(inputData)
                                .build()

                            workManager.enqueueUniquePeriodicWork(
                                this.date,
                                ExistingPeriodicWorkPolicy.REPLACE, workRequest
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setWebInfoHtml(webInfo: WebInfoEntity, str: String) {
        if (webInfo.previousHtml.isEmpty()) {
            webInfo.previousHtml = str
        }
        webInfo.currentHtml = str
    }

    class SwWorker(appContext: Context, workerParams: WorkerParameters) :
        Worker(appContext, workerParams) {
        private val con: Context = appContext
        override fun doWork(): Result {
            val title = inputData.getString(NOTICE_KEY_DATA)
            val id: Int = inputData.getInt("id", Int.MIN_VALUE)
            val isUpdated: Boolean = inputData.getBoolean(NOTICE_IS_UPDATED, false)

            if (isUpdated && title != null) {
                AppNotification.createNotification(con, id, title, NOTICE_UPDATE)
            }
            return Result.success()
        }
    }
}