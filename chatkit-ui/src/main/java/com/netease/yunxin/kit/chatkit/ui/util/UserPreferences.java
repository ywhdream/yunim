package com.netease.yunxin.kit.chatkit.ui.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;

public class UserPreferences {

    private final static String KEY_DOWNTIME_TOGGLE = "down_time_toggle";
    private final static String KEY_SB_NOTIFY_TOGGLE = "sb_notify_toggle";
    private final static String KEY_AV_NOTIFY_TOGGLE = "av_notify_toggle";
    private final static String KEY_TEAM_ANNOUNCE_CLOSED = "team_announce_closed";
    private final static String KEY_STATUS_BAR_NOTIFICATION_CONFIG = "KEY_STATUS_BAR_NOTIFICATION_CONFIG";

    // 测试过滤通知
    private final static String KEY_MSG_IGNORE = "KEY_MSG_IGNORE";
    // 响铃配置
    private final static String KEY_RING_TOGGLE = "KEY_RING_TOGGLE";

    // 震动配置
    private final static String KEY_VIBRATE_TOGGLE = "KEY_VIBRATE_TOGGLE";
    // 呼吸灯配置
    private final static String KEY_LED_TOGGLE = "KEY_LED_TOGGLE";
    // 通知栏标题配置
    private final static String KEY_NOTICE_CONTENT_TOGGLE = "KEY_NOTICE_CONTENT_TOGGLE";

    // 通知栏样式（展开、折叠）配置
    private final static String KEY_NOTIFICATION_FOLDED_TOGGLE = "KEY_NOTIFICATION_FOLDED";

    // 保存在线状态订阅时间
    private final static String KEY_SUBSCRIBE_TIME = "KEY_SUBSCRIBE_TIME";

    private final static String KEY_LIKE_SETTING = "KEY_LIKE_SETTING";
    private final static String KEY_MENTION_SETTING = "KEY_MENTION_SETTING";
    private final static String KEY_COMMENT_SETTING = "KEY_COMMENT_SETTING";
    private final static String KEY_LIVE_SETTING = "KEY_LIVE_SETTING";
    private final static String KEY_FOLLOWER_SETTING = "KEY_FOLLOWER_SETTING";
    private final static String KEY_SUBSCRIBER_SETTING = "KEY_SUBSCRIBER_SETTING";

    public static void setMsgIgnore(boolean enable) {
        saveBoolean(KEY_MSG_IGNORE, enable);
    }

    public static boolean getMsgIgnore() {
        return getBoolean(KEY_MSG_IGNORE, false);
    }

    public static void setNotificationToggle(boolean on) {
        saveBoolean(KEY_SB_NOTIFY_TOGGLE, on);
    }

    public static boolean getNotificationToggle() {
        return getBoolean(KEY_SB_NOTIFY_TOGGLE, true);
    }

    public static void setAVNotificationToggle(boolean on) {
        saveBoolean(KEY_AV_NOTIFY_TOGGLE, on);
    }

    public static boolean getAVNotificationToggle() {
        return getBoolean(KEY_AV_NOTIFY_TOGGLE, true);
    }

    public static void setRingToggle(boolean on) {
        saveBoolean(KEY_RING_TOGGLE, on);
    }

    public static boolean getRingToggle() {
        return getBoolean(KEY_RING_TOGGLE, true);
    }

    public static void setVibrateToggle(boolean on) {
        saveBoolean(KEY_VIBRATE_TOGGLE, on);
    }

    public static boolean getVibrateToggle() {
        return getBoolean(KEY_VIBRATE_TOGGLE, true);
    }

    public static void setLedToggle(boolean on) {
        saveBoolean(KEY_LED_TOGGLE, on);
    }

    public static boolean getLedToggle() {
        return getBoolean(KEY_LED_TOGGLE, true);
    }

    public static boolean getNoticeContentToggle() {
        return getBoolean(KEY_NOTICE_CONTENT_TOGGLE, false);
    }

    public static void setNoticeContentToggle(boolean on) {
        saveBoolean(KEY_NOTICE_CONTENT_TOGGLE, on);
    }

    public static void setDownTimeToggle(boolean on) {
        saveBoolean(KEY_DOWNTIME_TOGGLE, on);
    }

    public static boolean getDownTimeToggle() {
        return getBoolean(KEY_DOWNTIME_TOGGLE, false);
    }

    public static void setNotificationFoldedToggle(boolean folded) {
        saveBoolean(KEY_NOTIFICATION_FOLDED_TOGGLE, folded);
    }

    public static boolean getNotificationFoldedToggle() {
        return getBoolean(KEY_NOTIFICATION_FOLDED_TOGGLE, true);
    }

    public static void setStatusConfig(StatusBarNotificationConfig config) {
        saveStatusBarNotificationConfig(KEY_STATUS_BAR_NOTIFICATION_CONFIG, config);
    }

    public static StatusBarNotificationConfig getStatusConfig() {
        return getConfig(KEY_STATUS_BAR_NOTIFICATION_CONFIG);
    }

    public static void setTeamAnnounceClosed(String teamId, boolean closed) {
        saveBoolean(KEY_TEAM_ANNOUNCE_CLOSED + teamId, closed);
    }

