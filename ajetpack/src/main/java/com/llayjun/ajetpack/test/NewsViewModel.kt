package com.llayjun.ajetpack.test

import androidx.lifecycle.MutableLiveData
import com.github.kittinunf.fuel.httpGet
import com.llayjun.ajetpack.fuel.Api
import com.llayjun.ajetpack.lifecycle.BaseViewModel

class NewsViewModel : BaseViewModel() {

    private var mNewsBean = MutableLiveData<String>()

    fun getData() {
        mShowDialog?.setValue(true)
        Api.news.httpGet(listOf("category" to "5562b419e4b00c57d9b94ae2", "limit" to "20", "src" to "android")).responseString() { request, response, result ->
            //返回参数
            result.fold(success = {
                // 返回成功，处理数据
                println("aaa" + Thread.currentThread().name)
                mShowDialog?.postValue(false)
                mNewsBean.postValue(it)
            }, failure = {
                println("aaa" + Thread.currentThread().name)

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