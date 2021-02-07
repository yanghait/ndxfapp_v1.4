package com.ynzhxf.nd.xyfirecontrolapp.presenter.count.impl;

import com.ynzhxf.nd.xyfirecontrolapp.presenter.count.ILabelInfoCountPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.count.IProjectEventCountPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.count.IUserMaxEventCountPersenter;

/**统计桥梁工厂实例
 * Created by nd on 2018-07-16.
 */

public class CountPersenterFactory {


    /**
     * 获取用户最大权限项目的事件和实时报警统计
     * @param view
     * @return
     */
    public static IUserMaxEventCountPersenter getUserMaxEventCountPersenterImpl(IUserMaxEventCountPersenter.IUserMaxEventCountView view){
        return new UserMaxEventCountPersenterImpl(view);
    }

    /**
     * 获取项目事件统计和实时报警
     * @param view
     * @return
     */
    public static IProjectEventCountPersenter getProjectEventCountPersenterImpl(IProjectEventCountPersenter.IProjectEventCountView view){
        return new ProjectEventCountPersenterImpl(view);
    }

    /**
     * 获取标签记录数量信息统计
     * @param view
     * @return
     */
    public static ILabelInfoCountPersenter getLabelInfoCountPersenterImpl(ILabelInfoCountPersenter.ILabelInfoCountView view){
        return new LabelInfoCountPersenterImpl(view);
    }
}
