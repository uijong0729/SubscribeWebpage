package com.example.subscribewebpage.cron

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.subscribewebpage.SwThreadPool
import com.example.subscribewebpage.common.Const
import com.example.subscribewebpage.data.Transaction
import com.example.subscribewebpage.data.WebInfoEntity
import com.example.subscribewebpage.vm.WebInfoViewModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class SwUpdateWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val context: Context = appContext

    override fun doWork(): Result {
        SwThreadPool.es.submit {
            val dao = Transaction.getInstance(context)?.webInfoDao()
            Log.d(Const.DEBUG_TAG, "do work update size : ${WebInfoViewModel.list!!.size}")
            WebInfoViewModel.list?.stream()?.forEach { webInfo ->
                with(webInfo) {
                    if (this.cssQuery.isNotEmpty()) {
                        var doc: Document? = null
                        var elements: Elements? = null

                        //Jsoup
                        try {
                            doc = Jsoup.connect(this.url).get()
                        }catch (e: UnknownHostException){
                            Log.d(Const.DEBUG_TAG, "UnknownHostException : ${e.localizedMessage}, id : ${webInfo.id}")
                            dao?.updateEnableRequest(Const.DISABLE, webInfo.id)
                            return@with
                        }
                        val response = doc.html()

                        if (response.length < 61 || webInfo.url!!.length < 9) {
                            dao?.updateEnableRequest(Const.DISABLE, webInfo.id)
                            return@with
                        }

                        if (this.cssQuery.isBlank()) {
                            elements = doc.allElements
                        } else {
                            elements = doc.select(this.cssQuery)
                        }

                        val sb = StringBuilder(response.length)
                        var currentQueryResult = ""
                        if (this.tagAttr.isNotEmpty()) {
                            for (line in elements) {
                                sb.append(line.attr(this.tagAttr).trim())
                            }
                            currentQueryResult = sb.toString()
                        } else {
                            currentQueryResult = response
                        }

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
        return Result.success()
    }
}

private fun setWebInfoHtml(webInfo: WebInfoEntity, str: String) {
    if (webInfo.previousHtml.isEmpty()) {
        webInfo.previousHtml = str
    }
    webInfo.currentHtml = str
}
