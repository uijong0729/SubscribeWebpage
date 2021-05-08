package com.example.subscribewebpage

import io.github.rybalkinsd.kohttp.ext.httpGet
import org.junit.Test
import java.net.HttpURLConnection
import org.junit.Assert.*
import java.net.URL
import io.github.rybalkinsd.kohttp.dsl.httpGet
import io.github.rybalkinsd.kohttp.ext.url
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.InputStream

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 * See [OKHTTP](https://github.com/square/okhttp)
 */
class HttpUnitTest {
    var testUrl = "http://www.roue.co.jp/"

    @Test
    fun getHttp() {
        val url = URL(testUrl)

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional default is GET

            println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    println(line)
                }
            }
        }
    }

    // https://torajirousan.hatenadiary.jp/entry/2020/02/03/145043
    // https://github.com/square/okhttp
    @Test
    fun getOkHttp() {
        val response = testUrl.httpGet()
        if (response.body() != null){
//            response.body()!!.byteStream().bufferedReader(Charsets.UTF_8).use {
//                it.lines().forEach {
//                        line -> print(line)
//                }
//            }
            var body = response.body()!!
            body.string().replace("<html*", "", true)
        }
        //print(response.body().toString())
    }

    // CSS Selectors
    // https://www.w3schools.com/cssref/css_selectors.asp
    @Test
    fun getJsoup() {
        val border = "border: 3px red solid"
        val doc = Jsoup.connect(testUrl).get()
        val elements = doc.select(".thumbnail")
        for (line in elements) {
            if (line.attr("style").indexOf(border) > 0) {
                println("## ${line.attr("href")} ")
                println("## ${line.attr("style")} ")
                println("## ${line.text()} ")
            }
        }
    }
}