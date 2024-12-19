package com.netease.yunxin.kit.chatkit.ui

import android.app.Application
import com.netease.lava.nertc.sdk.NERtcOption
import com.netease.nimlib.sdk.avsignalling.constant.ChannelType
import com.netease.nimlib.sdk.v2.V2NIMError
import com.netease.nimlib.sdk.v2.auth.V2NIMLoginListener
import com.netease.nimlib.sdk.v2.auth.enums.V2NIMLoginClientChange
import com.netease.nimlib.sdk.v2.auth.enums.V2NIMLoginStatus
import com.netease.nimlib.sdk.v2.auth.model.V2NIMKickedOfflineDetail
import com.netease.nimlib.sdk.v2.auth.model.V2NIMLoginClient
import com.netease.yunxin.kit.alog.ALog
import com.netease.yunxin.kit.call.p2p.NECallEngine
import com.netease.yunxin.kit.call.p2p.model.NECallInitRtcMode
import com.netease.yunxin.kit.call.p2p.model.NEInviteInfo
import com.netease.yunxin.kit.chatkit.ui.notification.AVChatKit
import com.netease.yunxin.kit.chatkit.ui.perference.PreferencesHelper
import com.netease.yunxin.kit.chatkit.ui.util.CustomCallOrderProvider
import com.netease.yunxin.kit.chatkit.ui.util.DataUtils
import com.netease.yunxin.kit.corekit.im2.IMKitClient
import com.netease.yunxin.kit.corekit.im2.IMKitClient.addLoginListener
import com.netease.yunxin.kit.corekit.im2.IMKitClient.currentUser
import com.netease.yunxin.nertc.ui.CallKitNotificationConfig
import com.netease.yunxin.nertc.ui.CallKitUI.destroy
import com.netease.yunxin.nertc.ui.CallKitUI.init
import com.netease.yunxin.nertc.ui.CallKitUIOptions

/**
 * @Author: YWH
 * @Date: 2024/11/29 16:25
 * @Description: 类的描述信息
 */
open class YunxinImApplication : Application() {

    var mYunxinImApplication: YunxinImApplication? = null

    override fun onCreate() {
        super.onCreate()
        mYunxinImApplication = this
        // 部分Android机型在页面进入onResume前启动其他页面会取消当前页面流程，避免组件初始化后立即展示来电页面将初始化的逻辑滞后
//        if (!init) {
//            configCallKit()
//        }
        PreferencesHelper.setContext(this)
        AVChatKit.setContext(this)
//        AVChatSignalingUtils.offlineObserver()

    }



    fun getContext(): YunxinImApplication? {
        return mYunxinImApplication
    }


    // 初始化音视频通话组件
     fun configCallKit() {
        val options: CallKitUIOptions = CallKitUIOptions.Builder() // 必要：音视频通话 sdk appKey，用于通话中使用
            .rtcAppKey(DataUtils.readAppKey(this)) // 必要：当前用户 AccId
            .currentUserAccId(IMKitClient.account().toString()) // 通话接听成功的超时时间单位 毫秒，默认30s
            .timeOutMillisecond(30 * 1000L) // 此处为 收到来电时展示的 notification 相关配置，如图标，提示语等。
            .notificationConfigFetcher { invitedInfo: NEInviteInfo ->
                val currentUser = currentUser()
                val content = (
                        (if (currentUser != null) currentUser.name else invitedInfo.callerAccId)
                                + (if (invitedInfo.callType == ChannelType.AUDIO.value
                        ) "【语音通话】"
                        else "【视频通话】"))
                ALog.d("=======$content")
                CallKitNotificationConfig(R.drawable.ic_logo, null, null, content)
            } // 收到被叫时若 app 在后台，在恢复到前台时是否自动唤起被叫页面，默认为 true
            .resumeBGInvitation(true) // 请求 rtc token 服务，若非安全模式则不需设置(V1.8.0版本之前需要配置，V1.8.0及之后版本无需配置)
            //.rtcTokenService((uid, callback) -> requestRtcToken(appKey, uid, callback)) // 自己实现的 token 请求方法
            // 设置初始化 rtc sdk 相关配置，按照所需进行配置
            .rtcSdkOption(NERtcOption()) // 呼叫组件初始化 rtc 范围，NECallInitRtcMode.GLOBAL-全局初始化，
            // NECallInitRtcMode.IN_NEED-每次通话进行初始化以及销毁，全局初始化有助于更快进入首帧页面，
            // 当结合其他组件使用时存在rtc初始化冲突可设置NECallInitRtcMode.IN_NEED
            // 或当结合其他组件使用时存在rtc初始化冲突可设置NECallInitRtcMode.IN_NEED_DELAY_TO_ACCEPT
            .initRtcMode(NECallInitRtcMode.GLOBAL)
            .build()
        // 设置自定义话单消息发送
        NECallEngine.sharedInstance().setCallRecordProvider(CustomCallOrderProvider())
        // 若重复初始化会销毁之前的初始化实例，重新初始化
        init(applicationContext, options)
        addLoginListener(object : V2NIMLoginListener {
            override fun onLoginStatus(status: V2NIMLoginStatus) {
                if (status == V2NIMLoginStatus.V2NIM_LOGIN_STATUS_LOGOUT) {
                    destroy()
                }
            }

            override fun onLoginFailed(error: V2NIMError) {
            }

            override fun onKickedOffline(detail: V2NIMKickedOfflineDetail) {
            }

            override fun onLoginClientChanged(
                change: V2NIMLoginClientChange,
                clients: List<V2NIMLoginClient>
            ) {
            }
        })
    }

}
