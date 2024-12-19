package com.netease.yunxin.kit.chatkit.ui.util;

import android.content.Context;
import android.widget.TextView;

import com.netease.yunxin.kit.chatkit.ui.permission.Link;
import com.netease.yunxin.kit.chatkit.ui.permission.LinkBuilder;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @Describe 转换相关工具类
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

public class ConvertUtils {

    private ConvertUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * dp 转 px
     *
     * @param dpValue dp值ø
     * @return px值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    @Deprecated
    public static String numberConvert(int number) {
        if (number < 9999) return "" + number;
        int exp = (int) (Math.log(number) / Math.log(1000));
        DecimalFormat format = new DecimalFormat("0.0");
        format.setRoundingMode(RoundingMode.DOWN);
        String value = format.format(number / Math.pow(1000, exp));
        return String.format("%s%c", value, "kMBTPE".charAt(exp - 1));
    }


    @Deprecated
    public static String numberConvert(double number, boolean isDecimal) {
        if (number < 1000)
            return isDecimal ? "" + number : String.valueOf((int) number);

        int exp = (int) (Math.log(number) / Math.log(1000));
        DecimalFormat format = new DecimalFormat(isDecimal ? "0.0" : "#");
        format.setRoundingMode(RoundingMode.DOWN);
        String value = format.format(number / Math.pow(1000, exp));
        return String.format("%s%c", value, "kMBTPE".charAt(exp - 1));
    }


    @Deprecated
    public static String numberConvertWithoutDecimal(int number) {
        if (number < 999) return "" + number;
        int exp = (int) (Math.log(number) / Math.log(1000));
        DecimalFormat format = new DecimalFormat("#");
        format.setRoundingMode(RoundingMode.DOWN);
        String value = format.format(number / Math.pow(1000, exp));
        return String.format("%s%c", value, "kMBTPE".charAt(exp - 1));
    }
    public static void stringLinkConvert(TextView textView, List<Link> links, boolean onlyFirst) {
        stringLinkConvert(textView, links, onlyFirst, true);
    }
    public static void stringLinkConvert(TextView textView, List<Link> links, boolean onlyFirst, boolean useLinkMovement) {
        if (links == null || links.isEmpty()) {
            return;
        }
        try {
            LinkBuilder.on(textView)
                    .setFindOnlyFirstMatchesForAnyLink(onlyFirst)
                    .addLinks(links)
                    .setEnableLinkMovement(useLinkMovement)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
