package com.ynzhxf.nd.xyfirecontrolapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.blankj.utilcode.util.SPUtils;
import com.ynzhxf.nd.xyfirecontrolapp.bean.platform.LoginInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.GloblePars;

import java.util.Map;

/**
 * 平台全局数据缓存对象
 * Created by nd on 2018-07-12.
 */

public class GloblePlantformDatas {

    private static GloblePlantformDatas datas = new GloblePlantformDatas();

    /**
     * 实时数据请求间隔时间
     */
    public static int realDataUpdateTime = 3000;

    /**
     * 实时报警请求间隔时间
     */
    public static int realAlarmUpdateTime = 3000;


    //用户消息数量获取刷新时间
    public static int messageLogCountTime = 3000;

    //用户动态更新消息列表的时间
    public static int messageFlushTime = 3000;

    //用户未读消息数量
    public static int UserNotSeeCount = 0;


    //自定义的全局UI旋转动画线程是否运行
    public static boolean isRealUIRotateThreadRun = false;
    //自定义的全局UI位移动画线程是否运行
    public static boolean isRealUIMoveThreadRun = false;


    /**
     * 用户登陆信息缓存
     */
    private LoginInfoBean loginInfoBean;

    private GloblePlantformDatas() {
        /*Load();*/
    }

    public static GloblePlantformDatas getInstance() {
        if (datas == null) {
            datas = new GloblePlantformDatas();
        }
        return datas;
    }

    /**
     * 加载数据
     */
    private void Load() {
        loginInfoBean = new LoginInfoBean();
        //createTestLoginBean();//加载一个测试对象，正式发布删除
    }

    public void createTestLoginBean() {
        loginInfoBean.setToken("435c15834c414bc9af094961a48b6fde");
        loginInfoBean.setDeviceUUID("000000000000000");
        loginInfoBean.setDevicePlatform("android");
        loginInfoBean.setUserName("test111");
        loginInfoBean.setOrgName("云南省消防总队");
    }

    /**
     * 获取用户登信息陆对象
     *
     * @return
     */
    public LoginInfoBean getLoginInfoBean() {
        return loginInfoBean;
    }

    /**
     * 从SharedPreferences中读取用户信息，并将信息读取到缓存中
     */
    public void LoadLoginInfoBean(Context context) {
        //SharedPreferences pre = context.getSharedPreferences(GloblePars.USER_LOGIN_INFO, context.MODE_PRIVATE);
        if (loginInfoBean == null) {
            loginInfoBean = new LoginInfoBean();
        }
        loginInfoBean.setToken(SPUtils.getInstance().getString("token", null));
        loginInfoBean.setUserName(SPUtils.getInstance().getString("username", null));
        loginInfoBean.setDeviceUUID(SPUtils.getInstance().getString("deviceID", null));
        loginInfoBean.setDevicePlatform(SPUtils.getInstance().getString("devicePlatform", null));

        loginInfoBean.setOrgddress(SPUtils.getInstance().getString("OrgAddr"));
        loginInfoBean.setOrgName(SPUtils.getInstance().getString("OrgName"));
        loginInfoBean.setPhone(SPUtils.getInstance().getString("Phone"));
    }

    /**
     * //存储登陆信息数据到本地,更新缓存用户信息
     *
     * @param context
     * @param resultBean
     */
    public void saveLoginInfoBean(Context context, ResultBean<LoginInfoBean, Map<String, String>> resultBean) {
//        SharedPreferences.Editor editor = context.getSharedPreferences(GloblePars.USER_LOGIN_INFO, context.MODE_PRIVATE).edit();
//        editor.putString("token", resultBean.getData().getToken());
//        editor.putString("deviceID", resultBean.getData().getDeviceUUID());
//        editor.putString("username", resultBean.getData().getUserName());
//        editor.putString("devicePlatform", resultBean.getData().getDevicePlatform());
//        editor.apply();
        SPUtils.getInstance().put("token", resultBean.getData().getToken());
        SPUtils.getInstance().put("deviceID", resultBean.getData().getDeviceUUID());
        SPUtils.getInstance().put("username", resultBean.getData().getUserName());
        SPUtils.getInstance().put("devicePlatform", resultBean.getData().getDevicePlatform());

        SPUtils.getInstance().put("OrgAddr", resultBean.getExtra().get("OrgAddr"));
        SPUtils.getInstance().put("OrgName", resultBean.getExtra().get("OrgName"));
        SPUtils.getInstance().put("Phone", resultBean.getExtra().get("Phone"));

        loginInfoBean = resultBean.getData();
        loginInfoBean.setOrgddress(resultBean.getExtra().get("OrgAddr"));
        loginInfoBean.setOrgName(resultBean.getExtra().get("OrgName"));
        loginInfoBean.setPhone(resultBean.getExtra().get("Phone"));
    }

    /**
     * 清除用户权限信息
     *
     * @param context
     */
    public void clearLoginInfo(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(GloblePars.USER_LOGIN_INFO, context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        SPUtils.getInstance().clear();
        loginInfoBean = new LoginInfoBean();
    }


}
