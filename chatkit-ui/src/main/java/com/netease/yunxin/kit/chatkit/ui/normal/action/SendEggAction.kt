package com.netease.yunxin.kit.chatkit.ui.normal.action

import com.netease.yunxin.kit.chatkit.ui.util.IMSendGiftUtil
import com.netease.yunxin.kit.chatkit.ui.view.input.ActionConstants.ACTION_TYPE_EGG
import com.netease.yunxin.kit.common.ui.action.ActionItem

/**
 * @Author: YWH
 * @Date: 2024/11/28 15:45
 * @Description: 类的描述信息
 */
class SendEggAction : ActionItem(
    ACTION_TYPE_EGG,
    IMSendGiftUtil.getPanelGiftImageKey(),
    IMSendGiftUtil.getPanelGiftStringKey()
) {

}
