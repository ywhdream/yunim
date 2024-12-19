package com.netease.yunxin.kit.chatkit.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.netease.yunxin.kit.chatkit.ui.R


class UserAvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val avatarToVerifyRatio = .375f
    }

    lateinit var ivAvatar: FilterImageView
        private set
    lateinit var ivVerify: AppCompatImageView
        private set
    lateinit var ivBadge: AppCompatImageView
        private set
    lateinit var viewPulsingRingView: PulsingRingView
        private set

    var useFrameHolder = false
        set(value) {
            field = value
            updateViewVisibility()
            updateViewConstraints()
        }

    init {
        init(attrs)
    }

    fun setPulsingRingColor(@ColorInt ringColor: Int?) {
        viewPulsingRingView.ringColor = ringColor ?: Color.TRANSPARENT
    }

    fun setPulsingRingMode(mode: PulsingRingView.State) {
        viewPulsingRingView.state = mode
    }

    override fun setOnClickListener(l: OnClickListener?) {
        ivAvatar.setOnClickListener(l)
    }

    override fun setOnLongClickListener(l: OnLongClickListener?) {
        ivAvatar.setOnLongClickListener(l)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.view_user_avater, this)
        bindView()
        fetchViewAttributes(attrs)
    }

    private fun fetchViewAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val array: TypedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.UserAvatarView
            )

            useFrameHolder =
                array.getBoolean(R.styleable.UserAvatarView_useFrameHolder, useFrameHolder)

            array.recycle()
        }
    }

    private fun bindView() {
        viewPulsingRingView = findViewById(R.id.view_pulsing_bg)
        ivBadge = findViewById(R.id.iv_badge)
        ivAvatar = findViewById(R.id.iv_avatar)
        ivVerify = findViewById(R.id.iv_verify)
    }

    private fun updateViewVisibility() {
        if (useFrameHolder) {
            ivBadge.visibility = View.VISIBLE
            viewPulsingRingView.visibility = View.VISIBLE
        } else {
            ivBadge.visibility = View.GONE
            viewPulsingRingView.visibility = View.INVISIBLE
        }
    }

    private fun updateViewConstraints() {
        val newAvatarSizeRatio = if (useFrameHolder) {
            .78f
        } else {
            1f
        }

        val newVerifySizeRatio = newAvatarSizeRatio * avatarToVerifyRatio

        (ivAvatar.layoutParams as ConstraintLayout.LayoutParams).apply {
            matchConstraintPercentWidth = newAvatarSizeRatio
        }

        (ivVerify.layoutParams as ConstraintLayout.LayoutParams).apply {
            matchConstraintPercentWidth = newVerifySizeRatio
        }

        ivAvatar.requestLayout()
        ivVerify.requestLayout()
    }
}