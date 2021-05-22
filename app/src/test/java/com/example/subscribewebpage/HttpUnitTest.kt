package com.example.subscribewebpage

import io.github.rybalkinsd.kohttp.ext.httpGet
import org.jsoup.Jsoup
import org.junit.Test
import java.net.HttpURLConnection
import java.net.URL

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 * See [OKHTTP](https://github.com/square/okhttp)
 */
class HttpUnitTest {
    var testUrl = "https://www.naver.com/"

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
        //java.lang.IllegalArgumentException
        //java.net.UnknownHostException
        val response = "http://asdf".httpGet()
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
        //val doc = Jsoup.connect(testUrl).get()
        val doc = Jsoup.connect("https://a").get()
        val elements = doc.select("style"/*".thumbnail"*/)
        for (line in elements) {
            println(line.text())
//            if (line.attr("style").indexOf(border) > 0) {
//                println("## ${line.attr("href")} ")
//                println("## ${line.attr("style")} ")
//                println("## ${line.text()} ")
//            }
        }
    }
}