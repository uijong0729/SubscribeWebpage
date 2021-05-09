package com.example.subscribewebpage

import android.util.Log
import androidx.room.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.subscribewebpage.data.Transaction
import com.example.subscribewebpage.data.WebInfo

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * InstrumentationRegistry.getInstrumentation().targetContext
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val transaction = Transaction.getInstance(InstrumentationRegistry.getInstrumentation().targetContext)
        val dao = transaction?.webInfoDao()
        dao?.insertAll(
            WebInfo("name ${Math.random()}" , "keyword", 5, 20201231000000)
        )

        val list = dao?.getAll()
        for(items in list!!){
            Log.d("[DEBUGER]", "[DEBUGER] ${items.title}")
        }

    }
}

