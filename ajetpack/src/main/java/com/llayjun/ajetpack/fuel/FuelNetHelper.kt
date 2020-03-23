package com.llayjun.ajetpack.fuel

import com.blankj.utilcode.util.LogUtils
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.Request

object FuelNetHelper {

    const val BASE_URL = "http://httpbin.org"

    fun initFuel() {
        // 服务器base地址
        FuelManager.instance.basePath = BASE_URL
        FuelManager.instance.timeoutInMillisecond = 20000
//        FuelManager.instance.addRequestInterceptor(tokenInterceptor())
        FuelManager.instance.addRequestInterceptor(cUrlLoggingRequestInterceptor())
    }

    // token
    private fun tokenInterceptor() = { next: (Request) -> Request ->
        { req: Request ->
            req.header(mapOf("Authorization" to "token"))
            next(req)
        }
    }

    // 日志拦截
    private fun cUrlLoggingRequestInterceptor() = { next: (Request) -> Request ->
        { r: Request ->
            val logging = StringBuffer()
            logging.append("\n-----Method = ${r.method}")
            logging.append("\n-----headers = ${r.headers}")
            logging.append("\n-----url---->${r.url}")
            if (r.method == Method.POST) {
                logging.append("\n-----request parameters:")
                r.parameters.forEach {
                    logging.append("\n-----${it.first}=${it.second}")
                }
            }
            LogUtils.i(logging.toString())
            next(r)
        }
    }

}