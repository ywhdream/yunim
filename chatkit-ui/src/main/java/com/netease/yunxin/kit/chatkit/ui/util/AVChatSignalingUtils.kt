package com.netease.yunxin.kit.chatkit.ui.util

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.avsignalling.event.InvitedEvent
import com.netease.nimlib.sdk.v2.avsignalling.V2NIMSignallingListener
import com.netease.nimlib.sdk.v2.avsignalling.V2NIMSignallingService
import com.netease.nimlib.sdk.v2.avsignalling.enums.V2NIMSignallingEventType
import com.netease.nimlib.sdk.v2.avsignalling.model.V2NIMSignallingEvent
import com.netease.nimlib.sdk.v2.avsignalling.model.V2NIMSignallingRoomInfo
import com.netease.yunxin.kit.chatkit.ui.YunxinImApplication
import com.netease.yunxin.kit.chatkit.ui.avtivity.AVChatActivity.Companion.incomingCall
import com.netease.yunxin.kit.chatkit.ui.model.NimCallTypeReq
import com.netease.yunxin.kit.chatkit.ui.model.NimVideoConstant
import com.netease.yunxin.kit.chatkit.ui.notification.AVChatNotification


/**
 * @Author: Yangwenhao
 * @CreateDate: 2023/3/31
 * @Description: 信令事件回调
 */
object AVChatSignalingUtils {
    var mInvitedEvent: V2NIMSignallingEvent? = null
    var activityCount: Int = 0

    @JvmStatic
    fun offlineObserver(context: Context) {
        NIMClient.getService(V2NIMSignallingService::class.java)
            .addSignallingListener(object : V2NIMSignallingListener {
                override fun onOnlineEvent(event: V2NIMSignallingEvent) {
                    val activityAVChatActivity =
                        StringUtil.isActivityForeground(
                            context,
                            "com.netease.yunxin.kit.chatkit.ui.avtivity.AVChatActivity"
                        )

//                    val activityTeamAVChatActivity =
//                        YunxinImApplication().mYunxinImApplication?.getContext()?.let {
//                            StringUtil.isActivityForeground(
//                                it,
//                                "com.togl.rewardslink.im.avchatkit.teamavchat.activity.TeamAVChatActivity"
//                            )
//                        }


                    when (event.eventType) {
                        V2NIMSignallingEventType.V2NIM_SIGNALLING_EVENT_TYPE_INVITE -> {//邀请
                            if (!activityAVChatActivity) {
                                if (activityCount > 0) {
                                    val channelExtension = event.channelInfo.channelExtension

                                    val nimCallTypeReq = Gson().fromJson(
                                        channelExtension,
                                        NimCallTypeReq::class.java
                                    )

                                    if (nimCallTypeReq.type == NimVideoConstant.P2P_CALL) {//一对一私聊
                                        incomingCall(
                                            context,
                                            event.channelInfo.creatorAccountId,
                                            event.channelInfo.channelType.value,
                                            true,
                                            event.channelInfo.channelId,
                                            event.requestId,
                                            event.channelInfo.channelName
                                        )
                                    }
                                    mInvitedEvent = null
                                } else {
                                    mInvitedEvent = event

                                    if (UserPreferences.getAVNotificationToggle()) {
                                        val generalNotifier =
                                            AVChatNotification(YunxinImApplication().mYunxinImApplication?.getContext())
                                        generalNotifier.init(
                                            event.inviteeAccountId,
                                            event.channelInfo.creatorAccountId
                                        )
                                        generalNotifier.activeJustCallNotification()
                                    }
                                }
                            }
                        }

                        V2NIMSignallingEventType.V2NIM_SIGNALLING_EVENT_TYPE_CANCEL_INVITE -> {//取消

                        }

                        else -> {}

                    }


                }

                override fun onOfflineEvent(events: MutableList<V2NIMSignallingEvent>) {
                    val event = events[0]
                    if (event.eventType == V2NIMSignallingEventType.V2NIM_SIGNALLING_EVENT_TYPE_INVITE) {//邀请事件
                        val channelExtension = event.channelInfo.channelExtension

                        val nimCallTypeReq = Gson().fromJson(
                            channelExtension,
                            NimCallTypeReq::class.java
                        )


                        if (nimCallTypeReq.type == NimVideoConstant.P2P_CALL) { //一对一私聊
//                            AVChatActivity.incomingCall(
//                                YunxinImApplication().mYunxinImApplication?.getContext(),
//                                event.channelInfo.channelType.value,
//                                event.inviterAccountId,
//                                event
//                            )
//                            YunxinImApplication().mYunxinImApplication?.getContext()?.let {
//                                AVChatActivity.incomingCall(
//                                    it,
//                                    event.channelInfo.channelType.value,
//
//
//                                )
//                            }
                        } else { //群呼
//                            TeamAVChatActivity.startActivity(
//                                YunxinImApplication().mYunxinImApplication?.getContext(),
//                                true,
//                                nimCallTypeReq.data.teamId,
//                                event.channelInfo.channelName,
//                                nimCallTypeReq.data.members,
//                                event.channelInfo.channelName,
//                                event,
//                                "",
//                                null,
//                                null
//                            )
                        }


                    } else if (event.eventType == V2NIMSignallingEventType.V2NIM_SIGNALLING_EVENT_TYPE_CANCEL_INVITE) {//取消邀请事件

                    }

                }

                override fun onMultiClientEvent(event: V2NIMSignallingEvent?) {

                }

                override fun onSyncRoomInfoList(channelRooms: MutableList<V2NIMSignallingRoomInfo>?) {

                }
            })
    }


}