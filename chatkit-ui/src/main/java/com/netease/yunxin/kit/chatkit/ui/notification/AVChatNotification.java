package com.netease.yunxin.kit.chatkit.ui.notification;

import static androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.netease.yunxin.kit.chatkit.ui.R;
import com.netease.yunxin.kit.chatkit.ui.avtivity.AVChatActivity;
import com.netease.yunxin.kit.chatkit.ui.constant.Constant;


/**
 * 音视频聊天通知栏
 * Created by huangjun on 2015/5/14.
 */
public class AVChatNotification {

    private final Context context;

    private NotificationManager notificationManager;
    private Notification callingNotification;
    private Notification missCallNotification;
    private String account;
    private String displayName;
    private static final int NOTIFY_ID = AVChatNotification.class.hashCode();
    private static final int CALLING_NOTIFY_ID = 111;
    private static final int MISS_CALL_NOTIFY_ID = 112;
    private static final int JUST_CALL_NOTIFY_ID = 113;
    public String CHANNEL_ID_HIGH = "AVChatHigh";
    public String CHANNEL_ID_DEFAULT = "AVChatDefault";
    public String CHANNEL_NAME = "AVChatNotification";

    public AVChatNotification(Context context) {
        this.context = context;
    }

    public void init(String account, String displayName) {
        this.account = account;
        this.displayName = displayName;

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        AVChatNotificationChannelCompat26.createNIMMessageNotificationChannel(context);
    }

    private void buildCallingNotification() {
        if (callingNotification == null) {
            Intent localIntent = new Intent();
            localIntent.setClass(context, AVChatActivity.class);
            localIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            int pendingFlags;
//            if (Util.SDK_INT >= 23) {
//                pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
//            } else {
//                pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT;
//            }
            pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;

            PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFY_ID, localIntent, pendingFlags);

            String title = context.getString(R.string.chatroom_call_incoming);
            String tickerText = displayName;
//            int iconId = AVChatKit.getAvChatOptions().notificationIconRes;
            int iconId = R.drawable.avchat_audio_record_icon_checked;

            callingNotification = makeNotification(pendingIntent, title, tickerText, tickerText, iconId, false, false);
        }
    }

    private void buildMissCallNotification() {
        if (missCallNotification == null) {
            Intent notifyIntent = new Intent(context, AVChatKit.getAvChatOptions().entranceActivity);
            notifyIntent.putExtra(Constant.EXTRA_ACCOUNT, account);
            notifyIntent.putExtra(Constant.EXTRA_FROM_NOTIFICATION, true);
            notifyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            notifyIntent.setAction(Intent.ACTION_VIEW);
            notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            int pendingFlags;
//            if (Util.SDK_INT >= 23) {
//                pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
//            } else {
//                pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT;
//            }
            pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;

            PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFY_ID, notifyIntent, pendingFlags);

            String title = context.getString(R.string.chatroom_miss_call);
            String tickerText = displayName;
//            int iconId = AVChatKit.getAvChatOptions().notificationIconRes;
            int iconId = R.drawable.avchat_audio_record_icon_checked;


            missCallNotification = makeNotification(pendingIntent, title, tickerText, tickerText, iconId, true, true);
        }
    }

    private Notification makeNotification(PendingIntent pendingIntent, String title, String content, String tickerText,
                                          int iconId, boolean ring, boolean vibrate) {
        return makeNotification(pendingIntent, title, content, tickerText, iconId, ring, vibrate, false);
    }

    private Notification makeNotification(PendingIntent pendingIntent, String title, String content, String tickerText,
                                          int iconId, boolean ring, boolean vibrate, boolean highPriority) {
        NotificationCompat.Builder builder;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(highPriority ? CHANNEL_ID_HIGH : CHANNEL_ID_DEFAULT, CHANNEL_NAME,
                    highPriority ? NotificationManager.IMPORTANCE_HIGH : NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, highPriority ? CHANNEL_ID_HIGH : CHANNEL_ID_DEFAULT);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setPriority(highPriority ? Notification.PRIORITY_MAX : Notification.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setTicker(tickerText)
                .setSmallIcon(iconId)
                .setVisibility(VISIBILITY_PUBLIC);

        int defaults = Notification.DEFAULT_LIGHTS;
        if (vibrate) {
            defaults |= Notification.DEFAULT_VIBRATE;
        }
        if (ring) {
            defaults |= Notification.DEFAULT_SOUND;
        }
        builder.setDefaults(defaults);

        return builder.build();
    }

    public void activeCallingNotification(boolean active) {
        if (notificationManager != null) {
            if (active) {
                buildCallingNotification();
                notificationManager.notify(NOTIFY_ID, callingNotification);
                AVChatKit.getNotifications().put(NOTIFY_ID, callingNotification);
            } else {
                notificationManager.cancel(NOTIFY_ID);
                AVChatKit.getNotifications().remove(NOTIFY_ID);
            }
        }
    }

    public void activeMissCallNotification(boolean active) {
        if (notificationManager != null) {
            if (active) {
                buildMissCallNotification();
                notificationManager.notify(NOTIFY_ID, missCallNotification);
                AVChatKit.getNotifications().put(NOTIFY_ID, callingNotification);
            } else {
                notificationManager.cancel(NOTIFY_ID);
                AVChatKit.getNotifications().remove(NOTIFY_ID);
            }
        }
    }

    public void activeJustCallNotification() {
        if (notificationManager != null) {
            Intent localIntent = new Intent();
            //localIntent.setClass(context, DashboardActivity.class);
            localIntent.setClass(context, AVChatActivity.class);
            localIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            localIntent.setAction(Intent.ACTION_VIEW);

            String title = context.getString(R.string.chatroom_call_general);
            String tickerText = displayName;
//            int iconId = AVChatKit.getAvChatOptions().notificationIconRes;
            int iconId = R.drawable.avchat_audio_record_icon_checked;

            int pendingFlags;
//            if (Util.SDK_INT >= 23) {
//                pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
//            } else {
//                pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT;
//            }
            pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;

            PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFY_ID, localIntent, pendingFlags);
            callingNotification = makeNotification(pendingIntent, title, tickerText, tickerText, iconId, false, false, true);
            notificationManager.notify(NOTIFY_ID, callingNotification);

        }
    }
}
