package com.netease.yunxin.kit.chatkit.ui.permission

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.netease.nim.highavailable.LogUtils
import com.netease.yunxin.kit.chatkit.ui.util.ConvertUtils
import io.reactivex.rxjava3.observers.DisposableObserver


/**
 * @Describe 处理服务器数据 适用 ＲＥＳＥＴＦＵＬ　ＡＰＩ
 * @Author Jungle68
 * @Date 2017/5/18
 * @Contact master.jungle68@gmail.com
 */

abstract class PermissionBaseSubscriberForV2<T> : DisposableObserver<T>() {
    /**
     * onNext 执行后执行
     */
    override fun onComplete() {}
    override fun onError(e: Throwable) {
        Log.d(TAG, "onError: ${e.message}")
    }

    /**
     * 处理数据，按照 resutful api 要求，204 不反回数据
     *
     * @param data
     */
    override fun onNext(data: T) {
        // 数据接收成功
        onSuccess(data)
    }

    /**
     * 处理错误
     *
     * @param e
     */
    private fun handleError(e: Throwable) {
        e.printStackTrace()
        onException(e)
    }

    /**
     * 服务器正确处理返回正确数据
     *
     * @param data 正确的数据
     */
    protected abstract fun onSuccess(data: T)

    /**
     * 服务器正确接收到请求，主动返回错误状态以及数据
     *
     * @param message 错误信息
     * @param code
     */
    protected fun onFailure(message: String, code: Int) {
        onComplete()
    }

    /**
     * 系统级错误，网络错误，系统内核错误等
     *
     * @param throwable
     */
    protected fun onException(throwable: Throwable?) {
        onComplete()
    }

    protected fun handleErrorBodyString(code: Int, body: String?): Boolean {
        return false
    }


    companion object {
        private const val TAG = "PermissionBaseSubscriberForV2"
    }
}