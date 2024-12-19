package com.netease.yunxin.kit.chatkit.ui.avtivity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.netease.lava.nertc.sdk.LastmileProbeResult
import com.netease.lava.nertc.sdk.NERtc
import com.netease.lava.nertc.sdk.NERtcCallbackEx
import com.netease.lava.nertc.sdk.NERtcConstants
import com.netease.lava.nertc.sdk.NERtcEx
import com.netease.lava.nertc.sdk.NERtcOption
import com.netease.lava.nertc.sdk.NERtcUserJoinExtraInfo
import com.netease.lava.nertc.sdk.NERtcUserLeaveExtraInfo
import com.netease.lava.nertc.sdk.audio.NERtcAudioStreamType
import com.netease.lava.nertc.sdk.stats.NERtcAudioVolumeInfo
import com.netease.lava.nertc.sdk.video.NERtcRemoteVideoStreamType
import com.netease.lava.nertc.sdk.video.NERtcVideoStreamType
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.Observer
import com.netease.nimlib.sdk.avsignalling.constant.ChannelType
import com.netease.nimlib.sdk.v2.avsignalling.V2NIMSignallingListener
import com.netease.nimlib.sdk.v2.avsignalling.V2NIMSignallingService
import com.netease.nimlib.sdk.v2.avsignalling.config.V2NIMSignallingConfig
import com.netease.nimlib.sdk.v2.avsignalling.config.V2NIMSignallingPushConfig
import com.netease.nimlib.sdk.v2.avsignalling.config.V2NIMSignallingRtcConfig
import com.netease.nimlib.sdk.v2.avsignalling.enums.V2NIMSignallingChannelType
import com.netease.nimlib.sdk.v2.avsignalling.enums.V2NIMSignallingEventType
import com.netease.nimlib.sdk.v2.avsignalling.model.V2NIMSignallingEvent
import com.netease.nimlib.sdk.v2.avsignalling.model.V2NIMSignallingRoomInfo
import com.netease.nimlib.sdk.v2.avsignalling.params.V2NIMSignallingCallParams
import com.netease.nimlib.sdk.v2.avsignalling.params.V2NIMSignallingCallSetupParams
import com.netease.nimlib.sdk.v2.avsignalling.params.V2NIMSignallingCancelInviteParams
import com.netease.nimlib.sdk.v2.avsignalling.params.V2NIMSignallingRejectInviteParams
import com.netease.nimlib.sdk.v2.user.V2NIMUser
import com.netease.nimlib.sdk.v2.user.V2NIMUserService
import com.netease.yunxin.kit.chatkit.ui.R
import com.netease.yunxin.kit.chatkit.ui.constant.Constant.CALLEE_ACCOUNT_ID
import com.netease.yunxin.kit.chatkit.ui.constant.Constant.CHANNEL_ID
import com.netease.yunxin.kit.chatkit.ui.constant.Constant.KEY_IN_CALLING
import com.netease.yunxin.kit.chatkit.ui.constant.Constant.REQUEST_ID
import com.netease.yunxin.kit.chatkit.ui.constant.Constant.RTC_CHANNEL_NAME
import com.netease.yunxin.kit.chatkit.ui.constant.Constant.STATE_CALL_TYPE
import com.netease.yunxin.kit.chatkit.ui.databinding.AvchatActivityBinding
import com.netease.yunxin.kit.chatkit.ui.dialog.CommonDialog
import com.netease.yunxin.kit.chatkit.ui.model.AVChatTimeoutObserver
import com.netease.yunxin.kit.chatkit.ui.model.NimCallTypeReq
import com.netease.yunxin.kit.chatkit.ui.model.NimVideoConstant
import com.netease.yunxin.kit.chatkit.ui.notification.AVChatNotification
import com.netease.yunxin.kit.chatkit.ui.permission.BaseMPermission
import com.netease.yunxin.kit.chatkit.ui.permission.GlobalPermissionUtils
import com.netease.yunxin.kit.chatkit.ui.permission.GlobalPermissionUtils.Companion.permissionCamera
import com.netease.yunxin.kit.chatkit.ui.permission.MPermission
import com.netease.yunxin.kit.chatkit.ui.permission.getPermissionPurposeLinks
import com.netease.yunxin.kit.chatkit.ui.util.AVChatProfile
import com.netease.yunxin.kit.chatkit.ui.util.ScreenUtil
import com.netease.yunxin.kit.chatkit.ui.view.DialogHelper
import com.netease.yunxin.kit.chatkit.ui.view.ToggleListener
import com.netease.yunxin.kit.chatkit.ui.view.ToggleState
import com.netease.yunxin.kit.chatkit.ui.view.ToggleView
import com.netease.yunxin.kit.common.ui.activities.BaseActivity
import com.netease.yunxin.kit.common.ui.utils.ToastUtils
import java.nio.ByteBuffer
import kotlin.math.abs
import kotlin.math.max


/**
 * @Author: YWH
 * @Date: 2024/12/4 15:41
 * @Description: 类的描述信息
 */
