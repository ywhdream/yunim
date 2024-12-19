package com.netease.yunxin.kit.chatkit.ui.permission

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.netease.yunxin.kit.chatkit.ui.R
import com.netease.yunxin.kit.chatkit.ui.adapter.PrivacyPolicyAdapter
import com.netease.yunxin.kit.chatkit.ui.util.ConvertUtils
import com.netease.yunxin.kit.chatkit.ui.view.DialogHelper
import com.netease.yunxin.kit.chatkit.ui.view.LinearDecoration


fun showPrivacyPolicyDialog(
    mContext: Context,
    dialogListener: DialogListener?
) {
    val dialogView = View.inflate(mContext, R.layout.dialog_common_confirmation_vertical_new2, null)

    dialogView.run {
        val ibCloseButton = findViewById<ImageButton>(R.id.ibCloseButton)

        val tvDialogTitle = findViewById<TextView>(R.id.tvDialogTitle)
        val tvDialogMessage = findViewById<TextView>(R.id.tvDialogMessage)
        val rvDialogSubMessage = findViewById<RecyclerView>(R.id.rvDialogSubMessage)

        val checkBox = findViewById<CheckBox>(R.id.checkbox_term)
        val btnPositive = findViewById<Button>(R.id.btnPositive)
        val btnNegative = findViewById<TextView>(R.id.btnNegative)

        ibCloseButton.visibility = View.GONE
        checkBox.visibility = View.GONE
        btnPositive.isEnabled = true
        btnPositive.background =
            ContextCompat.getDrawable(context, R.drawable.solid_bg_blue_xr)

        tvDialogTitle.apply {
            visibility = View.VISIBLE
            text = mContext.getString(R.string.rw_dialog_privacy_policy_title)
        }

        tvDialogMessage.apply {
            val param = layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0, 0, 0, 0)
            layoutParams = param

            gravity = Gravity.NO_GRAVITY
            visibility = View.VISIBLE
            text = mContext.getString(
                R.string.rw_dialog_privacy_policy_desc,
                context.getString(R.string.setting_term_of_use),
                context.getString(R.string.privacy_policy)
            )
            ConvertUtils.stringLinkConvert(
                this,
                setLinks(
                    context,
                    context.getString(R.string.setting_term_of_use),
                    context.getString(R.string.privacy_policy),
                    false
                ),
                false
            )
        }

        val collectedDataList = ArrayList<PermissionObject>()
        collectedDataList.add(
            PermissionObject(
                context.getString(R.string.rw_collected_data_mobile_title),
                false,
                context.getString(R.string.rw_collected_data_mobile_msg)
            )
        )

        rvDialogSubMessage.apply {
            visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(mContext)
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
            adapter = PrivacyPolicyAdapter(mContext, collectedDataList)
        }

        checkBox.apply {
            this.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    btnPositive.isEnabled = true
                    btnPositive.background =
                        ContextCompat.getDrawable(context, R.drawable.solid_bg_blue_xr)
                } else {
                    btnPositive.isEnabled = false
                    btnPositive.background =
                        ContextCompat.getDrawable(context, R.drawable.solid_bg_light_grey_xr)
                }
            }
        }

        btnPositive.apply {
            btnPositive.text = mContext.getString(R.string.agree)
            setOnClickListener {
                dialogListener?.onConfirmClick()
                DialogHelper.getInstance().closeDialog()
            }
        }

        btnNegative.apply {
            visibility = View.VISIBLE
            btnNegative.text = mContext.getString(R.string.disagree)
            setOnClickListener {
                dialogListener?.onCancelClick()
                DialogHelper.getInstance().closeDialog()
            }
        }

        DialogHelper.getInstance().showCustomDialog(
            mContext,
            this,
            ibCloseButton,
            null,
            false
        )
    }
}

fun showInformationDialog(
    mContext: Context,
    dialogListener: DialogListener?,
    title: String,
    message: String
) {
    val dialogView = View.inflate(mContext, R.layout.dialog_common_confirmation_vertical_new2, null)

    dialogView.run {
        val ibCloseButton = findViewById<ImageButton>(R.id.ibCloseButton)

        val tvDialogTitle = findViewById<TextView>(R.id.tvDialogTitle)
        val tvDialogMessage = findViewById<TextView>(R.id.tvDialogMessage)

        val checkBox = findViewById<CheckBox>(R.id.checkbox_term)
        val btnPositive = findViewById<Button>(R.id.btnPositive)
        val btnNegative = findViewById<TextView>(R.id.btnNegative)

        btnNegative.visibility = View.GONE
        ibCloseButton.visibility = View.GONE
        checkBox.visibility = View.GONE
        btnPositive.isEnabled = true
        btnPositive.background = ContextCompat.getDrawable(context, R.drawable.solid_bg_blue_xr)

        tvDialogTitle.apply {
            visibility = View.VISIBLE
            text = title
        }

        tvDialogMessage.apply {
            visibility = View.VISIBLE
            text = message
        }

        btnPositive.apply {
            btnPositive.text = mContext.getString(R.string.text_got_it)
            setOnClickListener {
                dialogListener?.onConfirmClick()
                DialogHelper.getInstance().closeDialog()
            }
        }

        DialogHelper.getInstance().showCustomDialog(
            mContext,
            this,
            ibCloseButton,
            null,
            false
        )
    }
}

