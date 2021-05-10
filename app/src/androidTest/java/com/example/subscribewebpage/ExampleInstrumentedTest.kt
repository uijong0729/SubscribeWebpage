package com.example.subscribewebpage

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.subscribewebpage.data.RommAppDb
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * InstrumentationRegistry.getInstrumentation().targetContext
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val transaction = RommAppDb.getInstance(context)
        val dao = transaction?.webInfoDao()
//        dao?.insertAll(
//            WebInfo("name ${Math.random()}" , "keyword", "url", 5, 20201231000000)
//        )
//
//        // Create the observer which updates the UI.
//        val nameObserver = Observer<List<WebInfo>> { list ->
//            for (webInfo in list){
//                Log.d("[DEBUGGER]", webInfo.title)
//            }
//        }
//        dao?.getAll()!!.observe(context, nameObserver)
    }
}

