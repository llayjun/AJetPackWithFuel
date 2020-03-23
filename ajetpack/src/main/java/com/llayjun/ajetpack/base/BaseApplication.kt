package com.llayjun.ajetpack.base

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.llayjun.ajetpack.fuel.FuelNetHelper

class BaseApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        FuelNetHelper.initFuel()
    }
}