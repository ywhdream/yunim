package com.netease.yunxin.kit.chatkit.ui.view

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.*
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.netease.yunxin.kit.chatkit.ui.R

class PulsingRingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private val defaultState = State.IDLE
        private const val defaultRingWidth = 1f //dp
        private const val defaultRingBreathingWidth = 1f //dp
        private const val defaultRingWidthMode = 0
        private const val defaultIdleRingSizeRatio = .9f
        private const val defaultPulsingMinRatio = .85f
        private const val defaultPulsingMaxRatio = .93f
        private const val defaultRingSizeAnimDuration = 500L
        private const val defaultPulsingAnimDuration = 500L
        private val defaultAnimInterpolator = AccelerateInterpolator()
    }

    var state = defaultState
        set(value) {
            field = value
            animateStateTransition()
        }

    @ColorInt
    var ringColor: Int = -1
        set(value) {
            field = value
            invalidate()
        }

    //px
    var ringWidth: Int = 0
        set(value) {
            field = value
            animateStateTransition()
        }

    //px
    var ringBreathingWidth: Int = 0
        set(value) {
            field = value
            animateStateTransition()
        }

    var idleRingSizeRatio = defaultIdleRingSizeRatio
        set(value) {
            field = value
            animateStateTransition()
        }

    var pulsingRingMinRatio = defaultPulsingMinRatio
        set(value) {
            field = value
            animateStateTransition()
        }

    var pulsingRingMaxRatio = defaultPulsingMaxRatio
        set(value) {
            field = value
            animateStateTransition()
        }

    var sizeAnimationDuration: Long = defaultRingSizeAnimDuration
        set(value) {
            field = value
            animateStateTransition()
        }

    var pulsingAnimationDuration: Long = defaultPulsingAnimDuration
        set(value) {
            field = value
            animateStateTransition()
        }

    var animationInterpolator: TimeInterpolator = defaultAnimInterpolator
        set(value) {
            field = value
            animateStateTransition()
        }

    private var animatedSizeRatio = idleRingSizeRatio
    private var animatedRingWidth = ringWidth.toFloat()

    //0: expands when breathing in, 1: expands when breathing out
    private var ringWidthMode = defaultRingWidthMode
    private var ringSizeRatioValueAnimator: ValueAnimator? = null
    private var ringWidthValueAnimator: ValueAnimator? = null

    private val paint = Paint()

    init {
        init(attrs)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawRing(canvas)
    }

    private fun init(attrs: AttributeSet?) {
        fetchViewAttributes(attrs)
        paint.isAntiAlias = true
    }

    private fun fetchViewAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val array: TypedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.PulsingRingView
            )

            ringColor = array.getColor(R.styleable.PulsingRingView_ringColor, Color.TRANSPARENT)

            ringWidth = array.getDimensionPixelOffset(
                R.styleable.PulsingRingView_ringWidth,
                dp2px(defaultRingWidth).toInt()
            )

            ringBreathingWidth = array.getDimensionPixelOffset(
                R.styleable.PulsingRingView_ringBreathingWidth,
                dp2px(defaultRingBreathingWidth).toInt()
            )

            ringWidthMode =
                array.getInteger(R.styleable.PulsingRingView_ringBreathingWidthMode, ringWidthMode)

            idleRingSizeRatio =
                array.getFloat(R.styleable.PulsingRingView_ringIdleSizeRatio, idleRingSizeRatio)

            pulsingRingMinRatio = array.getFloat(
                R.styleable.PulsingRingView_ringPulsingSizeMinRatio,
                pulsingRingMinRatio
            )

            pulsingRingMaxRatio = array.getFloat(
                R.styleable.PulsingRingView_ringPulsingSizeMaxRatio,
                pulsingRingMaxRatio
            )

            pulsingAnimationDuration = array.getInteger(
                R.styleable.PulsingRingView_ringIdleSizeRatio,
                pulsingAnimationDuration.toInt()
            ).toLong()

            val interpolatorFlag =
                array.getInteger(R.styleable.PulsingRingView_ringAnimInterpolator, -1)

            animationInterpolator = when (interpolatorFlag) {
                0 -> AccelerateInterpolator()
                1 -> AccelerateDecelerateInterpolator()
                2 -> AnticipateInterpolator()
                3 -> AnticipateOvershootInterpolator()
                4 -> BounceInterpolator()
                5 -> DecelerateInterpolator()
                6 -> LinearInterpolator()
                7 -> OvershootInterpolator()
                else -> animationInterpolator
            }

            array.recycle()
        }
    }

    private fun drawRing(canvas: Canvas) {
        val midX = width / 2f
        val midY = height / 2f

        val roundedRectRadius = height / 2f * animatedSizeRatio

        paint.color = ringColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = animatedRingWidth

        canvas.drawCircle(midX, midY, roundedRectRadius, paint)
    }

    private fun animateStateTransition() {
        ringSizeRatioValueAnimator?.cancel()
        ringWidthValueAnimator?.cancel()

        val targetRingSizeRatio =
            when (state) {
                State.PULSING -> {
                    pulsingRingMinRatio
                }
                else -> {
                    idleRingSizeRatio
                }
            }

        ringSizeRatioValueAnimator =
            ValueAnimator.ofFloat(animatedSizeRatio, targetRingSizeRatio).apply {
                duration = sizeAnimationDuration
                addUpdateListener { animation ->
                    animatedSizeRatio = animation.animatedValue as Float
                    invalidate()

                    if (animation.animatedValue == pulsingRingMinRatio && state == State.PULSING)
                        startRingPulsing()
                }
                interpolator = animationInterpolator
            }

        val targetRingWidthRatio =
            when (state) {
                State.PULSING -> {
                    getAnimatedRingWidth(pulsingRingMinRatio)
                }
                else -> {
                    ringWidth.toFloat()
                }
            }

        ringWidthValueAnimator =
            ValueAnimator.ofFloat(animatedRingWidth, targetRingWidthRatio).apply {
                duration = sizeAnimationDuration
                addUpdateListener { animation ->
                    animatedRingWidth = animation.animatedValue as Float
                    //no need to invalidate here as ringSizeRatioValueAnimator will do the job
                }
                interpolator = animationInterpolator
            }

        ringSizeRatioValueAnimator?.start()
        ringWidthValueAnimator?.start()
    }

    private fun startRingPulsing() {
        ringSizeRatioValueAnimator?.cancel()
        ringSizeRatioValueAnimator = ValueAnimator.ofFloat(
            animatedSizeRatio,
            pulsingRingMaxRatio
        ).apply {
            duration = pulsingAnimationDuration
            addUpdateListener { animation ->
                animatedSizeRatio = animation.animatedValue as Float
                animatedRingWidth = getAnimatedRingWidth(animation.animatedValue as Float)
                invalidate()
            }
            interpolator = animationInterpolator
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
        }
        ringSizeRatioValueAnimator?.start()
    }

    private fun getAnimatedRingWidth(animatedRingSizeRatio: Float): Float {
        val ringWidthDiff = ringBreathingWidth - ringWidth
        val progress =
            (animatedRingSizeRatio - pulsingRingMinRatio) / (pulsingRingMaxRatio - pulsingRingMinRatio)

        return if (ringWidthMode == 0) {
            ringWidth + ringWidthDiff * (1f - progress)
        } else {
            ringWidth + ringWidthDiff * progress
        }
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        )
    }

    enum class State {
        PULSING,
        IDLE
    }
}