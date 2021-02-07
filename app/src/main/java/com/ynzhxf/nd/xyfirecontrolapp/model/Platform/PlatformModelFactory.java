package com.ynzhxf.nd.xyfirecontrolapp.model.Platform;

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
 * Created by nd on 2018-07-14.
 */

public class PlatformModelFactory {


    /**
     * 获取服务版本模型
     * @param persenter
     * @return
     */
    public  static IVersionCheckPersenter.IVersionCheckModel getVersionCheckModel(IVersionCheckPersenter persenter){
        return new VersionCheckModel(persenter);
    }

    /**
     * 获取登陆令牌
     * @param persenter
     * @return
     */
    public static ILoginKeyGetPersenter.ILoginKeyGetModel getLoginKeyGetModel(ILoginKeyGetPersenter persenter){
        return new LoginKeyGetModel(persenter);
    }

    /**
     * 获取登陆Model
     * @param persenter
     * @return
     */
    public static ILoginPersenter.ILoginModel getLoginModel(ILoginPersenter persenter){
        return new LoginModel(persenter);
    }

    /**
     * 获取登陆检测Model
     * @param persenter
     * @return
     */
    public static ICheckLoginPersenter.ICheckLoginModel getCheckedModel(ICheckLoginPersenter persenter){
        return new CheckLoginModel(persenter);
    }

    /**
     * 退出登陆
     * @param persenter
     * @return
     */
    public static ILoginOutPersenter.ILoginOutModel getLoginOutModel(ILoginOutPersenter persenter){
        return new LoginOutModel(persenter);
    }

    /**
     * 获取用户相关信息
     * @param persenter
     * @return
     */
    public static IUserInfoGetPersenter.IUserInfoGetModel getUserInfoGetModel(IUserInfoGetPersenter persenter){
        return new UserInfoGetModel(persenter);
    }

    /**
     * 用户秘密修改
     * @param persenter
     * @return
     */

    public static IUserPwdChangePersenter.IUserPwdChangeModel getUserPwdChangeModel(IUserPwdChangePersenter persenter){
        return new UserPwdChangeModel(persenter);
    }

    /**
     * 获取用户是否接受消息提醒状态
     * @param persenter
     * @return
     */
    public static IUserPushStatePersenter.IUserPushStateModel getUserPushStateModel(IUserPushStatePersenter persenter){
        return new UserPushStateModel(persenter);
    }

    /**
     * 设置用户消息提醒状态
     * @param persenter
     * @return
     */
    public static IUserPushStateSettingPersenter.IUserPushStateSettingModel getUserPushStateSettingModel(IUserPushStateSettingPersenter persenter){
        return new UserPushStateSettingModel(persenter);
    }
}
