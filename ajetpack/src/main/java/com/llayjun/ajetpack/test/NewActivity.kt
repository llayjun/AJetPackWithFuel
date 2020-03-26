package com.llayjun.ajetpack.test

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.llayjun.ajetpack.R
import com.llayjun.ajetpack.base.BaseActivity
import com.llayjun.ajetpack.databinding.ActivityNewsBinding
import kotlinx.android.synthetic.main.activity_news.*

class NewActivity : BaseActivity<NewsViewModel, ActivityNewsBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_news

    override fun initView(savedInstanceState: Bundle?) {
        button?.setOnClickListener {
            mViewModel?.getData()
        }
    }

    override fun loadData(savedInstanceState: Bundle?) {
        mDataBinding?.viewModels = mViewModel
        mDataBinding?.lifecycleOwner = this
        mViewModel?.getNewsBean()?.observe(this, Observer {
            LogUtils.i(it.toString())
            ToastUtils.showShort(it.toString())
        })
    }

    override fun initViewModel(): NewsViewModel {
        return ViewModelProviders.of(this).get(NewsViewModel::class.java)
    }

}