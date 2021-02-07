package com.ynzhxf.nd.xyfirecontrolapp.presenter.platform;

import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.Map;


/**
 * 获取服务器APP的版本号和下载地址
 * Created by nd on 2018-07-13.
 */

public interface IVersionCheckPersenter extends IBasePersenter{
    String TAG = "VersionCheck";
     interface IVersionCheckView extends IBaseView{
        /**
         *版本检测完成回调
         */
        void callBackVersionCheck(Map<String , String> result);
    }

     interface IVersionCheckModel{
        /**
         * 版本检测请求
         */
        void requestVersionCheck();
    }

    /**
     * 版本检测请求
     */
    void doVersionCheck();

    /**
     *检测完成
     */
    void callBackVersionCheck(Map<String , String> result);




}
