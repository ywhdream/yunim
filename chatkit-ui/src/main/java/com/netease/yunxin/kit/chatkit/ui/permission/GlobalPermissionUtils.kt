package com.netease.yunxin.kit.chatkit.ui.permission

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.netease.yunxin.kit.chatkit.ui.R
import com.netease.yunxin.kit.chatkit.ui.perference.PreferencesHelper
import com.netease.yunxin.kit.chatkit.ui.util.ActivityHandler
import com.netease.yunxin.kit.chatkit.ui.view.DialogHelper
import com.tbruyelle.rxpermissions3.Permission
import com.tbruyelle.rxpermissions3.RxPermissions

open class GlobalPermissionUtils {
    companion object {
        var permissionCallbackListener: PermissionCallbackListener? = null

        private val PERMISSION_CAMERA = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
        private val PERMISSION_CAMERA_ANDROID_13 = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES
        )

        private val PERMISSION_VIDEO_AUDIO_CALL = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )

        private val PERMISSION_VOICE = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
        private val PERMISSION_VOICE_ANDROID_13 = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES
        )

        private val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        private val PERMISSIONS_STORAGE_READ = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
        private val PERMISSIONS_STORAGE_ANDROID_13 = arrayOf(
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES
        )

        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
        private val PERMISSIONS_STORAGE_ALL_ANDROID_13 = arrayOf(
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES
        )

        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
        private val PERMISSIONS_STORAGE_VIDEO_IMAGE_ANDROID_13 = arrayOf(
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES
        )

        private val PERMISSIONS_STORAGE_READ_PHONE_STATE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
        )

        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
        private val PERMISSIONS_STORAGE_READ_PHONE_STATE_ANDROID_13 = arrayOf(
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_PHONE_STATE
        )

        private val PERMISSIONS_LOCATION = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        private val PERMISSIONS_CONTACT = arrayOf(
            Manifest.permission.READ_CONTACTS
        )


        var mRxPermissions: RxPermissions? = null

        private fun cameraPermission(activity: Activity) {
            DialogHelper.getInstance().showAppDetailSettings(
                activity,
                activity.getString(R.string.camera_permission), String.format(activity.getString(R.string.text_app_permission_services), activity.getString(R.string.text_permission_to_camera))
            )
        }
        private fun microphonePermission(activity: Activity) {
            DialogHelper.getInstance().showAppDetailSettings(
                activity,
                activity.getString(R.string.text_microphone_permission), String.format(activity.getString(R.string.text_app_permission_services), activity.getString(R.string.text_permission_to_microphone))
            )
        }
        private fun locationPermission(activity: Activity) {
            DialogHelper.getInstance().showAppDetailSettings(
                activity,
                activity.getString(R.string.text_location_permission), String.format(activity.getString(R.string.text_app_permission_services), activity.getString(R.string.text_access_to_location))
            )
        }
        private fun contactPermission(activity: Activity) {
            DialogHelper.getInstance().showAppDetailSettings(
                activity,
                activity.getString(R.string.text_contact_permission), String.format(activity.getString(R.string.text_app_permission_services), activity.getString(R.string.text_access_to_contact))
            )
        }
        private fun storagePermission(activity: Activity) {
            DialogHelper.getInstance().showAppDetailSettings(
                activity,
                activity.getString(R.string.text_storage_permission), String.format(activity.getString(R.string.text_app_permission_services), activity.getString(R.string.text_access_to_storage))
            )
        }

        @JvmStatic
        fun checkPermissions(activity: Activity, permissionLists: Array<String>): Boolean {
            for (permission in permissionLists) {
                if (!checkPermission(activity, permission)) {
                    return false
                }
            }
            return true
        }

        private fun checkPermission(context: Context, permission: String): Boolean {
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                true
            } else ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }





        @JvmStatic
        val permissionCamera: Array<String>
            get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                PERMISSION_CAMERA_ANDROID_13
            } else {
                PERMISSION_CAMERA
            }

        @JvmStatic
        val permissionCameraStorage: Array<String>
            get() {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    PERMISSION_CAMERA_ANDROID_13
                } else {
                    PERMISSION_CAMERA
                }
            }

        @JvmStatic
        val permissionVoice: Array<String>
            get() {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    PERMISSION_VOICE_ANDROID_13
                } else {
                    PERMISSION_VOICE
                }
            }

        @JvmStatic
        val permissionStorage: Array<String>
            get() {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    PERMISSIONS_STORAGE_ANDROID_13
                } else {
                    PERMISSIONS_STORAGE
                }
            }

        @JvmStatic
        val permissionReadStorage: Array<String>
            get() {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    PERMISSIONS_STORAGE_ANDROID_13
                } else {
                    PERMISSIONS_STORAGE_READ
                }
            }

        @JvmStatic
        val permissionReadStorageAll: Array<String>
            get() {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    PERMISSIONS_STORAGE_ALL_ANDROID_13
                } else {
                    PERMISSIONS_STORAGE_READ
                }
            }

        @JvmStatic
        val PermissionReadStorageVideoImage: Array<String>
            get() {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    PERMISSIONS_STORAGE_VIDEO_IMAGE_ANDROID_13
                } else {
                    PERMISSIONS_STORAGE_READ
                }
            }

        @JvmStatic
        val permissionStorageReadPhoneState: Array<String>
            get() {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    PERMISSIONS_STORAGE_READ_PHONE_STATE_ANDROID_13
                } else {
                    PERMISSIONS_STORAGE_READ_PHONE_STATE
                }
            }

        @JvmStatic
        fun isPermissionReadStorage(permissionName: String): Boolean {
            return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
                (permissionName.contains(Manifest.permission.READ_MEDIA_IMAGES)
                        && permissionName.contains(Manifest.permission.READ_MEDIA_AUDIO)
                        && permissionName.contains(Manifest.permission.READ_MEDIA_VIDEO))
            } else {
                (permissionName == Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        @JvmStatic
        val permissionMeetingKit: Array<String>
            get() {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    PERMISSION_CAMERA_MEETING_ANDROID_13
                } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
                    PERMISSION_CAMERA_MEETING12
                } else {
                    PERMISSION_CAMERA_MEETING
                }
            }

        @JvmStatic
        val permissionVideoAudioCall: Array<String>
            get() {
                return PERMISSION_VIDEO_AUDIO_CALL
            }

        @JvmStatic
        val permissionContact: Array<String>
            get() {
                return PERMISSIONS_CONTACT
            }

        @JvmStatic
        val permissionLocation: Array<String>
            get() {
                return PERMISSIONS_LOCATION
            }

        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
        private val PERMISSION_CAMERA_MEETING_ANDROID_13 = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )

        private val PERMISSION_CAMERA_MEETING12 = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )

        private val PERMISSION_CAMERA_MEETING = arrayOf(
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )

        @JvmStatic
        fun checkIsVideoAudioCallPermission(permission: String): Boolean {
            return permissionVideoAudioCall.contains(permission)
        }



        @JvmStatic
        fun requestPermissionsWithDescriptionDialog(mContext: Context, linkSet: List<Link>?, permissionsList: Array<String>?, customPermissionMessageList: HashMap<String?, String?>?, permissionListener: DialogHelper.DialogListener) {
            if (permissionsList == null || permissionsList.size <= 0) {
                return
            }

            var haveNeverAskAgainPermission = false

            val currentActivity = try {
                ActivityHandler.getInstance().currentActivity()
            } catch (e: Exception) {
                null
            }

            if (currentActivity != null && checkHaveNeverAskAgainPermission(permissionsList)) {
                haveNeverAskAgainPermission = true
            }

            var isAllPermissionGranted = true
            val permissionMapList = ArrayList<PermissionObject>()
            val excludedPermissionMapList = arrayListOf<String>(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
            )
            for (permission in permissionsList) {
                if (!excludedPermissionMapList.contains(permission)) {
                    var customPermissionMessage = ""
                    if (customPermissionMessageList != null && !customPermissionMessageList.isEmpty()) {
                        val customPermission = customPermissionMessageList[permission]
                        if (customPermission != null && !customPermission.isEmpty()) {
                            customPermissionMessage = customPermission
                        }
                    }
                    val isPermissionGranted = checkPermission(mContext, permission)
                    permissionMapList.add(
                        PermissionObject(
                            permission,
                            isPermissionGranted,
                            if (customPermissionMessage.isEmpty()) getCustomPermissionPurposeDescription(mContext, permission) else customPermissionMessage
                        )
                    )
                    if (isAllPermissionGranted && !isPermissionGranted) {
                        isAllPermissionGranted = false
                    }
                }
            }
            if (isAllPermissionGranted) {
                permissionListener.onPositiveButtonClicked(DialogHelper.getInstance(), object : DialogInterface {
                    override fun cancel() {}

                    override fun dismiss() {}
                }, 0, 0)

                // after go to settings page to allowed permission, once success allow, clear never ask again permission list data
                if (PreferencesHelper.getNeverAskAgainPermissions()?.isNotEmpty() == true) {
                    PreferencesHelper.saveNeverAskAgainPermissions("")
                }
            } else {
                // show dialog for permission purpose
                showPermissionsPurposeDialog(mContext, haveNeverAskAgainPermission, linkSet, permissionMapList, permissionListener, object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        initRequestPermissionsButtonClickFunction(mContext, permissionsList, object : PermissionResultListener {
                            override fun onPermissionsGranted() {
                                permissionListener.onPositiveButtonClicked(DialogHelper.getInstance(), object : DialogInterface {
                                    override fun cancel() {}

                                    override fun dismiss() {}
                                }, 0, 0)
                            }

                            override fun onPermissionsNotGranted() {
                                permissionListener.onNegativeButtonClicked(DialogHelper.getInstance(), object : DialogInterface {
                                    override fun cancel() {}

                                    override fun dismiss() {}
                                }, 0, 0)
                            }

                            override fun onPermissionsNotGrantedWithNeverAskAgain() {
                                permissionListener.onNegativeButtonClicked(DialogHelper.getInstance(), object : DialogInterface {
                                    override fun cancel() {}

                                    override fun dismiss() {}
                                }, 0, 0)
                            }
                        })
                    }
                })
            }
        }

        private fun getCustomPermissionPurposeDescription(mContext: Context, permission: String): String {
            val appName = mContext.getString(R.string.my_app_name)
            return when (permission) {
                Manifest.permission.READ_EXTERNAL_STORAGE -> mContext.getString(R.string.permission_purpose_description_read_external_storage, appName)
                Manifest.permission.WRITE_EXTERNAL_STORAGE -> mContext.getString(R.string.permission_purpose_description_write_external_storage, appName)
                Manifest.permission.READ_MEDIA_AUDIO -> mContext.getString(R.string.permission_purpose_description_read_media_audio, appName)
                Manifest.permission.READ_MEDIA_VIDEO -> mContext.getString(R.string.permission_purpose_description_read_media_video, appName)
                Manifest.permission.READ_MEDIA_IMAGES -> mContext.getString(R.string.permission_purpose_description_read_media_images, appName)
                Manifest.permission.READ_PHONE_STATE -> mContext.getString(R.string.permission_purpose_description_read_phone_state, appName)
                Manifest.permission.ACCESS_COARSE_LOCATION -> mContext.getString(R.string.permission_purpose_description_access_coarse_location, appName)
                Manifest.permission.ACCESS_FINE_LOCATION -> mContext.getString(R.string.permission_purpose_description_access_fine_location, appName)
                Manifest.permission.READ_CONTACTS -> mContext.getString(R.string.permission_purpose_description_read_contacts, appName, appName)
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN -> mContext.getString(R.string.permission_purpose_description_bluetooth, appName)

                Manifest.permission.CALL_PHONE -> mContext.getString(R.string.permission_purpose_description_call_phone, appName)
                Manifest.permission.CAMERA -> mContext.getString(R.string.permission_purpose_description_camera, appName)
                Manifest.permission.RECORD_AUDIO -> mContext.getString(R.string.permission_purpose_description_record_audio, appName)
                else -> ""
            }
        }

        @JvmStatic
        fun initRequestPermissionsButtonClickFunction(mContext: Context?, permissionLists: Array<String>, permissionResultListener: PermissionResultListener?) {
            initRxPermission(mContext)

            var permissionCount = 0

            val grantedPermissionList = ArrayList<String>()
            val notGrantedPermissionList = ArrayList<String>()
            val notGrantedPermissionListWithNever = ArrayList<String>()

            mRxPermissions?.requestEach(*permissionLists)
                ?.subscribe(object : PermissionBaseSubscriberForV2<Permission>() {
                    override fun onSuccess(data: Permission) {
                        permissionCount++

                        if (data.granted) {
                            // 权限被允许
                            grantedPermissionList.add(data.name)
                        } else if (data.shouldShowRequestPermissionRationale) {
                            // 权限被拒绝
                            notGrantedPermissionList.add(data.name)
                        } else {
                            // 权限被彻底禁止
                            notGrantedPermissionListWithNever.add(data.name)

                            // save never ask again permission
                            var existingList = Gson().fromJson<ArrayList<String>>(PreferencesHelper.getNeverAskAgainPermissions(), object : TypeToken<ArrayList<String>>() {}.type)
                            if (existingList == null) {
                                existingList = arrayListOf()
                                existingList.add(data.name)
                                PreferencesHelper.saveNeverAskAgainPermissions(Gson().toJson(existingList))
                            } else {
                                if (!existingList.contains(data.name)) {
                                    existingList.add(data.name)
                                    PreferencesHelper.saveNeverAskAgainPermissions(Gson().toJson(existingList))
                                }
                            }
                        }

                        // all request permissions result returned, only proceed next step
                        if (permissionCount == permissionLists.size) {
                            if (notGrantedPermissionListWithNever.isNotEmpty()) {
                                permissionResultListener?.onPermissionsNotGrantedWithNeverAskAgain()
                            } else if (notGrantedPermissionList.isNotEmpty()) {
                                permissionResultListener?.onPermissionsNotGranted()
                            } else if (grantedPermissionList.size == permissionLists.size) {
                                // once granted permission, clear never ask again permission list data
                                PreferencesHelper.saveNeverAskAgainPermissions("")
                                permissionResultListener?.onPermissionsGranted()
                            }
                        }
                    }
                })
        }

        private fun initRxPermission(mContext: Context?) {
            mRxPermissions = try {
                RxPermissions(mContext as FragmentActivity)
            } catch (e: Exception) {
                RxPermissions(mContext as Fragment)
            } catch (e: Exception) {
                null
            }
        }

        @JvmStatic
        fun isOverMarshmallow(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        }

        @TargetApi(value = Build.VERSION_CODES.M)
        fun hasNeverAskAgainPermission(context: Context, permission: List<String?>): Boolean {
            try {
                if (!isOverMarshmallow()) {
                    return false
                }

                for (value in permission) {
                    if (context.checkSelfPermission(value!!) != PackageManager.PERMISSION_GRANTED &&
                        ActivityHandler.getInstance().currentActivity() != null
                    ) {
                        return !ActivityHandler.getInstance().currentActivity().shouldShowRequestPermissionRationale(value)
                    }
                }

                return false
            } catch (e: Exception) {
                return false
            }
        }

        fun checkHaveNeverAskAgainPermission(permissionsList: Array<String>?): Boolean {
            val neverAskAgainPermissionList = Gson().fromJson<ArrayList<String>>(PreferencesHelper.getNeverAskAgainPermissions(), object : TypeToken<ArrayList<String>>() {}.type)

            permissionsList?.forEach { mPermissionName ->
                neverAskAgainPermissionList?.forEach { mPermissionNameExisting ->
                    if (mPermissionName == mPermissionNameExisting) {
                        return true
                    }
                }
            }
            return false
        }

        interface PermissionCallbackListener {
            fun onPermissionCallback(permission: Permission?)
        }


        interface PermissionResultListener {
            fun onPermissionsGranted()
            fun onPermissionsNotGranted()
            fun onPermissionsNotGrantedWithNeverAskAgain()
        }
    }


}