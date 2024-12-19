package com.netease.yunxin.kit.chatkit.ui.util;

import android.app.Notification;
import android.content.Context;
import android.util.SparseArray;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.netease.yunxin.kit.chatkit.ui.notification.AVChatKit;
import com.netease.yunxin.kit.chatkit.ui.perference.PreferencesHelper;


public class ApplicationCache {

    //TODO move pure to use Preference helper
    private static String account;

    public static Context context;

    private static StatusBarNotificationConfig notificationConfig;

    public static void clear() {
        account = null;
        userInfo = null;
    }

    public static String getAccount() {
        return account;
    }

    private static boolean mainTaskLaunching;

    public static void setAccount(String account) {
        ApplicationCache.account = account;
//        NimUIKit.setAccount(account);
        AVChatKit.setAccount(account);
//        RTSKit.setAccount(account);
    }

    public static void setNotificationConfig(StatusBarNotificationConfig notificationConfig) {
        ApplicationCache.notificationConfig = notificationConfig;
    }

    public static StatusBarNotificationConfig getNotificationConfig() {
        return notificationConfig;
    }


    public static void setContext(Context context) {
        ApplicationCache.context = context.getApplicationContext();
        PreferencesHelper.setContext(context);
//        AVChatKit.setContext(context);
//        RTSKit.setContext(context);
    }


    public static Context getContext() {
        return context;
    }

    public static void setMainTaskLaunching(boolean mainTaskLaunching) {
        ApplicationCache.mainTaskLaunching = mainTaskLaunching;

//        AVChatKit.setMainTaskLaunching(mainTaskLaunching);
    }

    public static boolean isMainTaskLaunching() {
        return mainTaskLaunching;
    }

    /*
     * Customize
     * */
    private static NimUserInfo userInfo;

    private static String togaNickName;

    private static String appkey = "0e93f53b5b02e29ca3eb6f37da3b05b9";

//    private static LoginModel loginModel;

    private static String vodtoken;

//    private static IUserInfoProvider<UserInfo> userInfoProvider;
//    private static ITeamDataProvider teamDataProvider;
//    private static ILogUtil iLogUtil;
//
//    public static String getUID() {
//        return getTogaAccountInfo() != null ? getTogaAccountInfo().getUid() : null;
//    }
//
//    public static String getCoverPhoto() {
//        return getTogaAccountInfo().getCover();
//    }
//
//    public static void setCoverPhoto(String coverPhoto) {
//        getTogaAccountInfo().setCover(coverPhoto);
//    }
//
//    public static String getTogaNickName() {
//        return getTogaAccountInfo().getNickname() == null ? "" : getTogaAccountInfo().getNickname();
//    }

    public static String getAppkey() {
        return appkey;
    }

    public static NimUserInfo getUserInfo() {
        if (userInfo == null) {
            userInfo = NIMClient.getService(UserService.class).getUserInfo(account);
        }

        return userInfo;
    }
//
//    public static void setTogaAccountInfo(LoginModel loginModel) {
//        ApplicationCache.loginModel = loginModel;
//    }
//
//    public static LoginModel getTogaAccountInfo() {
//        return ApplicationCache.loginModel;
//    }
//
//    private static SparseArray<Notification> notifications = new SparseArray<>();
//
//    public static SparseArray<Notification> getNotifications() {
//        return notifications;
//    }
//
//    public static ITeamDataProvider getTeamDataProvider() {
//        return teamDataProvider;
//    }
//
//    public static void setTeamDataProvider(ITeamDataProvider teamDataProvider) {
//        ApplicationCache.teamDataProvider = teamDataProvider;
//    }
//
//    public static void setiLogUtil(ILogUtil iLogUtil) {
//        ApplicationCache.iLogUtil = iLogUtil;
//    }
//
//    public static ILogUtil getiLogUtil() {
//        return iLogUtil;
//    }


    public static String getVodtoken() {
        return vodtoken;
    }

    //TODO: loo -for future use netease cloud
    public static void setVodtoken(String vodtoken) {
        ApplicationCache.vodtoken = vodtoken;
    }

    private static final String API_SERVER_TEST = "http://223.252.220.238:8080/appdemo"; // 测试
    private static final String API_SERVER = "https://app.netease.im/appdemo"; // 线上

    public static final String apiServer() {
        return API_SERVER_TEST;
    }
}