fun showMsgRequetDialog(
    mContext: Context,
    dialogListener: DialogListener?,
    title: String,
    message: String,
    isAccept: Boolean
) {
    val dialogView = View.inflate(mContext, R.layout.dialog_msg_requests_vertical, null)

    dialogView.run {
        val ibCloseButton = findViewById<ImageButton>(R.id.ibCloseButton)

        val tvDialogTitle = findViewById<TextView>(R.id.tvDialogTitle)
        val tvDialogMessage = findViewById<TextView>(R.id.tvDialogMessage)

        val btnPositive = findViewById<Button>(R.id.btnPositive)
        val btnNegative = findViewById<TextView>(R.id.btnNegative)

        ibCloseButton.visibility = View.GONE

        tvDialogTitle.apply {
            visibility = View.VISIBLE
            text = title
        }

        tvDialogMessage.apply {
            visibility = View.VISIBLE
            text = message
        }

        btnPositive.apply {
            if (isAccept) {
                btnPositive.background =
                    ContextCompat.getDrawable(context, R.drawable.bg_rounded_blue_53bcff_10dp)
                btnPositive.text = mContext.getString(R.string.accept_session)
            } else {
                btnPositive.background =
                    ContextCompat.getDrawable(context, R.drawable.bg_rounded_red_ed2121)
                btnPositive.text = mContext.getString(R.string.reject)
            }

            setOnClickListener {
                dialogListener?.onConfirmClick()
                DialogHelper.getInstance().closeDialog()
            }

        }

        btnNegative.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                dialogListener?.onCancelClick()
                DialogHelper.getInstance().closeDialog()
            }

        }

        DialogHelper.getInstance().showCustomDialog(
            mContext,
            this,
            ibCloseButton,
            null,
            false
        )
    }
}

fun showJoinGroupConfirmationDialog(
    mContext: Context,
    dialogListener: DialogListener?,
    title: String,
    message: String,
    imgUrl: String?
) {
    val dialogView = View.inflate(mContext, R.layout.dialog_common_confirmation_vertical_new3, null)

    dialogView.run {
        val tvDialogTitle = dialogView.findViewById<TextView>(R.id.tvDialogTitle)
        val tvDialogMessage = dialogView.findViewById<TextView>(R.id.tvDialogMessage)
        val btnNegative = dialogView.findViewById<TextView>(R.id.btnNegative)
        val btnPositive = dialogView.findViewById<Button>(R.id.btnPositive)
        val ivImage = dialogView.findViewById<AppCompatImageView>(R.id.ivDialogImage)

        tvDialogTitle.visibility = View.GONE
        btnNegative.visibility = View.VISIBLE
        btnPositive.isEnabled = true
        btnPositive.background = ContextCompat.getDrawable(context, R.drawable.solid_bg_blue_xr)

        tvDialogMessage.apply {
            text = message
            setTextColor(ContextCompat.getColor(context, R.color.black))
        }

        btnPositive.apply {
            btnPositive.text = mContext.getString(R.string.text_request_to_join)
            setOnClickListener {
                dialogListener?.onConfirmClick()
                DialogHelper.getInstance().closeDialog()
            }

        }

        btnNegative.apply {
            visibility = View.VISIBLE
            btnNegative.text = mContext.getString(R.string.text_not_now)
            setOnClickListener {
                dialogListener?.onCancelClick()
                DialogHelper.getInstance().closeDialog()
            }

        }

        ivImage.visibility = View.VISIBLE
        Glide.with(context)
            .load(imgUrl ?: R.drawable.img_avatar)
            .placeholder(R.drawable.img_avatar)
            .error(R.drawable.img_avatar)
            .dontAnimate()
            .into(ivImage)

        DialogHelper.getInstance().showCustomDialog(
            mContext,
            DialogHelper.SIMPLE_OK,
            this,
            false,
            null,
            null,
            null,
            android.R.style.Theme_Black_NoTitleBar_Fullscreen
        )
    }
}

interface DialogListener {
    fun onConfirmClick()
    fun onCancelClick()
}

fun getPermissionPurposeLinks(
    context: Context
): List<Link> {
    return setLinks(
        context,
        context.getString(R.string.permission_purpose_terms_of_use),
        context.getString(R.string.permission_purpose_privacy_policy),
        true
    )
}

