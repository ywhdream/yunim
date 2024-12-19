package com.netease.yunxin.kit.chatkit.ui.permission

import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.netease.yunxin.kit.chatkit.ui.R
import com.netease.yunxin.kit.chatkit.ui.databinding.DialogPermissionPurposeInformationBinding
import com.netease.yunxin.kit.chatkit.ui.util.ConvertUtils
import com.netease.yunxin.kit.chatkit.ui.util.DeviceUtils
import com.netease.yunxin.kit.chatkit.ui.view.DialogHelper
import com.netease.yunxin.kit.chatkit.ui.view.LinearDecoration

/**
 * @Author KC Chen
 * @Date 15/12/2023 - 4:44pm
 * @Description
 */
fun showPermissionsPurposeDialog(
    mContext: Context,
    haveNeverAskAgainPermission: Boolean,
    linkSet: List<Link>?,
    permissionList: ArrayList<PermissionObject>,
    dialogListener: DialogHelper.DialogListener?,
    agreeClickListener: View.OnClickListener?,
) {
//    val inflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//    val dialogView: DialogPermissionPurposeInformationBinding = DialogPermissionPurposeInformationBinding.inflate(inflater)
    val inflater: LayoutInflater =
        mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val dialogView: View = inflater.inflate(R.layout.dialog_permission_purpose_information, null)


    dialogView.run {

        findViewById<TextView>(R.id.tvDialogTitle).apply {
            text = mContext.getString(R.string.permission_purpose_title)
            setTextColor(ContextCompat.getColor(context, R.color.black))
        }

        findViewById<RecyclerView>(R.id.rvPermissionPurposeList).apply {
            layoutManager = LinearLayoutManager(mContext)
            //设置Item的间隔
            addItemDecoration(
                LinearDecoration(
                    0,
                    mContext.resources.getDimensionPixelOffset(R.dimen.dimen_10_dp),
                    0,
                    0,
                    true
                )
            )
            setHasFixedSize(true)
            setItemViewCacheSize(10)
            isDrawingCacheEnabled = true
            drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
            adapter = PermissionPurposeAdapter(mContext, permissionList)
        }

        findViewById<AppCompatTextView>(R.id.tvReadFullVersion).apply {
            val termOfServiceText = mContext.getString(R.string.permission_purpose_terms_of_use)
            val privacyPolicyText = mContext.getString(R.string.permission_purpose_privacy_policy)
            val description = mContext.getString(
                R.string.permission_purpose_for_detail_description,
                termOfServiceText,
                privacyPolicyText
            )
            text = description
            gravity = Gravity.NO_GRAVITY
            visibility = View.VISIBLE

            if (linkSet?.isNotEmpty() == true) {
                ConvertUtils.stringLinkConvert(
                    this,
                    linkSet,
                    false
                )
            } else {
                visibility = View.GONE
            }
        }


        findViewById<Button>(R.id.btnPositive).apply {
            text =
                mContext.getString(if (haveNeverAskAgainPermission) R.string.setting_permission else R.string.agree)
            setOnClickListener {
                if (haveNeverAskAgainPermission) {
                    DeviceUtils.openAppDetail(mContext)
                    findViewById<Button>(R.id.btnNegative).performClick()
                } else {
                    agreeClickListener?.onClick(this)
                }
                DialogHelper.getInstance().closeDialog()
            }
        }

        findViewById<Button>(R.id.btnNegative).apply {
            text = mContext.getString(R.string.disagree)
            setOnClickListener {
                dialogListener?.onNegativeButtonClicked(
                    DialogHelper.getInstance(),
                    object : DialogInterface {
                        override fun cancel() {}

                        override fun dismiss() {}
                    },
                    0,
                    -1
                )
                DialogHelper.getInstance().closeDialog()
            }
        }

        DialogHelper.getInstance().showCustomDialog(
            mContext,
            this,
            null,
            null,
            false
        )
    }
}