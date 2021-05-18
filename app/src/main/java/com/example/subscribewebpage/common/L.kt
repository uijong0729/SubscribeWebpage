package com.example.subscribewebpage.common

import android.util.Log
import com.example.subscribewebpage.common.Const.DEBUG_TAG

class L {
    companion object {
        fun debug(any: Any, msg:String){
            val tag :String = any.javaClass.name
            Log.d("[$tag] $DEBUG_TAG ", msg)
        }
    }
}