private fun setLinks(
    context: Context,
    termOfUseText: String,
    privacyPolicyText: String,
    isUnderLine: Boolean
): List<Link> {
    val links: MutableList<Link> = ArrayList()

    val replyNameLink = Link(termOfUseText)
        .setTextColor(
            ContextCompat.getColor(
                context,
                R.color.themeColor_red
            )
        )
        .setTextColorOfHighlightedLink(
            ContextCompat.getColor(
                context,
                R.color.general_for_hint
            )
        )
        .setHighlightAlpha(.5f)
        .setUnderlined(isUnderLine)
        .setOnClickListener { _: String?, _: LinkMetadata? ->
//            redirectToTermAndPolicy(context)
        }

    val nameLink = Link(privacyPolicyText)
        .setTextColor(
            ContextCompat.getColor(
                context,
                R.color.themeColor_red
            )
        )
        .setTextColorOfHighlightedLink(
            ContextCompat.getColor(
                context,
                R.color.general_for_hint
            )
        )
        .setHighlightAlpha(.5f)
        .setUnderlined(isUnderLine)
        .setOnClickListener { _: String?, _: LinkMetadata? ->
//            redirectToUserPolicy(context)
        }
    links.add(nameLink)

    links.add(replyNameLink)

    return links
}

fun showExitScashPaymentWebViewConfirmationDialog(
    mContext: Context,
    dialogListener: DialogListener?
) {
    val dialogView = View.inflate(mContext, R.layout.dialog_common_confirmation_vertical_new2, null)

    dialogView.run {
        val tvDialogTitle = findViewById<TextView>(R.id.tvDialogTitle)
        val tvDialogMessage = findViewById<TextView>(R.id.tvDialogMessage)

        val btnPositive = findViewById<Button>(R.id.btnPositive)

        val btnLlHorizontal = findViewById<LinearLayout>(R.id.btnLlHorizontal)
        val btnHorizontalCancel = findViewById<Button>(R.id.btnHorizontalCancel)
        val btnHorizontalPositive = findViewById<Button>(R.id.btnHorizontalPositive)

        btnPositive.visibility = View.GONE
        btnLlHorizontal.visibility = View.VISIBLE
        btnHorizontalPositive.isEnabled = true
        btnHorizontalPositive.background =
            ContextCompat.getDrawable(context, R.drawable.solid_bg_blue_xr)

        tvDialogTitle.apply {
            visibility = View.VISIBLE
            text = mContext.getString(R.string.rw_text_cancel_transaction)
        }

        tvDialogMessage.apply {
            visibility = View.VISIBLE
            setTextColor(ResourcesCompat.getColor(context.resources, R.color.gray_808080, null))
            text = mContext.getString(R.string.rw_text_cancel_transaction_description)
        }

        btnHorizontalPositive.apply {
            text = mContext.getString(R.string.rw_text_yes)
            stateListAnimator = null
            setOnClickListener {
                dialogListener?.onConfirmClick()
                DialogHelper.getInstance().closeDialog()
            }
        }

        btnHorizontalCancel.apply {
            text = mContext.getString(R.string.rw_text_no)
            stateListAnimator = null
            setOnClickListener {
                dialogListener?.onCancelClick()
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

fun showMarkAsRedeemConfirmationDialog(
    mContext: Context,
    dialogListener: DialogListener?
) {
    val dialogView = View.inflate(mContext, R.layout.dialog_common_confirmation_vertical_new2, null)

    dialogView.run {
        val tvDialogTitle = findViewById<TextView>(R.id.tvDialogTitle)
        val tvDialogMessage = findViewById<TextView>(R.id.tvDialogMessage)

        val btnPositive = findViewById<Button>(R.id.btnPositive)

        val btnLlHorizontal = findViewById<LinearLayout>(R.id.btnLlHorizontal)
        val btnHorizontalCancel = findViewById<Button>(R.id.btnHorizontalCancel)
        val btnHorizontalPositive = findViewById<Button>(R.id.btnHorizontalPositive)

        btnPositive.visibility = View.GONE
        btnLlHorizontal.visibility = View.VISIBLE
        btnHorizontalPositive.isEnabled = true
        btnHorizontalPositive.background =
            ContextCompat.getDrawable(context, R.drawable.solid_bg_blue_xr)

        tvDialogTitle.apply {
            visibility = View.VISIBLE
            text = "${context.getString(R.string.rw_text_mark_as_redeemed)}?"
        }

        tvDialogMessage.apply {
            visibility = View.VISIBLE
            setTextColor(ResourcesCompat.getColor(context.resources, R.color.gray_808080, null))
            text = mContext.getString(R.string.rw_text_voucher_redeem_desc)
        }

        btnHorizontalPositive.apply {
            text = mContext.getString(R.string.rw_text_yes)
            stateListAnimator = null
            setOnClickListener {
                dialogListener?.onConfirmClick()
                DialogHelper.getInstance().closeDialog()
            }

        }

        btnHorizontalCancel.apply {
            text = mContext.getString(R.string.rw_text_no)
            stateListAnimator = null
            setOnClickListener {
                dialogListener?.onCancelClick()
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