package com.example.subscribewebpage

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SwThreadPool {
    //    * If you would like to immediately block waiting
    //    * for a task, you can use constructions of the form
    //    * {@code result = exec.submit(aCallable).get();}
    companion object{
        // << 1
        private val cores :Int = (Runtime.getRuntime().availableProcessors()) shr 1
        var es: ExecutorService = Executors.newFixedThreadPool(cores)
    }
}