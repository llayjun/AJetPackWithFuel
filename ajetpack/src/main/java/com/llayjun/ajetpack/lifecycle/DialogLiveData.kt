package com.llayjun.ajetpack.lifecycle

import androidx.lifecycle.MutableLiveData
import com.llayjun.ajetpack.bean.DialogBean

class DialogLiveData<T> : MutableLiveData<T>() {

    private val bean: DialogBean = DialogBean()

    fun setValue(isShow: Boolean) {
        bean.isShow = isShow
        value = bean as T
    }

    fun postValue(isShow: Boolean){
        bean.isShow = isShow
        postValue(bean as T)
    }

}