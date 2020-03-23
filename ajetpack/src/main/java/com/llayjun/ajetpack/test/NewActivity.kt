package com.llayjun.ajetpack.test

import android.os.Bundle
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.llayjun.ajetpack.R
import com.llayjun.ajetpack.base.BaseMVVMActivity
import com.llayjun.ajetpack.databinding.ActivityNewsBinding
import kotlinx.android.synthetic.main.activity_news.*

class NewActivity : BaseMVVMActivity<NewsViewModel, ActivityNewsBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_news

    override fun initData(savedInstanceState: Bundle?) {
        mDataBinding?.viewModels = this
    }

    override fun initView(savedInstanceState: Bundle?) {
        button?.setOnClickListener {
            mViewModel?.getData()
        }
    }

    override fun loadData(savedInstanceState: Bundle?) {
        mViewModel?.getNewsBean()?.observe(this, Observer {
            LogUtils.i(it.toString())
            ToastUtils.showShort(it.toString())
        })
    }

    override fun showToast(obj: Any?) {
        ToastUtils.showShort(obj.toString())
    }

}