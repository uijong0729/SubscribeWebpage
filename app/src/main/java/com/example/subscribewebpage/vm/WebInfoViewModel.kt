package com.example.subscribewebpage.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.subscribewebpage.data.Transaction
import com.example.subscribewebpage.data.WebInfoEntity

/**
 * <LiveData>
 * @link https://developer.android.com/topic/libraries/architecture/livedata?hl=ko
 *
*/
class WebInfoViewModel(app: Application) : AndroidViewModel(app) {

    companion object{
        var allWebInfo : MutableLiveData<MutableList<WebInfoEntity>> = MutableLiveData()
        var list:MutableList<WebInfoEntity>? = null
    }

    init {
        getAllWebInfo()
    }

    fun getAllWebInfoObservers(): MutableLiveData<MutableList<WebInfoEntity>>{
        return allWebInfo
    }

    fun getAllWebInfo() {
        list = Transaction.getInstance(getApplication())?.webInfoDao()?.getAll()
        allWebInfo.postValue(list)
    }

    fun getWebInfo(id :Int): WebInfoEntity? = Transaction.getInstance(getApplication())?.webInfoDao()?.getWebInfoById(id)

    private fun getWebInfoByDate(date :String):WebInfoEntity? = Transaction.getInstance(getApplication())?.webInfoDao()?.getWebInfoByDate(date)

    fun insertWebInfo(entity: WebInfoEntity) :WebInfoEntity? {
        Transaction.getInstance(getApplication())?.webInfoDao()?.insert(entity)
        val result: WebInfoEntity? = getWebInfoByDate(entity.date)
        if (result != null) {
            list?.add(result)
            /*
            * postValue()는 setValue()와 다르게 백그라운드에서 값을 변경한다.
            * 백그라운드 쓰레드에서 동작하다가 메인 쓰레드에 값을 post 하는 방식으로 사용된다.
            * LiveData 의 값을 즉각적으로 변경해야 한다면 postValue()가 아닌 setValue()를 사용해야 한다.
            */
            allWebInfo.postValue(list)
        }
        return result
    }

    fun deleteWebInfo(entity: WebInfoEntity){
        Transaction.getInstance(getApplication())?.webInfoDao()?.delete(entity)
        list?.removeIf {
            it -> it.id == entity.id
        }
        allWebInfo.value = list
    }
}