package com.netease.yunxin.kit.chatkit.ui.util

import androidx.annotation.StyleRes
import com.netease.yunxin.kit.chatkit.ui.R


object DialogUtils {
    var style = R.style.EasyDialogStyle
        private set
    var listStyle = R.style.EasyListDialogStyle
        private set

    fun initStyle(@StyleRes style: Int) {
        DialogUtils.style = style
    }

    fun initListStyle(@StyleRes style: Int) {
        listStyle = style
    }
}