class AVChatActivity : BaseActivity(), View.OnClickListener, ToggleListener,
    SensorEventListener {


    private lateinit var avchatActivityBinding: AvchatActivityBinding
    private var isLocalVideoMuted = false //是否关闭摄像头
    private var isLocalAudioStream = false //是否禁言 默认不禁言
    private var isSwitchRender = true // 大小图像相互切换 默认小图
    private var mUid: Long = 0 //获取对方的uid
    private var surfaceInit = false
    private var largeAccount = String() // 显示在大图像的用户id
    private var smallAccount = String() // 显示在小图像的用户id

    // move
    private var lastX = 0
    private var lastY = 0
    private var inX = 0
    private var inY = 0
    private val TOUCH_SLOP = 10
    private var paddingRect: Rect = Rect(
        ScreenUtil.dip2px(10f),
        ScreenUtil.dip2px(20f),
        ScreenUtil.dip2px(10f),
        ScreenUtil.dip2px(70f)
    )
    private var switchCameraToggle: ToggleView? = null
    private var closeCameraToggle: ToggleView? = null
    private var muteToggle: ToggleView? = null
    private var loudspeakerToggle: ToggleView? = null //扬声器
    private var shouldEnableToggle = false
    private var topRootHeight = 0
    private var bottomRootHeight = 0
    private var bottomRootFUHeight = 0
    private val BASIC_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    private var canSwitchCamera: Boolean = false
    private var mChannelId = String() //信令频道
    private var mIsInComingCall = false // false打电话，
    private var stateCallType: Int = 0//  音频或视频
    private var calleeAccountId = String()//呼叫的对象
    val BASIC_PERMISSION_REQUEST_CODE: Int = 0x100
    private var isHangUp = false //是否已经接听，默认未接通
    private var isInSwitch = false
    private var channelId = String()
    private var requestId = String()
    private val mQequestId = System.nanoTime().toString()

    private var rtcChannelName = String()
    private var signallingListener: V2NIMSignallingListener? = null
    private var isConversion = false //是否转视频
    private val DELAY_FINISH_ACTIVITY_FOR_API_CALL: Int = 500
    private var notifier = AVChatNotification(this)
    private var mSensorManager: SensorManager? = null
    private var mProximity: Sensor? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private var hasOnPause = false // 是否暂停音视频
    private val SENSOR_SENSITIVITY: Int = 2

    private var isReleasedVideo = false

    // 通话过程中，收到对方挂断电话
    private var timeoutObserver: Observer<Int> =
        Observer<Int> {
            // 来电超时，自己未接听
            if (mIsInComingCall) {
                activeMissCallNotifier()
            } else { //主动方

                Log.d("ywh", "===== ")
                cancelInviteOther(NimVideoConstant.NO_RESPONSE)
            }
            Handler(Looper.getMainLooper())
                .postDelayed(
                    { finish() },
                    DELAY_FINISH_ACTIVITY_FOR_API_CALL.toLong()
                )
        }

    //取消邀请别人
    private fun cancelInviteOther(eventType: Int) {

        val params =
            V2NIMSignallingCancelInviteParams.Builder(mChannelId, calleeAccountId, mQequestId)
                .offlineEnabled(true)
                .build()
        NIMClient.getService(V2NIMSignallingService::class.java).cancelInvite(params, {
            Log.d("ywh", "成功: ")
            sendCustomizeMsg(eventType)
//                    AVChatSoundPlayer.instance().stop()
            NERtcEx.getInstance().leaveChannel()
            closeSession()
        },
            {
                Log.d("ywh", "失败:${it.code}  ${it.desc}  ${it.detail} ")
                closeSession()
            })


    }


    //接听成功后，移除时间监听
    fun removeTime() {
        AVChatTimeoutObserver.getInstance()
            .observeTimeoutNotification(timeoutObserver, false, mIsInComingCall)
    }

    private fun registerObserves(register: Boolean) {
        AVChatTimeoutObserver.getInstance()
            .observeTimeoutNotification(timeoutObserver, register, mIsInComingCall)
    }

    // 设置窗口flag，亮屏并且解锁/覆盖在锁屏界面上
    private fun dismissKeyguard() {
        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
    }

    @SuppressLint("InvalidWakeLockTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // 启动成功，取消timeout处理
        AVChatProfile.getInstance().activityLaunched()
        dismissKeyguard()
        avchatActivityBinding = AvchatActivityBinding.inflate(layoutInflater)
        setContentView(avchatActivityBinding.getRoot())
        mIsInComingCall = intent.getBooleanExtra(KEY_IN_CALLING, false)
        calleeAccountId = intent.getStringExtra(CALLEE_ACCOUNT_ID).toString()
        rtcChannelName = intent.getStringExtra(RTC_CHANNEL_NAME).toString()
        requestId = intent.getStringExtra(REQUEST_ID).toString()
        channelId = intent.getStringExtra(CHANNEL_ID).toString()
        stateCallType = intent.getIntExtra(STATE_CALL_TYPE, 1)
        initializeSDK()
        checkPermissionAndMakeCallUI()
        offlineObserver()
        registerObserves(true)

        if (stateCallType == V2NIMSignallingChannelType.V2NIM_SIGNALLING_CHANNEL_TYPE_VIDEO.value) {
            setupLocalView(false)

        }


        notifier.init(calleeAccountId, calleeAccountId)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (mSensorManager != null) {
            mProximity = mSensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        }

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "tag")


    }

    private fun cancelCallingNotifier() {
        notifier.activeCallingNotification(false)
    }

    override fun onResume() {
        super.onResume()
        mSensorManager?.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL)
        cancelCallingNotifier()

        if (hasOnPause) {
            hasOnPause = false
        }
    }

    override fun onPause() {
        super.onPause()
        mSensorManager?.unregisterListener(this)
        hasOnPause = true
    }

    override fun onStop() {
        super.onStop()
        activeCallingNotifier()
    }

    override fun onBackPressed() {
        // 禁用返回键
    }

    private fun activeCallingNotifier() {
        val isUserFinish = false
        if (!isUserFinish) {
            notifier.activeCallingNotification(true)
        }
    }

    private fun activeMissCallNotifier() {
        notifier.activeMissCallNotification(true)
    }

    private fun offlineObserver() {
        signallingListener = object : V2NIMSignallingListener {
            override fun onOnlineEvent(event: V2NIMSignallingEvent) {
                Log.d("ywh", "onOnlineEvent: ${event.eventType}")


                // 处理在线事件
                when (event.eventType) {
                    V2NIMSignallingEventType.V2NIM_SIGNALLING_EVENT_TYPE_CLOSE -> {
                        NERtcEx.getInstance().leaveChannel()
                        finish()
                    }

                    V2NIMSignallingEventType.V2NIM_SIGNALLING_EVENT_TYPE_JOIN -> {
                        removeTime()
//                        AVChatSoundPlayer.instance().stop()
                        showVideoInitLayout()
                        isHangUp = true
                        setupLocalView(true)
                    }

                    V2NIMSignallingEventType.V2NIM_SIGNALLING_EVENT_TYPE_CANCEL_INVITE -> {
                        NERtcEx.getInstance().leaveChannel()
                        finish()
                    }

                    V2NIMSignallingEventType.V2NIM_SIGNALLING_EVENT_TYPE_REJECT -> {

                        //如果对方拒绝，显示通话被拒绝，对方已经接听，则显示对方正在通话中，请稍后再拨。
                        val nimCallType: NimCallTypeReq = Gson().fromJson(
                            event.channelInfo.channelExtension,
                            NimCallTypeReq::class.java
                        )
                        ToastUtils.showLongToast(
                            this@AVChatActivity,
                            if (nimCallType.type.equals(NimVideoConstant.IS_CALL_BUSY)) getString(
                                R.string.avchat_peer_busy
                            ) else getString(R.string.call_rejected)
                        )
                        closeChannel(NimVideoConstant.REJECT)

                    }

                    V2NIMSignallingEventType.V2NIM_SIGNALLING_EVENT_TYPE_CONTROL -> {
                        val channelExtension = event.serverExtension

                        val nimSwitchCallType
                                : NimCallTypeReq = Gson().fromJson(
                            channelExtension,
                            NimCallTypeReq::class.java
                        )
                        val type = nimSwitchCallType.type


                        Log.d("ywh", "onOnlineEvent: $type")
                        when (type) {//如果是音频，直接就转，不需要发送自定义消息
                            NimVideoConstant.TO_AUDIO ->
                                showVideoAndAudioView(V2NIMSignallingChannelType.V2NIM_SIGNALLING_CHANNEL_TYPE_AUDIO.value)

                            NimVideoConstant.TO_VIDEO ->
                                setControl()

                            NimVideoConstant.ACCEPT_TO_VIDEO -> {//同意切换到视频
                                showVideoAndAudioView(V2NIMSignallingChannelType.V2NIM_SIGNALLING_CHANNEL_TYPE_VIDEO.value)
                                stateCallType =
                                    V2NIMSignallingChannelType.V2NIM_SIGNALLING_CHANNEL_TYPE_VIDEO.value
                                isConversion = false
                            }

                            NimVideoConstant.REJECT_TO_VIDEO -> {
                                isConversion = false
                                ToastUtils.showLongToast(
                                    this@AVChatActivity,
                                    getString(R.string.switching_reject_video)
                                )
                            }
                        }


                    }

                    else -> {}
                }
            }

            override fun onOfflineEvent(events: List<V2NIMSignallingEvent>) {
                // 处理离线事件
            }

            override fun onMultiClientEvent(event: V2NIMSignallingEvent) {
                // 处理多端事件
            }

            override fun onSyncRoomInfoList(channelRooms: List<V2NIMSignallingRoomInfo>) {
                // 处理同步信令频道房间列表
            }
        }



        NIMClient.getService(V2NIMSignallingService::class.java)
            .addSignallingListener(signallingListener)

    }


    private fun setControl() {
        try {
            CommonDialog.Builder(this@AVChatActivity)
                .setCanceledOnTouchOutside(false)
                .setTitle(getString(R.string.tips), R.color.black_translucent)
                .setMessage(getString(R.string.request_switching), R.color.black_translucent)
                .setPositiveButton(getString(R.string.accept_session), {
                    sendCtrl(NimVideoConstant.ACCEPT_TO_VIDEO)
                    showVideoAndAudioView(ChannelType.VIDEO.value)
                }, R.color.color_blue_238efa)
                .setNegativeButton(
                    getString(R.string.reject),
                    { sendCtrl(NimVideoConstant.REJECT_TO_VIDEO) },
                    R.color.color_blue_238efa
                ).show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    //发送自定义命令
    private fun sendCtrl(type: String) {
        Log.d("ywh", "sendCtrl: $type")
        val nimCallTypeReq = NimCallTypeReq()
        val dataRequest = NimCallTypeReq.DataRequest()

        nimCallTypeReq.type = type

        nimCallTypeReq.setData(dataRequest)
        val customInfo = Gson().toJson(nimCallTypeReq)
        Log.d("ywh", "成功: $customInfo")
        NIMClient.getService(V2NIMSignallingService::class.java)
            .sendControl(mChannelId, calleeAccountId, customInfo, {

                when (type) {
                    NimVideoConstant.TO_AUDIO -> {
                        stateCallType =
                            V2NIMSignallingChannelType.V2NIM_SIGNALLING_CHANNEL_TYPE_AUDIO.value
                        showVideoAndAudioView(stateCallType)
                    }

                    NimVideoConstant.ACCEPT_TO_VIDEO -> {
                        stateCallType =
                            V2NIMSignallingChannelType.V2NIM_SIGNALLING_CHANNEL_TYPE_VIDEO.value
                        showVideoAndAudioView(stateCallType)
                    }

                    NimVideoConstant.TO_VIDEO -> isConversion = true
                }
            },
                {
                    Log.d("ywh", "失败: ${it.desc}")

                    closeSession()
                })


    }

    //判断是语音还是视频，显示不同的界面
    private fun checkPermissionAndMakeCallUI() {
        checkPermission()
        avchatActivityBinding.avchatVideoLayout.root.visibility = View.VISIBLE
        avchatActivityBinding.avchatSurfaceLayout.root.visibility = View.VISIBLE
        if (mIsInComingCall) {
            // 来电
            showIncomingCall(stateCallType)
        } else {
            // 去电
            doOutgoingCall(calleeAccountId, stateCallType)
        }

    }

    //来电操作
    private fun showIncomingCall(state: Int) {
        findSurfaceView()
        findVideoViews()
        showVideoAndAudioView(state)

        showProfile() //对方的详细信息
        showNotify(if (state == V2NIMSignallingChannelType.V2NIM_SIGNALLING_CHANNEL_TYPE_AUDIO.value) R.string.avchat_audio_call_request else R.string.avchat_video_call_request)

        setRefuseReceive(true)
        setTopRoot(true)
        setMiddleRoot(true)
        setBottomRoot(true)
        setBottomRootFU(false)

        avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.avchatVideoSwitchAudio.visibility =
            View.INVISIBLE
        avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.ivAudioVideo.visibility =
            View.INVISIBLE
        canSwitchCamera = true


    }

    //根据状态显示音频或者视频
    fun showVideoAndAudioView(state: Int) {
        avchatActivityBinding.avchatVideoLayout.avchatVideoRefuseReceive.llRefuseReceive2.visibility =
            View.GONE
        if (state == V2NIMSignallingChannelType.V2NIM_SIGNALLING_CHANNEL_TYPE_AUDIO.value) {
            avchatActivityBinding.avchatSurfaceLayout.renderRemoteUser.visibility = View.GONE
            avchatActivityBinding.avchatSurfaceLayout.renderLocalUser.visibility = View.GONE
            avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.avchatSwitchCamera.visibility =
                View.GONE
            avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatLinearLayout2.visibility =
                View.GONE
            avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatLinearLayout4.visibility =
                View.VISIBLE

            avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.avchatVideoSwitchAudio.setText(
                R.string.avchat_switch_to_video
            )
            avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.ivAudioVideo.setBackgroundResource(
                R.drawable.avchat_switch_mode_video_icon
            )
            avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.avchatVideoTitle.setText(R.string.voice_call)
            avchatActivityBinding.avchatVideoLayout.avchatVideoRefuseReceive.receive.setBackgroundResource(
                R.drawable.ic_call_pick_up
            )
            setMiddleRoot(true)
            avchatActivityBinding.avchatSurfaceLayout.smallSizePreviewCoverImg.visibility =
                View.GONE
            isLocalVideoMuted = false
            avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatImg4.setBackgroundResource(
                R.drawable.avchat_speaker_icon_normal
            )
            NERtcEx.getInstance().setSpeakerphoneOn(false)

        } else {
            avchatActivityBinding.avchatSurfaceLayout.renderRemoteUser.visibility = View.VISIBLE
            avchatActivityBinding.avchatSurfaceLayout.renderLocalUser.visibility = View.VISIBLE
            avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.avchatSwitchCamera.visibility =
                View.VISIBLE

            avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatLinearLayout2.visibility =
                View.VISIBLE
            avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatLinearLayout4.visibility =
                View.GONE
            avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.avchatVideoSwitchAudio.setText(
                R.string.avchat_switch_to_audio
            )
            avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.ivAudioVideo.setBackgroundResource(
                R.drawable.avchat_switch_mode_audio_icon
            )
            avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.avchatVideoTitle.setText(R.string.video_call)
            avchatActivityBinding.avchatVideoLayout.avchatVideoRefuseReceive.receive.setBackgroundResource(
                R.drawable.ic_video_pick_up
            )
            setMiddleRoot(false)

            avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatImg2.setBackgroundResource(
                R.drawable.avchat_video_close_camera_normal
            )


            isLocalVideoMuted = true
            NERtcEx.getInstance().setSpeakerphoneOn(true)
            NERtcEx.getInstance()
                .muteLocalVideoStream(NERtcVideoStreamType.kNERtcVideoStreamTypeMain, false)
        }
    }

    private fun setMiddleRoot(visible: Boolean) {
        avchatActivityBinding.avchatVideoLayout.avchatVideoMiddleControl.visibility =
            if (visible) View.VISIBLE else View.GONE
    }

    //扬声器是否外放
    private fun loudspeaker() {
        NERtcEx.getInstance().setSpeakerphoneOn(!NERtcEx.getInstance().isSpeakerphoneOn)
        if (NERtcEx.getInstance().isSpeakerphoneOn) {
            avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatImg4.setBackgroundResource(
                R.drawable.avchat_speaker_icon_checked
            )
        } else {
            avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatImg4.setBackgroundResource(
                R.drawable.avchat_speaker_icon_normal
            )
        }
    }

    // 切换摄像头（主要用于前置和后置摄像头切换）
    private fun switchCamera() {
        NERtcEx.getInstance().switchCamera()
    }

    //设置是否静音
    private fun toggleMute() {
        isLocalAudioStream = !isLocalAudioStream
        NERtcEx.getInstance().muteLocalAudioStream(isLocalAudioStream)
    }

    //设置本地预览
    fun setupLocalView(isHangUp: Boolean) {

        Log.d("ywh", "setupLocalView: $isHangUp")
        NERtcEx.getInstance().setupLocalVideoCanvas(null)
        NERtcEx.getInstance().enableLocalVideo(true)
        NERtcEx.getInstance().startVideoPreview(NERtcVideoStreamType.kNERtcVideoStreamTypeMain)
        if (isHangUp) {
            avchatActivityBinding.avchatSurfaceLayout.renderLocalUser.setScalingType(NERtcConstants.VideoScalingType.SCALE_ASPECT_BALANCED)
            NERtcEx.getInstance()
                .setupLocalVideoCanvas(avchatActivityBinding.avchatSurfaceLayout.renderLocalUser)
            //以开启本地视频主流采集并发送为例
            NERtcEx.getInstance()
                .enableLocalVideo(NERtcVideoStreamType.kNERtcVideoStreamTypeMain, true)
        } else {
            avchatActivityBinding.avchatSurfaceLayout.renderRemoteUser.setScalingType(NERtcConstants.VideoScalingType.SCALE_ASPECT_BALANCED)
            NERtcEx.getInstance()
                .setupLocalVideoCanvas(avchatActivityBinding.avchatSurfaceLayout.renderRemoteUser)
        }
        isLocalVideoMuted = true
    }

    //显示远程视图
    fun setupRemoteView(uid: Long) {
        //对方开启视频，按需设置画布及订阅视频
        NERtcEx.getInstance()
            .setupRemoteVideoCanvas(avchatActivityBinding.avchatSurfaceLayout.renderRemoteUser, uid)
        NERtcEx.getInstance().subscribeRemoteVideoStream(
            uid,
            NERtcRemoteVideoStreamType.kNERtcRemoteVideoStreamTypeHigh,
            true
        )
    }

    // 大小图像相互切换
    private fun switchRender() {
        NERtcEx.getInstance().setupLocalVideoCanvas(null)

        if (isSwitchRender) { //大图显示自己，小图显示远端
            avchatActivityBinding.avchatSurfaceLayout.renderRemoteUser.setScalingType(NERtcConstants.VideoScalingType.SCALE_ASPECT_BALANCED)
            NERtcEx.getInstance()
                .setupLocalVideoCanvas(avchatActivityBinding.avchatSurfaceLayout.renderRemoteUser)
            NERtcEx.getInstance().setupRemoteVideoCanvas(
                avchatActivityBinding.avchatSurfaceLayout.renderLocalUser,
                mUid
            )
            isSwitchRender = false
        } else { //小图显示自己，大图显示远端
            avchatActivityBinding.avchatSurfaceLayout.renderLocalUser.setScalingType(NERtcConstants.VideoScalingType.SCALE_ASPECT_BALANCED)
            NERtcEx.getInstance()
                .setupLocalVideoCanvas(avchatActivityBinding.avchatSurfaceLayout.renderLocalUser)
            NERtcEx.getInstance().setupRemoteVideoCanvas(
                avchatActivityBinding.avchatSurfaceLayout.renderRemoteUser,
                mUid
            )
            isSwitchRender = true
        }
        //以开启本地视频主流采集并发送为例
        NERtcEx.getInstance().enableLocalVideo(NERtcVideoStreamType.kNERtcVideoStreamTypeMain, true)
        NERtcEx.getInstance().subscribeRemoteVideoStream(
            mUid,
            NERtcRemoteVideoStreamType.kNERtcRemoteVideoStreamTypeHigh,
            true
        )
    }

    //初始化2.0音视频SDK
    private fun initializeSDK() {
        val option = NERtcOption()
        //设置detail_info打印更详细日志
        option.logLevel = NERtcConstants.LogLevel.DEBUG
        try {
            NERtcEx.getInstance().init(
                this,
                "43cf17ab859f4c669349fd68e363d6db",
                object : NERtcCallbackEx {
                    override fun onUserSubStreamVideoStart(l: Long, i: Int) {
                    }

                    override fun onUserSubStreamVideoStop(l: Long) {
                    }

                    override fun onUserAudioMute(l: Long, b: Boolean) {
                    }

                    //对方关闭摄像头
                    override fun onUserVideoMute(uid: Long, muted: Boolean) {
                        avchatActivityBinding.avchatSurfaceLayout.renderRemoteUser.visibility =
                            if (muted) View.GONE else View.VISIBLE
                    }

                    override fun onUserVideoMute(
                        neRtcVideoStreamType: NERtcVideoStreamType,
                        l: Long,
                        b: Boolean
                    ) {
                    }

                    /*@Override
                        public void onLocalAudioFirstPacketSent(NERtcAudioStreamType neRtcAudioStreamType) {

                        }*/
                    override fun onFirstAudioDataReceived(l: Long) {
                    }

                    override fun onLocalAudioFirstPacketSent(audioStreamType: NERtcAudioStreamType) {
                    }

                    override fun onFirstVideoDataReceived(l: Long) {
                    }

                    override fun onFirstVideoDataReceived(
                        neRtcVideoStreamType: NERtcVideoStreamType,
                        l: Long
                    ) {
                    }

                    override fun onFirstAudioFrameDecoded(l: Long) {
                    }

                    override fun onFirstVideoFrameDecoded(l: Long, i: Int, i1: Int) {
                    }

                    override fun onFirstVideoFrameRender(
                        userID: Long,
                        streamType: NERtcVideoStreamType,
                        width: Int,
                        height: Int,
                        elapsedTime: Long
                    ) {
                    }

                    override fun onFirstVideoFrameDecoded(
                        neRtcVideoStreamType: NERtcVideoStreamType,
                        l: Long,
                        i: Int,
                        i1: Int
                    ) {
                    }

                    override fun onUserVideoProfileUpdate(l: Long, i: Int) {
                    }

                    override fun onAudioDeviceChanged(i: Int) {
                    }

                    override fun onAudioDeviceStateChange(i: Int, i1: Int) {
                    }

                    override fun onVideoDeviceStageChange(i: Int) {
                    }

                    override fun onConnectionTypeChanged(i: Int) {
                    }

                    override fun onReconnectingStart() {
                    }

                    override fun onReJoinChannel(i: Int, l: Long) {
                    }

                    override fun onAudioMixingStateChanged(i: Int) {
                    }

                    override fun onAudioMixingTimestampUpdate(l: Long) {
                    }

                    override fun onAudioEffectTimestampUpdate(l: Long, l1: Long) {
                    }

                    override fun onAudioEffectFinished(i: Int) {
                    }

                    override fun onLocalAudioVolumeIndication(i: Int) {
                    }

                    override fun onLocalAudioVolumeIndication(i: Int, b: Boolean) {
                    }

                    override fun onRemoteAudioVolumeIndication(
                        neRtcAudioVolumeInfos: Array<NERtcAudioVolumeInfo>,
                        i: Int
                    ) {
                    }

                    override fun onLiveStreamState(s: String, s1: String, i: Int) {
                    }

                    override fun onConnectionStateChanged(i: Int, i1: Int) {
                    }

                    override fun onCameraFocusChanged(rect: Rect) {
                    }

                    override fun onCameraExposureChanged(rect: Rect) {
                    }

                    override fun onRecvSEIMsg(l: Long, s: String) {
                    }

                    override fun onAudioRecording(i: Int, s: String) {
                    }

                    override fun onError(i: Int) {
                    }

                    override fun onWarning(i: Int) {
                    }

                    override fun onApiCallExecuted(apiName: String, result: Int, message: String) {
                    }

                    override fun onMediaRelayStatesChange(i: Int, s: String) {
                    }

                    override fun onMediaRelayReceiveEvent(i: Int, i1: Int, s: String) {
                    }

                    override fun onLocalPublishFallbackToAudioOnly(
                        b: Boolean,
                        neRtcVideoStreamType: NERtcVideoStreamType
                    ) {
                    }

                    override fun onRemoteSubscribeFallbackToAudioOnly(
                        l: Long,
                        b: Boolean,
                        neRtcVideoStreamType: NERtcVideoStreamType
                    ) {
                    }

                    override fun onLastmileQuality(i: Int) {
                    }

                    override fun onLastmileProbeResult(lastmileProbeResult: LastmileProbeResult) {
                    }

                    override fun onMediaRightChange(b: Boolean, b1: Boolean) {
                    }

                    override fun onRemoteVideoSizeChanged(
                        userId: Long,
                        videoType: NERtcVideoStreamType,
                        width: Int,
                        height: Int
                    ) {
                    }

                    override fun onLocalVideoRenderSizeChanged(
                        videoType: NERtcVideoStreamType,
                        width: Int,
                        height: Int
                    ) {
                    }

                    override fun onVirtualBackgroundSourceEnabled(b: Boolean, i: Int) {
                    }

                    override fun onUserSubStreamAudioStart(l: Long) {
                    }

                    override fun onUserSubStreamAudioStop(l: Long) {
                    }

                    override fun onUserSubStreamAudioMute(l: Long, b: Boolean) {
                    }

                    override fun onPermissionKeyWillExpire() {
                    }

                    override fun onUpdatePermissionKey(s: String, i: Int, i1: Int) {
                    }

                    override fun onLocalVideoWatermarkState(
                        neRtcVideoStreamType: NERtcVideoStreamType,
                        i: Int
                    ) {
                    }

                    override fun onUserDataStart(uid: Long) {
                    }

                    override fun onUserDataStop(uid: Long) {
                    }

                    override fun onUserDataReceiveMessage(
                        uid: Long,
                        bufferData: ByteBuffer,
                        bufferSize: Long
                    ) {
                    }

                    override fun onUserDataStateChanged(uid: Long) {
                    }

                    override fun onUserDataBufferedAmountChanged(uid: Long, previousAmount: Long) {
                    }

                    override fun onLabFeatureCallback(key: String, param: Any) {
                    }

                    //本端用户加入房间结果回调
                    override fun onJoinChannel(
                        result: Int,
                        channelId: Long,
                        elapsed: Long,
                        uid: Long
                    ) {
                    }

                    //本端用户离开房间回调
                    override fun onLeaveChannel(i: Int) {
                        NERtcEx.getInstance().leaveChannel()
                    }

                    //远端用户加入房间
                    override fun onUserJoined(uid: Long) {
                    }

                    override fun onUserJoined(
                        l: Long,
                        neRtcUserJoinExtraInfo: NERtcUserJoinExtraInfo
                    ) {
                    }

                    //远端用户离开房间
                    override fun onUserLeave(l: Long, code: Int) {
                        if (code != 0) {
                            sendCustomizeMsg(NimVideoConstant.BILL)
                            NERtcEx.getInstance().leaveChannel()
                            finish()
                        }
                    }

                    override fun onUserLeave(
                        l: Long,
                        i: Int,
                        neRtcUserLeaveExtraInfo: NERtcUserLeaveExtraInfo
                    ) {
                    }

                    //远端用户打开音频
                    override fun onUserAudioStart(l: Long) {
                    }

                    //远端用户关闭音频
                    override fun onUserAudioStop(l: Long) {
                    }

                    //远端用户打开视频，建议在此按需设置画布及订阅视频
                    override fun onUserVideoStart(uid: Long, maxProfile: Int) {
                        setupRemoteView(uid)
                        mUid = uid
                        Log.d("ywh", "uid: $uid")
                    }

                    //远端用户关闭视频，可释放之前绑定的画布
                    override fun onUserVideoStop(uid: Long) {
                        NERtcEx.getInstance().setupRemoteVideoCanvas(null, uid)
                    }

                    //与服务器断连，退出页面
                    override fun onDisconnect(i: Int) {
                        finish()
                    }

                    override fun onClientRoleChange(i: Int, i1: Int) {
                    }
                }, option
            )
        } catch (e: Exception) {
            finish()
        }
    }

    //发送自定义消息
    fun sendCustomizeMsg(eventType: Int) {
//        val videoAndAudioAttachment: VideoAndAudioAttachment = VideoAndAudioAttachment()
//        videoAndAudioAttachment.setCallType(state)
//        videoAndAudioAttachment.setEventType(eventType)
//        if (eventType == NimVideoConstant.BILL) {
//            videoAndAudioAttachment.setDuration(getTime().toDouble())
//        }
//        val message = MessageBuilder.createCustomMessage(
//            sessionId,
//            SessionTypeEnum.P2P,
//            videoAndAudioAttachment
//        )
//
//        NIMClient.getService<MsgService>(MsgService::class.java).sendMessage(message, false)
//            .setCallback(object : RequestCallback<Void?> {
//                override fun onSuccess(result: Void?) {
//                    MessageListPanelHelper.getInstance().notifyAddMessage(message) // 界面上add一条
//                }
//
//                override fun onFailed(code: Int) {
//                    closeSession()
//                }
//
//                override fun onException(exception: Throwable) {
//                }
//            })
    }

    /**
     * ********************** surface 初始化 **********************
     */
    @SuppressLint("ClickableViewAccessibility")
    fun findSurfaceView() {
        if (surfaceInit) {
            return
        }
        avchatActivityBinding.avchatSurfaceLayout.smallSizePreviewLayout.setOnTouchListener(
            smallPreviewTouchListener
        )
        avchatActivityBinding.avchatSurfaceLayout.renderLocalUser.setOnClickListener(this)
        avchatActivityBinding.avchatSurfaceLayout.renderLocalUser.setZOrderMediaOverlay(true)
        avchatActivityBinding.avchatSurfaceLayout.renderRemoteUser.setZOrderMediaOverlay(false)
        surfaceInit = true
    }

    /**
     * ************************** video 初始化 ***********************
     */
    private var videoInit: Boolean = false
    private fun findVideoViews() {
        if (videoInit) {
            return
        }

        avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.avchatVideoSwitchAudio.setOnClickListener(
            this
        )
        avchatActivityBinding.avchatVideoLayout.avchatVideoRefuseReceive.receive.setOnClickListener(
            this
        )
        avchatActivityBinding.avchatVideoLayout.avchatVideoRefuseReceive.refuse.setOnClickListener(
            this
        )


        muteToggle = ToggleView(
            avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatLinearLayout1,
            ToggleState.DISABLE,
            this
        )
        loudspeakerToggle = ToggleView(
            avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatLinearLayout4,
            ToggleState.DISABLE,
            this
        )
        closeCameraToggle = ToggleView(
            avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatLinearLayout2,
            ToggleState.DISABLE,
            this
        )

        avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatImg1.setBackgroundResource(
            R.drawable.avchat_mute_icon_selector
        )

        avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatImg3.setBackgroundResource(
            R.drawable.avchat_audio_record_icon_selector
        )
        avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatTv1.text =
            this.getString(R.string.mute)
        avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatTv2.text =
            this.getString(R.string.camera_off)
        avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatTv3.text =
            this.getString(R.string.record)
        avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatTv4.text =
            this.getString(R.string.avchat_speaker)
        avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatLinearLayout3.visibility =
            View.GONE


        avchatActivityBinding.avchatVideoLayout.avchatVideoBottomFu.avchatImg2.setBackgroundResource(
            R.drawable.ic_call_hang_up
        )

        switchCameraToggle = ToggleView(
            avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.avchatSwitchCamera,
            ToggleState.ON,
            this
        )
        avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatImg3.isEnabled =
            false
        avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatImg3.setOnClickListener(
            this
        )
        avchatActivityBinding.avchatVideoLayout.avchatVideoBottomFu.avchatImg2.setOnClickListener(
            this
        )

        videoInit = true
        avchatActivityBinding.avchatVideoLayout.tvVideoPause.text = this.getString(
            R.string.video_is_paused,
            "displayName"
        )
    }

    // 显示个人信息
    @SuppressLint("CheckResult")
    private fun showProfile() {
        val mAccountIdList = mutableListOf<String>()
        mAccountIdList.add(calleeAccountId)
        NIMClient.getService(V2NIMUserService::class.java).getUserList(mAccountIdList, { success ->
            Glide.with(this).load(success[0].avatar).error(R.drawable.img_avatar).dontAnimate()
                .apply(RequestOptions.circleCropTransform())
                .into(avchatActivityBinding.avchatVideoLayout.avchatVideoHead.ivAvatar)
        }, {
        })

        avchatActivityBinding.avchatVideoLayout.avchatVideoNickname.text = calleeAccountId
    }

    // 显示通知
    private fun showNotify(resId: Int) {
        avchatActivityBinding.avchatVideoLayout.avchatVideoNotify.setText(resId)
        avchatActivityBinding.avchatVideoLayout.avchatVideoNotify.visibility = View.VISIBLE

    }

    private fun setRefuseReceive(visible: Boolean) {
        avchatActivityBinding.avchatVideoLayout.avchatVideoRefuseReceive.root.visibility =
            (if (visible) View.VISIBLE else View.GONE)

    }

    private fun setTopRoot(visible: Boolean) {
        avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.root.visibility =
            if (visible) View.VISIBLE else View.GONE

        if (topRootHeight == 0) {
            val rect = Rect()
            avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.root.getGlobalVisibleRect(
                rect
            )
            topRootHeight = rect.bottom
        }
    }

    private fun setBottomRoot(visible: Boolean) {
        avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.root.visibility =
            if (visible) View.VISIBLE else View.GONE
        if (bottomRootHeight == 0) {
            bottomRootHeight =
                avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.root.height
        }
    }

    private fun setBottomRootFU(visible: Boolean) {
        avchatActivityBinding.avchatVideoLayout.avchatVideoBottomFu.root.visibility =
            if (visible) View.VISIBLE else View.GONE
        if (bottomRootFUHeight == 0) {
            bottomRootFUHeight =
                avchatActivityBinding.avchatVideoLayout.avchatVideoBottomFu.root.height
        }
    }


    //拨打电话 创建频道
    private fun doOutgoingCall(calleeAccountId: String?, state: Int) {
        findSurfaceView()
        findVideoViews()
        showVideoAndAudioView(state)

        showProfile() //对方的详细信息

        showNotify(R.string.avchat_wait_receive)
        setRefuseReceive(false)
        shouldEnableToggle = true
        // enableCameraToggle();   //使用音视频预览时这里可以开启切换摄像头按钮
        setTopRoot(true)
        setMiddleRoot(true)
        setBottomRoot(true)
        setBottomRootFU(true)

        avchatActivityBinding.avchatVideoLayout.root.setBackgroundResource(R.color.color_transparent_grey)

        avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.avchatVideoSwitchAudio.visibility =
            View.INVISIBLE
        avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.ivAudioVideo.visibility =
            View.INVISIBLE
        avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.avchatVideoTitle.visibility =
            View.VISIBLE


        val content: String = String.format(
            this.getString(R.string.opponent_request_video_android),
            "ywh"
        )

        val nimCallTypeReq = NimCallTypeReq()
        val dataRequest = NimCallTypeReq.DataRequest()
        nimCallTypeReq.type = NimVideoConstant.P2P_CALL

        nimCallTypeReq.setData(dataRequest)
        val customInfo = Gson().toJson(nimCallTypeReq)


        val channelType =
            if (state == V2NIMSignallingChannelType.V2NIM_SIGNALLING_CHANNEL_TYPE_AUDIO.value) {
                V2NIMSignallingChannelType.V2NIM_SIGNALLING_CHANNEL_TYPE_AUDIO
            } else {
                V2NIMSignallingChannelType.V2NIM_SIGNALLING_CHANNEL_TYPE_VIDEO

            }

        //推送配置
        val v2NIMSignallingPushConfig = V2NIMSignallingPushConfig(true, "ywh", content, null)
        v2NIMSignallingPushConfig.isPushEnabled = true

        //信令配置
        val v2NIMSignallingConfig = V2NIMSignallingConfig()

        //音视频配置
        val v2NIMSignallingRtcConfig =
            V2NIMSignallingRtcConfig(System.nanoTime().toString(), 60 * 60L, null)


        val params = V2NIMSignallingCallParams.Builder(calleeAccountId, mQequestId, channelType)
            .channelExtension(customInfo)
            .channelName(System.nanoTime().toString())
            .pushConfig(v2NIMSignallingPushConfig)
            .signallingConfig(v2NIMSignallingConfig)
            .rtcConfig(v2NIMSignallingRtcConfig)

            .build()

        NIMClient.getService(V2NIMSignallingService::class.java).call(params, {
            //call success
            mChannelId = it.roomInfo.channelInfo.channelId

            // 创建频道成功并且成功加入
            val deniedPermissions: List<String> = BaseMPermission.getDeniedPermissions(
                this,
                BASIC_PERMISSIONS
            )
            if (deniedPermissions.isNotEmpty()) {
                showNoneCameraPermissionView(true)
                return@call
            }
            var uid: Long = 0
            for (mUid in it.roomInfo.members) {
                uid = mUid.uid
            }

            // 邀请对方成功，把自己添加到房间
            NERtcEx.getInstance().joinChannel(
                it.rtcInfo.rtcToken,
                it.roomInfo.channelInfo.channelName,
                uid,
                null
            )

            canSwitchCamera = true

            if (state == V2NIMSignallingChannelType.V2NIM_SIGNALLING_CHANNEL_TYPE_AUDIO.value) {
                NERtcEx.getInstance().setSpeakerphoneOn(false)
            }


        }, {
            Log.d("ywh", "doOutgoingCall: ${it.code} ${it.desc}")
            closeSession()
        })


    }

    @SuppressLint("ClickableViewAccessibility")
    private var smallPreviewTouchListener =
        View.OnTouchListener { v, event ->
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = x
                    lastY = y
                    val p = IntArray(2)
                    avchatActivityBinding.avchatSurfaceLayout.smallSizePreviewLayout.getLocationOnScreen(
                        p
                    )
                    inX = x - p[0]
                    inY = y - p[1]
                }

                MotionEvent.ACTION_MOVE -> {
                    val diff = max(abs((lastX - x)), abs((lastY - y)))
                    if (diff >= TOUCH_SLOP) {
                        val destX: Int = if (x - inX <= paddingRect.left) {
                            paddingRect.left
                        } else if (x - inX + v.width >= ScreenUtil.screenWidth - paddingRect.right) {
                            ScreenUtil.screenWidth - v.width - paddingRect.right
                        } else {
                            x - inX
                        }

                        val destY: Int = if (y - inY <= paddingRect.top) {
                            paddingRect.top
                        } else if (y - inY + v.height >= ScreenUtil.screenHeight - paddingRect.bottom) {
                            ScreenUtil.screenHeight - v.height
                        } else {
                            y - inY
                        }

                        val params = v.layoutParams as FrameLayout.LayoutParams
                        params.gravity = Gravity.NO_GRAVITY
                        params.leftMargin = destX
                        params.topMargin = destY
                        v.layoutParams = params
                    }
                }

                MotionEvent.ACTION_UP -> if (max(
                        abs((lastX - x).toDouble()),
                        abs((lastY - y).toDouble())
                    ) <= 5
                ) {
                    val temp: String = largeAccount
                    largeAccount = smallAccount
                    smallAccount = temp
                }

            }
            true
        }

    //拒绝对方的邀请
    private fun doRefuseCall() {
        Log.d("ywh", "doRefuseCall: $channelId  $calleeAccountId $requestId")
        val offlineEnabled = true
        val params =
            V2NIMSignallingRejectInviteParams.Builder(channelId, calleeAccountId, requestId)
                .offlineEnabled(offlineEnabled)
                .build()
        NIMClient.getService(V2NIMSignallingService::class.java).rejectInvite(params, {
//            AVChatSoundPlayer.instance().stop()
            Log.d("ywh", "成功: ")
            closeSession()
        }, {
            Log.d("ywh", "失败: ${it.code}  ${it.desc}")

            closeSession()
        })


    }

    override fun onClick(v: View) {

        val i = v.id
        if (i == R.id.refuse) { //未接通，拒绝接通
            doRefuseCall()
        } else if (i == R.id.receive) { //接听视频
            doReceiveCall()
        } else if (i == R.id.avchat_linear_layout_1) { //设置禁言
            toggleMute()
        } else if (i == R.id.avchat_linear_layout_2) { //是否关闭/开启相机
            closeCamera()
        } else if (i == R.id.avchat_linear_layout_4) { //是否开启扬声器
            loudspeaker()
        } else if (i == R.id.avchat_switch_camera) { //摄像头的切换
            switchCamera()
        } else if (i == R.id.avchat_video_switch_audio) { //音视频互相转化
            if (isInSwitch) {
                ToastUtils.showLongToast(this, getString(R.string.please_wait))
            } else {
                if (stateCallType == ChannelType.AUDIO.value) { //转视频
                    if (!isConversion) {
                        sendCtrl(NimVideoConstant.TO_VIDEO)
                    }
                } else {
                    sendCtrl(NimVideoConstant.TO_AUDIO)
                }
            }
        } else if (i == R.id.avchat_img_2) { //发起方挂断
            if (isHangUp) {
                closeChannel(NimVideoConstant.BILL)
            } else {
                cancelInviteOther(NimVideoConstant.MISS)
            }
        } else if (i == R.id.render_local_user) {
            switchRender()
        }
    }

    /**
     * ********************** 开关摄像头 **********************
     */
    private fun closeCamera() {
        if (isLocalVideoMuted) {
            NERtcEx.getInstance()
                .muteLocalVideoStream(NERtcVideoStreamType.kNERtcVideoStreamTypeMain, true)
            avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatImg2.setBackgroundResource(
                R.drawable.avchat_video_close_camera_checked
            )
            isLocalVideoMuted = false
            avchatActivityBinding.avchatSurfaceLayout.renderLocalUser.visibility = View.GONE
            avchatActivityBinding.avchatSurfaceLayout.smallSizePreviewCoverImg.visibility =
                View.VISIBLE
        } else {
            NERtcEx.getInstance()
                .muteLocalVideoStream(NERtcVideoStreamType.kNERtcVideoStreamTypeMain, false)
            avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatImg2.setBackgroundResource(
                R.drawable.avchat_video_close_camera_normal
            )
            isLocalVideoMuted = true
            avchatActivityBinding.avchatSurfaceLayout.renderLocalUser.visibility = View.VISIBLE
            avchatActivityBinding.avchatSurfaceLayout.smallSizePreviewCoverImg.visibility =
                View.GONE
        }
    }

    override fun toggleOn(v: View) {
        onClick(v)
    }

    override fun toggleOff(v: View) {
        onClick(v)
    }

    override fun toggleDisable(v: View?) {
    }

    private fun closeSession() {
        finish()
    }

    private fun showNoneCameraPermissionView(show: Boolean) {
        avchatActivityBinding.avchatVideoLayout.avchatVideoPermissionControl.root.visibility =
            if (show) View.VISIBLE else View.GONE
    }

    /**
     * ************************************ 权限检查 ***************************************
     */
    private fun checkPermission() {
        val missedPermissions = NERtc.checkPermission(
            applicationContext
        )
        if (missedPermissions.isNotEmpty()) {
            GlobalPermissionUtils.requestPermissionsWithDescriptionDialog(
                this, getPermissionPurposeLinks(
                    this
                ), permissionCamera, null, object : DialogHelper.DialogListener {
                    override fun onPositiveButtonClicked(
                        instance: DialogHelper?,
                        dialog: DialogInterface?,
                        id: Int,
                        requestCode: Int
                    ) {
                        val showRationalePermissions: MutableList<String> = ArrayList()
                        for (permission in missedPermissions) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    this@AVChatActivity,
                                    permission
                                )
                            ) {
                                showRationalePermissions.add(permission)
                            }
                        }


                        val permissions = missedPermissions.toTypedArray()

                        MPermission.with(this@AVChatActivity)
                            .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                            .permissions(*permissions) // 使用解构来传递数组参数
                            .request()


                    }

                    override fun onNegativeButtonClicked(
                        instance: DialogHelper?,
                        dialog: DialogInterface?,
                        id: Int,
                        requestCode: Int
                    ) {
                    }
                })
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults)

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // 接听来电
    private fun doReceiveCall() {
        showNotify(R.string.connecting)
        shouldEnableToggle = true

        //信令配置
        val v2NIMSignallingConfig = V2NIMSignallingConfig()
        //音视频配置
        val v2NIMSignallingRtcConfig = V2NIMSignallingRtcConfig(rtcChannelName, 60 * 60L, null)

        val setupParams =
            V2NIMSignallingCallSetupParams.Builder(channelId, calleeAccountId, requestId)
                .signallingConfig(v2NIMSignallingConfig)
                .rtcConfig(v2NIMSignallingRtcConfig)
                .build()


        NIMClient.getService(V2NIMSignallingService::class.java).callSetup(setupParams, {
            //call setup success
            Log.d("ywh", "doReceiveCall: ${it.roomInfo}")

            mChannelId = it.roomInfo.channelInfo.channelId


            // 接受邀请成功
            var uid: Long = 0
            for (mUid in it.roomInfo.members) {
                uid = mUid.uid
            }


            NERtcEx.getInstance().joinChannel(
                it.rtcInfo.rtcToken,
                it.roomInfo.channelInfo.channelName, uid, null
            )
            if (stateCallType == V2NIMSignallingChannelType.V2NIM_SIGNALLING_CHANNEL_TYPE_AUDIO.value) {
                NERtcEx.getInstance().setSpeakerphoneOn(false)
            }
            removeTime()
//            AVChatSoundPlayer.instance().stop()
            isHangUp = true
            NERtcEx.getInstance().stopVideoPreview(NERtcVideoStreamType.kNERtcVideoStreamTypeMain)
            setupLocalView(true)
            showVideoInitLayout()


        }, {

            Log.d("ywh", "doReceiveCall: ${it.code} ${it.desc}")
            // 加入频道失败
            ToastUtils.showLongToast(this, getString(R.string.cancel_call))
            closeSession()
        })


    }

    fun showVideoInitLayout() {
        findSurfaceView()
        findVideoViews()
        avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.avchatVideoSwitchAudio.visibility =
            View.VISIBLE
        avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.ivAudioVideo.visibility =
            View.VISIBLE
        avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.avchatVideoTitle.visibility =
            View.INVISIBLE
        avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.avchatVideoTitle.visibility =
            View.INVISIBLE
        avchatActivityBinding.avchatVideoLayout.avchatVideoNotify.visibility = View.INVISIBLE
        avchatActivityBinding.avchatVideoLayout.root.setBackgroundResource(R.color.transparent)

        isInSwitch = false
        enableToggle()
        setTime(true)
        setTopRoot(true)
        setBottomRoot(true)
        if (stateCallType == V2NIMSignallingChannelType.V2NIM_SIGNALLING_CHANNEL_TYPE_VIDEO.value) {
            setMiddleRoot(false)
        }
        setBottomRootFU(true)
        setRefuseReceive(false)
        showNoneCameraPermissionView(false)
        avchatActivityBinding.avchatSurfaceLayout.renderRemoteUser.setOnClickListener(
            viewVideoToggleClickListener
        )
    }

    // 底部控制开关可用
    private fun enableToggle() {
        if (shouldEnableToggle) {
            if (canSwitchCamera) {
                switchCameraToggle!!.enable()
            }
            closeCameraToggle!!.enable()
            muteToggle!!.enable()
            loudspeakerToggle!!.enable()
            avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.avchatImg3.isEnabled =
                true
            shouldEnableToggle = false
        }
    }


    private fun setTime(visible: Boolean) {
        avchatActivityBinding.avchatVideoLayout.avchatVideoTime.visibility =
            if (visible) View.VISIBLE else View.GONE
        if (visible) {
            avchatActivityBinding.avchatVideoLayout.avchatVideoTime.base =
                SystemClock.elapsedRealtime()
            avchatActivityBinding.avchatVideoLayout.avchatVideoTime.start()
        }
    }

    private var viewVideoToggle = true
    private val viewVideoToggleClickListener = View.OnClickListener { _: View? ->
        if (viewVideoToggle) {
            avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.root.visibility =
                View.GONE
            avchatActivityBinding.avchatVideoLayout.avchatVideoMiddleControl.visibility = View.GONE
            avchatActivityBinding.avchatVideoLayout.avchatVideoRefuseReceive.root.visibility =
                View.GONE
            avchatActivityBinding.avchatVideoLayout.avchatRecordLayout.root.visibility = View.GONE
            avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.root.visibility =
                View.GONE
            avchatActivityBinding.avchatVideoLayout.avchatVideoBottomFu.root.visibility = View.GONE
            avchatActivityBinding.avchatVideoLayout.avchatVideoTime.visibility = View.GONE
            avchatActivityBinding.avchatVideoLayout.avchatVideoNetunstable.visibility = View.GONE
        } else {
            avchatActivityBinding.avchatVideoLayout.avchatVideoTopControl.root.visibility =
                View.VISIBLE
            avchatActivityBinding.avchatVideoLayout.avchatVideoMiddleControl.visibility = View.GONE
            avchatActivityBinding.avchatVideoLayout.avchatVideoRefuseReceive.root.visibility =
                View.GONE
            avchatActivityBinding.avchatVideoLayout.avchatRecordLayout.root.visibility = View.GONE
            avchatActivityBinding.avchatVideoLayout.avchatVideoBottomControl.root.visibility =
                View.VISIBLE
            avchatActivityBinding.avchatVideoLayout.avchatVideoBottomFu.root.visibility =
                View.VISIBLE
            avchatActivityBinding.avchatVideoLayout.avchatVideoTime.visibility = View.VISIBLE
            avchatActivityBinding.avchatVideoLayout.avchatVideoNetunstable.visibility = View.GONE
        }
        viewVideoToggle = !viewVideoToggle
    }

    override fun onDestroy() {
        super.onDestroy()
        NIMClient.getService(V2NIMSignallingService::class.java)
            .removeSignallingListener(signallingListener)
        registerObserves(false)


        //界面销毁时强制尝试挂断，防止出现红米Note 4X等手机在切后台点击杀死程序时，实际没有杀死的情况
        try {
            manualHangUp()
        } catch (ignored: java.lang.Exception) {
        }
        AVChatProfile.getInstance().isAVChatting = false
        cancelCallingNotifier()
        //销毁2.0实例
        NERtcEx.getInstance().release()


    }

    // 主动挂断
    private fun manualHangUp() {
        releaseVideo()
        if (wakeLock!!.isHeld) {
            wakeLock!!.release()
        }
    }

    // 主动挂断
    private fun releaseVideo() {
        if (stateCallType == ChannelType.VIDEO.value) {
            if (isReleasedVideo) {
                return
            }

            isReleasedVideo = true
        }
    }

    //关闭频道
    fun closeChannel(eventType: Int) {

        NIMClient.getService(V2NIMSignallingService::class.java)
            .closeRoom(mChannelId, true, "",
                {
                    Log.d("ywh", "success: $it")
                    //close room success
                    sendCustomizeMsg(eventType)
//                    AVChatSoundPlayer.instance().stop()
                    NERtcEx.getInstance().leaveChannel()
                    closeSession()
                },
                {
                    Log.d("ywh", "failure: $it")
                    //close room failure
                    closeSession()
                })

    }

    @SuppressLint("WakelockTimeout")
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                if (!wakeLock!!.isHeld) {
                    wakeLock!!.acquire()
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    companion object {
        @JvmStatic
        fun incomingCall(
            context: Context?,
            calleeAccountId: String?,
            state: Int?,
            isCall: Boolean?,
            channelId: String = "",
            requestId: String = "",
            rtcChannelName: String = "",
        ) {

            val intent = Intent(context, AVChatActivity::class.java)
            intent.putExtra(KEY_IN_CALLING, isCall)
            intent.putExtra(CALLEE_ACCOUNT_ID, calleeAccountId)
            intent.putExtra(STATE_CALL_TYPE, state)
            intent.putExtra(CHANNEL_ID, channelId)
            intent.putExtra(REQUEST_ID, requestId)
            intent.putExtra(RTC_CHANNEL_NAME, rtcChannelName)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

            context?.startActivity(intent)
        }
    }

}
