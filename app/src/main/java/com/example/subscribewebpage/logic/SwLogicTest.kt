package com.example.subscribewebpage.logic

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.junit.Test

class SwLogicTest {

    // CIO : Coroutine based I/O
    private val client: HttpClient = HttpClient(CIO);

    @Test
    suspend fun test() {
        val url = "https://www.naver.com/"
        val response: HttpResponse = client.get(url)
        Log.d("Test", response.readText());
        assert(true)
    }
}