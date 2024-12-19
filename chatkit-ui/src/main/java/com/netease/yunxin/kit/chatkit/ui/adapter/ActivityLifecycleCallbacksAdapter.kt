package com.netease.yunxin.kit.chatkit.ui.adapter

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.avsignalling.model.ChannelBaseInfo
import com.netease.nimlib.sdk.uinfo.UserService
import com.netease.yunxin.kit.chatkit.ui.avtivity.AVChatActivity.Companion.incomingCall
import com.netease.yunxin.kit.chatkit.ui.model.NimCallTypeReq
import com.netease.yunxin.kit.chatkit.ui.model.NimVideoConstant
import com.netease.yunxin.kit.chatkit.ui.util.AVChatSignalingUtils.activityCount
import com.netease.yunxin.kit.chatkit.ui.util.AVChatSignalingUtils.mInvitedEvent

/**
 * @Author: Yangwenhao
 * @CreateDate: 2023/3/31
 * @Description: 监听APP前后台的操作
 */

class ActivityLifecycleCallbacksAdapter : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        activityCount++

        if (mInvitedEvent != null) {
            val channelExtension = mInvitedEvent?.channelInfo?.channelExtension

            val nimCallTypeReq = Gson().fromJson(
                channelExtension,
                NimCallTypeReq::class.java
            )
            Log.d("ywh", "mInvitedEvent: ${mInvitedEvent?.channelInfo?.channelType?.value}")

            if (nimCallTypeReq.type == NimVideoConstant.P2P_CALL) {//一对一私聊

                incomingCall(
                    activity,
                    mInvitedEvent?.channelInfo?.creatorAccountId,
                    mInvitedEvent?.channelInfo?.channelType?.value,
                    true,
                    mInvitedEvent?.channelInfo?.channelId.toString(),
                    mInvitedEvent?.requestId.toString(),
                    mInvitedEvent?.channelInfo?.channelName.toString()
                )


            } else { //群呼
//                val channelBaseInfo: ChannelBaseInfo = mInvitedEvent!!.channelBaseInfo
//                val dataRequest = nimCallTypeReq.getData()
//                TeamAVChatActivity.startActivity(
//                    BaseApplication.getContext(), true, dataRequest.teamId,
//                    channelBaseInfo.channelName, dataRequest.members,
//                    channelBaseInfo.channelName, mInvitedEvent, "", null, null
//                )
            }
            mInvitedEvent = null
        }

    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
        activityCount--

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }


}
