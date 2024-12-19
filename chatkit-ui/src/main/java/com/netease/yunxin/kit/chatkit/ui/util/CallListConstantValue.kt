package com.netease.yunxin.kit.chatkit.ui.util

/**
 * @author chenkooichuin
 * Created 07/04/2023 at 11:06 AM
 *
 * @description
 **/
class CallListConstantValue {

    enum class CallType {
        CALL_TYPE_VOICE,
        CALL_TYPE_VIDEO
    }

    enum class GroupType {
        GROUP_TYPE_INDIVIDUAL,
        GROUP_TYPE_GROUP
    }

    enum class ActionType {
        ACTION_TYPE_ACCEPT,
        ACTION_TYPE_REJECT,
        ACTION_TYPE_MISSED,
        ACTION_TYPE_END
    }

    companion object {
        private const val CALL_TYPE_VOICE = "voice"
        private const val CALL_TYPE_VIDEO = "video"

        private const val GROUP_TYPE_INDIVIDUAL = "individual"
        private const val GROUP_TYPE_GROUP = "group"

        private const val ACTION_TYPE_ACCEPT = "accept"
        private const val ACTION_TYPE_REJECT = "reject"
        private const val ACTION_TYPE_MISSED = "missed"
        private const val ACTION_TYPE_END = "end"

        @JvmStatic
        fun getCallTypeValue(callType: CallType): String {
            return when(callType){
                CallType.CALL_TYPE_VOICE -> CALL_TYPE_VOICE
                else -> CALL_TYPE_VIDEO
            }
        }

        @JvmStatic
        fun getGroupTypeValue(groupType: GroupType): String {
            return when(groupType){
                GroupType.GROUP_TYPE_INDIVIDUAL -> GROUP_TYPE_INDIVIDUAL
                else -> GROUP_TYPE_GROUP
            }
        }

        @JvmStatic
        fun getActionTypeValue(actionType: ActionType): String {
            return when(actionType){
                ActionType.ACTION_TYPE_ACCEPT -> ACTION_TYPE_ACCEPT
                ActionType.ACTION_TYPE_REJECT -> ACTION_TYPE_REJECT
                ActionType.ACTION_TYPE_MISSED -> ACTION_TYPE_MISSED
                else -> ACTION_TYPE_END
            }
        }
    }
}