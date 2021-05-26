package com.example.subscribewebpage.cron

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.subscribewebpage.SwThreadPool
import com.example.subscribewebpage.common.AppNotification
import com.example.subscribewebpage.common.Const
import com.example.subscribewebpage.data.Transaction
import com.example.subscribewebpage.data.WebInfoEntity
import com.example.subscribewebpage.vm.WebInfoViewModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.net.UnknownHostException
import java.util.concurrent.Callable

class SwUpdateWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val context: Context = appContext

    override fun doWork(): Result {

        var getResult : Result? = SwThreadPool.es.submit(Callable {
            val dao = Transaction.getInstance(context)?.webInfoDao()
            WebInfoViewModel.list = dao?.getAll()
            Log.d(Const.DEBUG_TAG, "Start Update Worker. do work update size : ${WebInfoViewModel.list!!.size}")
            WebInfoViewModel.list?.stream()?.forEach { webInfo ->
                with(webInfo) {
                    var doc: Document?
                    var elements: Elements?
                    //Jsoup
                    try {
                        // https://jsoup.org/
                        doc = Jsoup.connect(this.url).get()
                    } catch (e: UnknownHostException) {
                        Log.d(
                            Const.DEBUG_TAG,
                            "UnknownHostException : ${e.localizedMessage}, id : ${webInfo.id}"
                        )
                        dao?.updateEnableRequest(Const.DISABLE, webInfo.id)
                        return@with
                    }
                    val response = doc.html()

                    if (response.length < 61 || webInfo.url!!.length < 9) {
                        dao?.updateEnableRequest(Const.DISABLE, webInfo.id)
                        return@with
                    }

                    // 스크립트 태그 삭제
                    if (this.cssQuery.isBlank()) {
                        elements = doc.body().allElements
                    } else {
                        elements = doc.body().select(this.cssQuery)
                    }

                    for(removeTarget in elements.tagName(Const.TAG_NAME_SCRIPT)){
                        removeTarget.remove()
                    }

                    val sb = StringBuilder(response.length)
                    if (this.tagAttr.isBlank()) {
                        // 유저관점
                        val regex = """(</[A-Za-z>]+)|(.+=.+)|\s\s\s|\n""".toRegex()
                        sb.append(regex.replace(elements.html(), ""))
                    } else {
                        // 기술자 관점
                        val elementList = elements.tagName(this.tagAttr)
                        for (line in elementList) {
                            sb.append(line.text())
                        }
                    }

                    //val regex = """([!"#${'$'}%&'()*+,./:;<=>?@\^_`{|}~-])""".toRegex()
                    var currentQueryResult = sb.toString()
                    if (sb.toString().length > 8192) {
                        currentQueryResult = currentQueryResult.substring(0, 8191)
                    }

                    //Log.d(Const.DATA_TAG, regex.replace(currentQueryResult, ""))
                    Log.d(Const.DATA_TAG, currentQueryResult)
                    setWebInfoHtml(this, currentQueryResult)

                    // 更新
                    dao?.updateByIdToPreviousHtml(this.currentHtml, this.id)
                    if (this.interval!! < Const.MINIMUM_INTERVAL) {
                        this.interval = Const.MINIMUM_INTERVAL
                    }
                }
            }
            Log.d(Const.DEBUG_TAG, "Exit SwUpdate Worker")
            return@Callable Result.success()
        }).get()

        if (getResult == null){
            return Result.failure()
        }else{
            WebInfoViewModel.list?.stream()?.forEach{ webInfo ->
                with(webInfo) {
                    // == : 값 비교
                    // === : 참조 비교
                    var isUpdated = false
                    if (this.previousHtml != this.currentHtml) {
                        if (currentHtml.indexOf(this.searchKeyword, 0) > 0) {
                            isUpdated = true
                        }
                    }

                    Log.d(Const.DEBUG_TAG, "Notification Status $isUpdated")

                    if (isUpdated) {
                        AppNotification.createNotification(
                            context,
                            this.id,
                            title,
                            Const.NOTICE_UPDATE
                        )
                    }
                }
            }
            return getResult
        }
    }
}

private fun setWebInfoHtml(webInfo: WebInfoEntity, str: String) {
    if (webInfo.previousHtml.isEmpty()) {
        webInfo.previousHtml = str
    }
    webInfo.currentHtml = str
}
