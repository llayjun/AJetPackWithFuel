package com.llayjun.ajetpack.test

import androidx.lifecycle.MutableLiveData
import com.github.kittinunf.fuel.httpGet
import com.llayjun.ajetpack.fuel.Api
import com.llayjun.ajetpack.lifecycle.BaseViewModel

class NewsViewModel : BaseViewModel() {

    private var mNewsBean = MutableLiveData<String>()

    fun getData() {
        mShowDialog?.setValue(true)
        Api.news.httpGet(listOf("foo" to "foo", "bar" to "bar")).responseString { request, response, result ->
            //返回参数
            result.fold(success = {
                // 返回成功，处理数据
                mShowDialog?.postValue(false)
                mNewsBean.postValue(it)
            }, failure = {
                // 请求失败，执行错误处理
                mShowDialog?.postValue(false)
                mThrowable?.postValue(it)
            })
        }
    }

    fun getNewsBean(): MutableLiveData<String> {
        return mNewsBean
    }

}