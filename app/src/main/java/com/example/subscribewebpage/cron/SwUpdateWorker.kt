package com.example.subscribewebpage.cron

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.subscribewebpage.common.Const
import com.example.subscribewebpage.data.Transaction
import com.example.subscribewebpage.data.WebInfoEntity
import com.example.subscribewebpage.vm.WebInfoViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import java.util.concurrent.TimeUnit

class SwUpdateWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val context: Context = appContext

    override fun doWork(): Result = runBlocking {
        launch {
            val dao = Transaction.getInstance(context)?.webInfoDao()
            WebInfoViewModel.list = dao?.getAll()
            WebInfoViewModel.list?.stream()?.forEach { webInfo ->
                if (webInfo != null) {
                    with(webInfo) {
                        if (this.cssQuery.isNotEmpty()) {
                            if (this.url?.indexOf("https://", 0)!! == -1) {
                                this.url = "https://" + this.url
                            }

                            //Jsoup
                            val doc = Jsoup.connect(this.url).get()
                            val elements = doc.select(this.cssQuery)
                            Log.d(Const.DEBUG_TAG, "cssQuery : " + this.cssQuery)
                            Log.d(Const.DEBUG_TAG, "tagAttr : " + this.tagAttr)
                            val sb = StringBuilder()
                            for (line in elements) {
                                if (this.tagAttr.isNotEmpty()) {
                                    sb.append(line.attr(this.tagAttr).trim())
                                } else {
                                    sb.append(line.text().trim())
                                }
                            }

                            val currentQueryResult = sb.toString()
                            Log.d(Const.DEBUG_TAG, currentQueryResult)
                            setWebInfoHtml(this, currentQueryResult)

                            // 更新
                            dao?.updateByIdToPreviousHtml(this.currentHtml, this.id)

                            if (this.interval!! < Const.MINIMUM_INTERVAL) {
                                this.interval = Const.MINIMUM_INTERVAL
                            }

                            val workRequest = PeriodicWorkRequest
                                .Builder(
                                    SwNoticeWorker::class.java,
                                    this.interval!!,
                                    TimeUnit.MINUTES
                                )
                                .build()

                            val workManager = WorkManager.getInstance(context)
                            workManager.enqueueUniquePeriodicWork(
                                this.date,
                                ExistingPeriodicWorkPolicy.REPLACE, workRequest
                            )

                        }
                    }
                }
            }
        }
        return@runBlocking Result.success()
    }

    private fun setWebInfoHtml(webInfo: WebInfoEntity, str: String) {
        if (webInfo.previousHtml.isEmpty()) {
            webInfo.previousHtml = str
        }
        webInfo.currentHtml = str
    }
}