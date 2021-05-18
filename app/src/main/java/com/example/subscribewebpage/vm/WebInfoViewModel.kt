package com.example.subscribewebpage.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.subscribewebpage.common.SaveType
import com.example.subscribewebpage.data.Transaction
import com.example.subscribewebpage.data.WebInfoEntity
import kotlin.concurrent.thread

/**
 * <LiveData>
 * @link https://developer.android.com/topic/libraries/architecture/livedata?hl=ko
 *
 * 뷰 모델은 사용자 관점의 View 를 동기화하기 위한 클래스이므로 백그라운드 상의 갱신은 평범하게 room 의 dao 를 통해 접근할것
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

    fun updateWebInfo(entity: WebInfoEntity, type : SaveType){
        val dao = Transaction.getInstance(getApplication())?.webInfoDao()
        thread {
            when(type){
                SaveType.CURRENT -> dao?.updateByIdToCurrentHtml(entity.currentHtml, entity.id)
                SaveType.PREVIOUS -> dao?.updateByIdToPreviousHtml(entity.currentHtml, entity.id)
            }
            getAllWebInfo()
        }
    }
}