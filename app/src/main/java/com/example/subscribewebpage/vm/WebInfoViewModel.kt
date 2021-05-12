package com.example.subscribewebpage.vm

import android.app.Application
import android.telecom.Call
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.subscribewebpage.data.Transaction
import com.example.subscribewebpage.data.WebInfoEntity
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import java.util.concurrent.Callable
import kotlin.concurrent.thread

/**
 * <LiveData>
 * @link https://developer.android.com/topic/libraries/architecture/livedata?hl=ko
 *
*/
class WebInfoViewModel(app: Application) : AndroidViewModel(app) {

    companion object{
        var allWebInfo : MutableLiveData<MutableList<WebInfoEntity>> = MutableLiveData()
    }

    init {
        getAllWebInfo()
    }

    fun getAllWebInfoObservers(): MutableLiveData<MutableList<WebInfoEntity>>{
        return allWebInfo
    }

    fun getAllWebInfo(){
        thread {
            val list = Transaction.getInstance(getApplication())?.webInfoDao()?.getAll()
            allWebInfo.postValue(list)
        }
    }

    fun callable(id: Int): Callable<WebInfoEntity?>? {
        return Callable {
            try {
                Callable {
                    Transaction.getInstance(getApplication())?.webInfoDao()?.getWebInfoById(id)
                }
            }catch (e: Exception){
                Log.e(this::class.java.name, e.localizedMessage)
                throw e
            }
        }.call()
    }
    fun getWebInfo(id :Int) = Transaction.getInstance(getApplication())?.webInfoDao()?.getWebInfoById(id)

    fun insertWebInfo(entity: WebInfoEntity) {
        thread {
            Transaction.getInstance(getApplication())?.webInfoDao()?.insert(entity)
        }
    }
}