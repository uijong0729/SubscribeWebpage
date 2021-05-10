package com.example.subscribewebpage.vm

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.subscribewebpage.data.RommAppDb
import com.example.subscribewebpage.data.WebInfoEntity
import kotlin.concurrent.thread

/**
 * <LiveData>
 * @link https://developer.android.com/topic/libraries/architecture/livedata?hl=ko
 *
*/
class WebInfoViewModel(app: Application) : AndroidViewModel(app) {
    var allWebInfo : MutableLiveData<List<WebInfoEntity>> = MutableLiveData()

    fun getAllWebInfoObservers(): MutableLiveData<List<WebInfoEntity>>{
        return allWebInfo
    }

    fun getAllWebInfo(){
        thread {
            val list = RommAppDb.getInstance(getApplication())?.webInfoDao()?.getAll()
            allWebInfo.postValue(list)
        }
    }

    fun getWebInfo(id :Int) = RommAppDb.getInstance(getApplication())?.webInfoDao()?.getWebInfoById(id)

    fun insertWebInfo(vararg entity: WebInfoEntity){
        RommAppDb.getInstance(getApplication())?.webInfoDao()?.insertAll(*entity)
    }

}