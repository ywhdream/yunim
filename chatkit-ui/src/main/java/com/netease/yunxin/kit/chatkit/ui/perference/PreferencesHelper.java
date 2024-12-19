package com.netease.yunxin.kit.chatkit.ui.perference;


import android.content.Context;
import android.content.SharedPreferences;



public class PreferencesHelper implements IPreferencesHelper {
    public static final String KEY_NEVER_ASK_AGAIN_PERMISSIONS = "key_never_ask_again_permissions";
    private static Context mContext;
    public static void setContext(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        return mContext;
    }
    private static SharedPreferences getSharedPreferences() {
        return getContext().getSharedPreferences("Yippi", Context.MODE_PRIVATE);
    }
    private static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }
    private static String getString(String key, String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }

    public static void saveNeverAskAgainPermissions(String location) {
        saveString(KEY_NEVER_ASK_AGAIN_PERMISSIONS, location);
    }

    public static String getNeverAskAgainPermissions() {
        return getString(KEY_NEVER_ASK_AGAIN_PERMISSIONS, "");
    }

}
