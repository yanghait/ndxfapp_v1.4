package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

/**
 *
 * 项目维保状态和消防接管状态设置
 * Created by nd on 2018-07-19.
 */

public interface IProjectSettingRepaireOrTokenPersenter extends IBasePersenter{

    String TAG = "ProjectSettingRepaireOrToken";

    /**
     * 视图接口
     */
    interface IProjectSettingRepaireOrTokenView extends IBaseView {
        /**
         *数据请求完成回调
         */
        void callBackProjectSettingRepaireOrToken(ResultBean<ProjectNodeBean, String> result);

    }

    /**
     * 模型接口
     */
    interface IProjectSettingRepaireOrTokenModel{

        /**
         * 数据请求
         * @param proID 项目ID
         * @param confirmPwd 确认密码
         * @param type 操作类型
         */
        void requestProjectSettingRepaireOrToken(String proID , String confirmPwd , String type);
    }

    /**
     * 发送数据请求
     * @param proID 项目ID
     * @param confirmPwd 确认密码
     * @param type 操作类型
     */
    void doProjectSettingRepaireOrToken(String proID , String confirmPwd , String type);


    /**
     * 完成请求返回数据
     */
    void callBackProjectSettingRepaireOrToken(ResultBean<ProjectNodeBean, String> result);
}
