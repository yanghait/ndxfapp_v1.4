package com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.impl;

import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.ICheckLoginPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.ILoginKeyGetPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.ILoginOutPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.ILoginPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IUserInfoGetPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IUserPushStatePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IUserPushStateSettingPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IUserPwdChangePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IVersionCheckPersenter;

/**
 * 平台及安全操作实例创建
 * Created by nd on 2018-07-14.
 */

public class PlatfromPersenterFactory {


    /**
     * 获取APP版本号
     * @param view
     * @return
     */
    public static IVersionCheckPersenter getVersionCheckPersenterImpl(IVersionCheckPersenter.IVersionCheckView view){
        return new VersionCheckPersenterImpl(view);
    }

    /**
     *登陆处理
     * @param view
     * @return
     */
    public static ILoginPersenter getLoginPersenterImpl(ILoginPersenter.ILoginView view){
        return new LoginPersenterImpl(view);
    }

    /**
     * 用户权限检测
     * @param view
     * @return
     */
    public static  ICheckLoginPersenter getCheckLoginImpl(ICheckLoginPersenter.ICheckLoginView view){
        return new CheckLoginPersenterImpl(view);
    }

    /**
     * 用户登陆退出
     * @param view
     * @return
     */
    public static ILoginOutPersenter getLoginOutPersenterImpl(ILoginOutPersenter.ILoginOutView view){
        return new LoginOutPersenterImpl(view);
    }

    /**
     * 获取平台授权登陆令牌
     * @param view
     * @return
     */
    public static ILoginKeyGetPersenter getLoginKeyGetPersenterImpl(ILoginKeyGetPersenter.ILoginKeyGetView view){
        return new LoginKeyGetPersenterImpl(view);
    }

    /**
     * 获取用户相关信息
     * @param view
     * @return
     */
    public static IUserInfoGetPersenter getUserInfoGetPersenterImpl(IUserInfoGetPersenter.IUserInfoGetView view){
        return new UserInfoGetPersenterImpl(view);
    }

    /**
     * 密码修改桥梁
     * @param view
     * @return
     */
    public static IUserPwdChangePersenter getUserPwdChangeImpl(IUserPwdChangePersenter.IUserPwdChangeView view){
        return new UserPwdChangePersenterImpl(view);
    }

    /**
     * 获取用户推送状态
     * @param view
     * @return
     */
    public static IUserPushStatePersenter getUserPushStatePersenterImpl(IUserPushStatePersenter.IUserPushStateView view){
        return new UserPushStatePersenterImpl(view);
    }

    /**
     * 修改用户接受消息提醒状态
     * @param view
     * @return
     */
    public static IUserPushStateSettingPersenter getUserPushStateSettingPersenterImpl(IUserPushStateSettingPersenter.IUserPushStateSettingView view){
        return new UserPushStateSettingPersenterImpl(view);
    }
}
