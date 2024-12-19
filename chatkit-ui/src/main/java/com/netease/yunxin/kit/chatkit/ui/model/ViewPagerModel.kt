package com.netease.yunxin.kit.chatkit.ui.model

import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment

data class ViewPagerModel(
    val fragment: Fragment,
    val title: String?,
    @DrawableRes val iconDrawable: Int?,
    val tag: String? = null
) {
    constructor(fragment: Fragment, title: String?, iconDrawable: Int?) : this(
        fragment,
        title,
        iconDrawable,
        ""
    )
}