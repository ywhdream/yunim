package com.netease.yunxin.kit.chatkit.ui.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.netease.yunxin.kit.chatkit.ui.R

object IMSendGiftUtil {

    private const val isEggType: Boolean = false

    @JvmStatic
    @DrawableRes
    fun getPanelGiftImageKey(): Int {
        return if (isEggType) R.drawable.ic_eggs else R.drawable.ic_redpacket_im
    }

    @JvmStatic
    @StringRes
    fun getPanelGiftStringKey(): Int {
        return if (isEggType) R.string.input_panel_egg else R.string.input_panel_redpacket
    }

    @JvmStatic
    @DrawableRes
    fun getGiftImageKey(): Int {
        return if (isEggType) R.drawable.message_eggs_normal else R.drawable.ic_redpacket
    }

    @JvmStatic
    @DrawableRes
    fun getGiftBigImageKey(): Int {
        return if (isEggType) R.drawable.message_eggs_normal else R.drawable.ic_redpacket_big_2
    }

    @JvmStatic
    @DrawableRes
    fun getGiftBigImageKey(isAllowToClaim: Boolean = false): Int {
        return if (isAllowToClaim && !isEggType) getGiftBigImageKey() else R.drawable.ic_redpacket_big_not_allow_to_claim
    }

    @JvmStatic
    @StringRes
    fun getReceivedGiftStringKey(): Int {
        return if (isEggType) R.string.viewholder_egg_break else R.string.viewholder_redpacket_open
    }

    @JvmStatic
    @StringRes
    fun getGiftDetailStringKey(): Int {
        return if (isEggType) R.string.viewholder_egg_detail else R.string.viewholder_redpacket_detail
    }

    @JvmStatic
    @StringRes
    fun getDisclaimerStringKey(): Int {
        return if (isEggType) R.string.viewholder_egg_refund else R.string.viewholder_redpacket_refund
    }
}