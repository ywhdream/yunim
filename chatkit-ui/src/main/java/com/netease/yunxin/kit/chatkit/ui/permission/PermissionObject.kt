package com.netease.yunxin.kit.chatkit.ui.permission

/**
 * @Author KC Chen
 * @Date 14/12/2023 - 12:18 pm
 * @Description
 */
data class PermissionObject (
    val permission: String,
    val isGranted: Boolean = false,
    val customMessage: String = "",
)