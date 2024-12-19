package com.netease.yunxin.kit.chatkit.ui.util;

import android.os.Handler;

import com.netease.yunxin.kit.chatkit.ui.notification.AVChatKit;


public class AVChatProfile {
    private boolean isAVChatting = false;
    private final Runnable launchTimeout = () -> {
        // If didn't start successfully, restore av chatting -> false
        setAVChatting(false);
    };

    public static AVChatProfile getInstance() {
        return InstanceHolder.instance;
    }

    public boolean isAVChatting() {
        return isAVChatting;
    }

    public void setAVChatting(boolean chatting) {
        isAVChatting = chatting;
    }

    public void launchActivity(
            final String displayName,
            final int source
    ) {
        Runnable runnable = () -> {
            // Please wait while the task starting
            if (!AVChatKit.isMainTaskLaunching()) {
                launchActivityTimeout();
               // AVChatActivity.incomingCall(AVChatKit.getContext(), displayName, source);
            } else {
                launchActivity( displayName, source);
            }
        };
        Handlers.sharedHandler(AVChatKit.getContext()).postDelayed(runnable, 200);
    }

    public void activityLaunched() {
        Handler handler = Handlers.sharedHandler(AVChatKit.getContext());
        handler.removeCallbacks(launchTimeout);
    }

    /**
     * Some devices (such as OPPO and VIVO) do not allow the
     * activity to be started from the background broadcast Receiver by default.
     * Added a timeout mechanism for starting an activity
     */
    private void launchActivityTimeout() {
        Handler handler = Handlers.sharedHandler(AVChatKit.getContext());
        handler.removeCallbacks(launchTimeout);
        handler.postDelayed(launchTimeout, 3000);
    }

    private static class InstanceHolder {
        public final static AVChatProfile instance = new AVChatProfile();
    }
}