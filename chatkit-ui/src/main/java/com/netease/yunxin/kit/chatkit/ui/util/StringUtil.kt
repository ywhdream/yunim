package com.netease.yunxin.kit.chatkit.ui.util

import android.app.ActivityManager
import android.content.Context
import android.text.TextUtils

object StringUtil {

    /**
     * 判断某个界面是否在前台
     */
//    fun isActivityForeground(context: Context?, className: String?): Boolean {
//        if (TextUtils.isEmpty(className)) {
//            return false
//        }
//        val am = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        val activityList = am.getRunningTasks(1)
//        if (activityList.size > 0) {
//            val cpns = activityList[0].topActivity
//            return className == cpns?.className
//        }
//        return false
//    }

    @JvmStatic
    fun isEmpty(input: String?): Boolean {
        return TextUtils.isEmpty(input)
    }


    fun isActivityForeground(context: Context, targetActivityClass: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = activityManager.getRunningTasks(1)
        if (runningTasks.isNotEmpty()) {
            val topActivity = runningTasks[0].topActivity
            return topActivity?.className == targetActivityClass
        }
        return false
    }
}