package com.netease.yunxin.kit.chatkit.ui.model

/**
 * @Author: Yangwenhao
 * @CreateDate: 2023/1/13
 * @Description: 音视频常量
 */
object NimVideoConstant {
    //切换呼叫通知类型
    const val TO_AUDIO = "toAudio"//音频
    const val TO_VIDEO = "toVideo" //视频
    const val ACCEPT_TO_VIDEO = "agreeToVideo" //同意视频切换
    const val REJECT_TO_VIDEO = "rejectToVideo" //拒绝视频切换
    const val IS_CALL_BUSY = "isCallBusy" //忙线拒绝接听

    //呼叫类型
    const val P2P_CALL = "p2pCall" //一对一呼叫
    const val TEAM_CALL = "teamCall" //群呼

    //音视频通话类型
    const val NO_RESPONSE = 0 //对方无人接听
    const val MISS = 1//未接电话
    const val BILL = 2//电话回单
    const val REJECT = 3//对方拒接电话
    const val ACCEPT = 4//对方接听电话


    //事件常量
    const val EVENT_CALL = "event_call"//对方拒接电话


}