package com.example.subscribewebpage.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.subscribewebpage.data.AutoFillEntity
import com.example.subscribewebpage.data.Transaction
import kotlin.concurrent.thread

/**
 * <LiveData>
 * @link https://developer.android.com/topic/libraries/architecture/livedata?hl=ko
 *
*/
class AutoFillViewModel(app: Application) : AndroidViewModel(app) {

    companion object{
        var liveAutoFill : MutableLiveData<Array<String>> = MutableLiveData()
        var list:List<AutoFillEntity>? = null
    }

    init {
        getAllAutoFill()
    }

    fun getAllWebInfoObservers(): MutableLiveData<Array<String>>{
        return liveAutoFill
    }

    fun getAllAutoFill() {
        thread {
            list = Transaction.getInstance(getApplication())?.autoFillDao()?.getAll()
            if (list!!.isNotEmpty()){
                var tmpList = Array(list!!.size) { "" }
                for (idx in list!!.indices){
                    tmpList[idx] = list!![idx].keyword
                }
                liveAutoFill.postValue(tmpList)
            }
        }
    }

    fun insertAutoFill(entity: AutoFillEntity) {
        thread {
            Transaction.getInstance(getApplication())?.autoFillDao()?.insert(entity.keyword)
        }
    }
}