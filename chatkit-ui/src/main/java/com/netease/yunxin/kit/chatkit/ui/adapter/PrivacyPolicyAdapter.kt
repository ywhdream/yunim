package com.netease.yunxin.kit.chatkit.ui.adapter

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import com.netease.yunxin.kit.chatkit.ui.R
import com.netease.yunxin.kit.chatkit.ui.permission.PermissionObject
import com.netease.yunxin.kit.chatkit.ui.util.ViewHolder


class PrivacyPolicyAdapter (context: Context, mDatas: List<PermissionObject>) : CommonAdapter<PermissionObject>(context, R.layout.item_privacy_policy_sub_message, mDatas) {
    override fun convert(holder: ViewHolder?, permissionObject: PermissionObject?, position: Int) {
        holder?.let { mHolder ->
            mHolder.getView<AppCompatTextView>(R.id.tvTitlePersonalData).text = "${permissionObject?.permission}"
            mHolder.getView<AppCompatTextView>(R.id.tvMessagePersonalData).text = "${permissionObject?.customMessage}"
        }
    }
}