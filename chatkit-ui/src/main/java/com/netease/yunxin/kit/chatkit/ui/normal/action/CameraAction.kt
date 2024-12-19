package com.netease.yunxin.kit.chatkit.ui.normal.action

import android.view.View
import com.netease.yunxin.kit.chatkit.ui.R
import com.netease.yunxin.kit.chatkit.ui.view.input.ActionConstants
import com.netease.yunxin.kit.common.ui.action.ActionItem


class CameraAction : ActionItem(
    ActionConstants.ACTION_TYPE_CAMERA,
    R.drawable.chat_bottom_panel_camera,
    R.string.choose_from_camera
) {
    override fun onClick(view: View?) {
        super.onClick(view)
    }

}