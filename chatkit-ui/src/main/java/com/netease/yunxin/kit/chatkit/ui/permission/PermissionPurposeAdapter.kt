package com.netease.yunxin.kit.chatkit.ui.permission

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.netease.yunxin.kit.chatkit.ui.R
import com.netease.yunxin.kit.chatkit.ui.adapter.CommonAdapter
import com.netease.yunxin.kit.chatkit.ui.util.ViewHolder


/**
 * @Author KC Chen
 * @Date 14/12/2023 - 4:36pm
 * @Description
 */
class PermissionPurposeAdapter(context: Context, mDatas: List<PermissionObject>) : CommonAdapter<PermissionObject>(context, R.layout.item_permission_purpose, mDatas) {

    override fun convert(holder: ViewHolder?, permissionObject: PermissionObject?, position: Int) {
        holder?.let { mHolder ->
            mHolder.itemView.isSelected = permissionObject?.isGranted == true

            mHolder.getView<AppCompatTextView>(R.id.tvMessage).text = "${position + 1}. ${permissionObject?.customMessage ?: ""}"

            mHolder.getView<AppCompatImageView>(R.id.ivIcon).setImageResource(
                if(permissionObject?.isGranted == true) {
                    R.drawable.ic_permission_purpose_granted
                } else {
                    R.drawable.ic_permission_purpose_not_granted
                }
            )
        }
    }
}