package com.llayjun.ajetpack.base

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.llayjun.ajetpack.R
import com.llayjun.ajetpack.bean.DialogBean
import com.llayjun.ajetpack.dialog.LoadingDialog
import com.llayjun.ajetpack.lifecycle.BaseViewModel
import com.llayjun.ajetpack.util.ActivityUtil
import org.json.JSONException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketTimeoutException

abstract class BaseMVVMActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {

    public var mViewModel: VM? = null

    public var mDataBinding: DB? = null

    public var mContext: Context? = null

    public var mLoadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        this.mContext = this
        ActivityUtil.getInstance()?.addActivity(this)
        mLoadingDialog = LoadingDialog.create(this, "加载中...", false, null)
        initViewModel()
        initObserve()
        initData(savedInstanceState)
        initView(savedInstanceState)
        loadData(savedInstanceState)
    }

    private fun initViewModel() {
        if (mViewModel == null) {
            val modelClass: Class<BaseViewModel>
            val type: Type? = javaClass.genericSuperclass
            modelClass = if (type is ParameterizedType) {
                (type as ParameterizedType).actualTypeArguments[0] as Class<BaseViewModel>
            } else {
                // 如果没有指定泛型参数，则默认使用BaseViewModel
                BaseViewModel::class.java
            }
            mViewModel = ViewModelProviders.of(this).get(modelClass) as VM
        }
    }

    private fun initObserve() {
        if (mViewModel == null) return
        // 监听当前ViewModel中 showDialog和error的值
        mViewModel?.getShowDialog(this, Observer<DialogBean> {
            if (it.isShow) {
                showDialog()
            } else {
                dismissDialog()
            }
        })
        // 错误
        mViewModel?.getThrowable(this, Observer {
            showThrowable(it)
        })
        // 提示
        mViewModel?.getToast(this, Observer {
            ToastUtils.showShort(it)
        })
    }

    /**
     * 返回布局界面
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 初始化数据
     */
    protected abstract fun initData(savedInstanceState: Bundle?)

    /**
     * 初始化视图
     */
    protected abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 加载数据
     */
    protected abstract fun loadData(savedInstanceState: Bundle?)

    /**
     * 今日头条适配方式
     *
     * @return
     */
    override fun getResources(): Resources? {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 1080)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding?.unbind()
        ActivityUtil.getInstance()?.removeActivity(this)
    }

    /**
     * 显示用户等待框
     */
    protected open fun showDialog() {
        if (mLoadingDialog != null && !mLoadingDialog!!.isShowing) {
            mLoadingDialog?.show()
        }
    }

    /**
     * 隐藏等待框
     */
    protected open fun dismissDialog() {
        if (mLoadingDialog != null && mLoadingDialog!!.isShowing) {
            mLoadingDialog?.dismiss()
        }
    }

    /**
     * ViewModel层发生了错误，给出通知
     */
    protected abstract fun showToast(obj: Any?)

    /**
     * 网络错误问题
     */
    private fun showThrowable(throwable: Throwable?) {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort(R.string.result_network_error)
            return
        }
        return when (throwable) {
            is ConnectException -> {
                ToastUtils.showShort(R.string.result_server_error)
            }
            is SocketTimeoutException -> {
                ToastUtils.showShort(R.string.result_server_timeout)
            }
            is JSONException -> {
                ToastUtils.showShort(R.string.result_json_error)
            }
            else -> {
                ToastUtils.showShort(R.string.result_empty_error)
            }
        }
    }

}