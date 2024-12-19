package com.netease.yunxin.kit.chatkit.ui.notification;

import android.app.Notification;
import android.content.Context;
import android.util.SparseArray;

import com.netease.yunxin.kit.chatkit.ui.config.AVChatOptions;
import com.netease.yunxin.kit.chatkit.ui.model.ITeamDataProvider;
import com.netease.yunxin.kit.chatkit.ui.model.IUserInfoProvider;


/**
 * 云信音视频组件定制化入口
 * Created by winnie on 2017/12/6.
 */

public class AVChatKit {

    private static final SparseArray<Notification> notifications = new SparseArray<>();
    private static Context context;
    private static String account;
    private static boolean mainTaskLaunching;
    private static AVChatOptions avChatOptions;
    private static IUserInfoProvider userInfoProvider;
    private static ITeamDataProvider teamDataProvider;


    public static void init(AVChatOptions avChatOptions) {
        AVChatKit.avChatOptions = avChatOptions;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        AVChatKit.context = context;
    }

    public static String getAccount() {
        return account;
    }

    public static void setAccount(String account) {
        AVChatKit.account = account;
    }

    public static boolean isMainTaskLaunching() {
        return mainTaskLaunching;
    }

    public static void setMainTaskLaunching(boolean mainTaskLaunching) {
        AVChatKit.mainTaskLaunching = mainTaskLaunching;
    }

    /**
     * 获取通知栏提醒数组
     */
    public static SparseArray<Notification> getNotifications() {
        return notifications;
    }

    /**
     * 获取音视频初始化配置
     *
     * @return AVChatOptions
     */
    public static AVChatOptions getAvChatOptions() {
        return avChatOptions;
    }

    /**
     * 获取用户相关资料提供者
     *
     * @return IUserInfoProvider
     */
    public static IUserInfoProvider getUserInfoProvider() {
        return userInfoProvider;
    }

    /**
     * 设置用户相关资料提供者
     *
     * @param userInfoProvider 用户相关资料提供者
     */
    public static void setUserInfoProvider(IUserInfoProvider userInfoProvider) {
        AVChatKit.userInfoProvider = userInfoProvider;
    }

    /**
     * 获取群组数据提供者
     *
     * @return ITeamDataProvider
     */
    public static ITeamDataProvider getTeamDataProvider() {
        return teamDataProvider;
    }

    /**
     * 设置群组数据提供者
     *
     * @param teamDataProvider 群组数据提供者
     */
    public static void setTeamDataProvider(ITeamDataProvider teamDataProvider) {
        AVChatKit.teamDataProvider = teamDataProvider;
    }


}