    public static boolean getTeamAnnounceClosed(String teamId) {
        return getBoolean(KEY_TEAM_ANNOUNCE_CLOSED + teamId, false);
    }

    public static void setOnlineStateSubsTime(long time) {
        saveLong(KEY_SUBSCRIBE_TIME, time);
    }

    public static long getOnlineStateSubsTime() {
        return getLong(KEY_SUBSCRIBE_TIME, 0);
    }

    public static void saveLikeSetting(int value) {
        saveInt(KEY_LIKE_SETTING, value);
    }

    public static int getLikeSetting() {
        return getInt(KEY_LIKE_SETTING, 1);
    }

    public static void saveMentionSetting(int value) {
        saveInt(KEY_MENTION_SETTING, value);
    }

    public static int getMentionSetting() {
        return getInt(KEY_MENTION_SETTING, 1);
    }

    public static void saveCommentSetting(int value) {
        saveInt(KEY_COMMENT_SETTING, value);
    }

    public static int getCommentSetting() {
        return getInt(KEY_COMMENT_SETTING, 1);
    }

    public static void setNewFollowerToggle(boolean state) {
        saveBoolean(KEY_FOLLOWER_SETTING, state);
    }

    public static boolean getNewFollowerToggle() {
        return getBoolean(KEY_FOLLOWER_SETTING, true);
    }

    public static void setSubscriberToggle(boolean closed) {
        saveBoolean(KEY_SUBSCRIBER_SETTING, closed);
    }

    public static boolean getSubscriberToggle() {
        return getBoolean(KEY_SUBSCRIBER_SETTING, true);
    }

    public static void setLiveToggle(boolean closed) {
        saveBoolean(KEY_TEAM_ANNOUNCE_CLOSED, closed);
    }

    public static boolean getLiveToggle() {
        return getBoolean(KEY_TEAM_ANNOUNCE_CLOSED, true);
    }

    private static StatusBarNotificationConfig getConfig(String key) {
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        String jsonString = getSharedPreferences().getString(key, "");
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            if (jsonObject == null) {
                return null;
            }
            config.downTimeBegin = jsonObject.getString("downTimeBegin");
            config.downTimeEnd = jsonObject.getString("downTimeEnd");
            config.downTimeToggle = jsonObject.getBoolean("downTimeToggle");
            config.ring = jsonObject.getBoolean("ring");
            config.vibrate = jsonObject.getBoolean("vibrate");
            config.notificationSmallIconId = jsonObject.getIntValue("notificationSmallIconId");
            config.notificationSound = jsonObject.getString("notificationSound");
            config.hideContent = jsonObject.getBoolean("hideContent");
            config.ledARGB = jsonObject.getIntValue("ledargb");
            config.ledOnMs = jsonObject.getIntValue("ledonms");
            config.ledOffMs = jsonObject.getIntValue("ledoffms");
            config.titleOnlyShowAppName = jsonObject.getBoolean("titleOnlyShowAppName");
            if (jsonObject.containsKey("notificationFolded")) {
                config.notificationFolded = jsonObject.getBoolean("notificationFolded");
            }
            config.notificationEntrance = (Class<? extends Activity>) Class.forName(jsonObject.getString("notificationEntrance"));
            config.notificationColor = jsonObject.getInteger("notificationColor");
            config.downTimeEnableNotification = jsonObject.getBoolean("downTimeEnableNotification");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return config;
    }

    private static void saveStatusBarNotificationConfig(String key, StatusBarNotificationConfig config) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("downTimeBegin", config.downTimeBegin);
            jsonObject.put("downTimeEnd", config.downTimeEnd);
            jsonObject.put("downTimeToggle", config.downTimeToggle);
            jsonObject.put("ring", config.ring);
            jsonObject.put("vibrate", config.vibrate);
            jsonObject.put("notificationSmallIconId", config.notificationSmallIconId);
            jsonObject.put("notificationSound", config.notificationSound);
            jsonObject.put("hideContent", config.hideContent);
            jsonObject.put("ledargb", config.ledARGB);
            jsonObject.put("ledonms", config.ledOnMs);
            jsonObject.put("ledoffms", config.ledOffMs);
            jsonObject.put("titleOnlyShowAppName", config.titleOnlyShowAppName);
            jsonObject.put("notificationFolded", config.notificationFolded);
            jsonObject.put("notificationEntrance", config.notificationEntrance.getName());
            jsonObject.put("notificationColor", config.notificationColor);
            jsonObject.put("downTimeEnableNotification", config.downTimeEnableNotification);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.putString(key, jsonObject.toString());
        editor.commit();
    }

    private static boolean getBoolean(String key, boolean value) {
        return getSharedPreferences().getBoolean(key, value);
    }

    private static void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private static void saveLong(String key, long value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(key, value);
        editor.commit();
    }

    private static long getLong(String key, long value) {
        return getSharedPreferences().getLong(key, value);
    }

    private static int getInt(String key, int value) {
        return getSharedPreferences().getInt(key, value);
    }

    private static void saveInt(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    static SharedPreferences getSharedPreferences() {
        return ApplicationCache.getContext().getSharedPreferences("Demo." + ApplicationCache.getAccount(), Context.MODE_PRIVATE);
    }
}