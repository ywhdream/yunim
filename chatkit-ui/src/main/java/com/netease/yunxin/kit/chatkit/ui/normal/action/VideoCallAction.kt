package com.netease.yunxin.kit.chatkit.ui.normal.action

import android.view.View
import com.netease.yunxin.kit.chatkit.ui.R
import com.netease.yunxin.kit.chatkit.ui.view.input.ActionConstants.ACTION_TYPE_VIDEO_CALL_ACTION
import com.netease.yunxin.kit.common.ui.action.ActionItem


class VideoCallAction : ActionItem(
    ACTION_TYPE_VIDEO_CALL_ACTION,
    R.drawable.chat_bottom_panel_video_call,
    R.string.msg_type_video_call
) {

    override fun onClick(view: View?) {
        super.onClick(view)
    }
}