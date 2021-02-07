package com.ynzhxf.nd.xyfirecontrolapp.util;

import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * 帮助工具类
 * Created by nd on 2018-07-10.
 */

public class HelperTool {
    private static final int MIN_DELAY_TIME = 300;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    /**
     * 创建一个UUID去掉连接符的字符串
     *
     * @return
     */
    public final static String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 判断字符串是否是空值或为空
     *
     * @param query
     * @return
     */
    public final static boolean stringIsEmptyOrNull(String query) {
        if (query == null) {
            return true;
        }
        if (query.length() == 0) {
            return true;
        }
        return false;
    }


    /**
     * 防止快速点击系统未响应，而多次操作
     *
     * @return
     */
    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    /**
     * 获取令牌
     */
    public static String getToken() {
        GloblePlantformDatas datas = GloblePlantformDatas.getInstance();
        if (datas.getLoginInfoBean() != null) {
            return datas.getLoginInfoBean().getToken();
        }
        return null;
    }

    /**
     * 获取当前登陆的用户名
     *
     * @return
     */
    public static String getUsername() {
        GloblePlantformDatas datas = GloblePlantformDatas.getInstance();
        if (datas.getLoginInfoBean() != null) {
            return datas.getLoginInfoBean().getUserName();
        }
        return null;
    }

    /**
     * 将一个对象转换成int
     *
     * @param object
     */
    public static int objectToInt(Object object) {
        int result = -10010;
        try {
            result = (int) Double.parseDouble(object + "");
        } catch (Exception e) {

        }
        return result;
    }

    /**
     * 把时间毫秒转换成时间字符串
     *
     * @return
     */
    public static String MillTimeToStringTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
        return formatter.format(date);
    }

    /**
     * 把时间毫秒转换成时间字符串
     *
     * @return
     */
    public static String MillTimeToStringDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        return formatter.format(date);
    }

    /**
     * 把时间毫秒转换成时间详细字符串
     *
     * @return
     */
    public static String MillTimeToStringDateDetail(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return formatter.format(date);
    }

    /**
     * 把时间毫秒转换成年月日字符串
     *
     * @return
     */
    public static String MillTimeToYearMonth(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return formatter.format(date);
    }

    /**
     * 把时间毫秒转换成年月日字符串
     *
     * @return
     */
    public static String MillTimeToYearMon(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        return formatter.format(date);
    }

    /**
     * 把时间毫秒转换成小时分钟字符串
     *
     * @return
     */
    public static String MillTimeToHour(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return formatter.format(date);
    }

    /**
     * 把时间毫秒转换成小时分钟字符串
     *
     * @return
     */
    public static String MillTimeToDateForFireMain(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy~MM~dd~HH:mm:ss", Locale.CHINA);
        return formatter.format(date);
    }

    /**
     * 将obj转double
     *
     * @param obj
     * @return
     */
    public static double objectToDouble(Object obj) {
        try {
            return Double.parseDouble(obj + "");

        } catch (Exception e) {

        }

        return 0;
    }
}
