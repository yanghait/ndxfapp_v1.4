package com.ynzhxf.nd.xyfirecontrolapp.model.count;

import com.ynzhxf.nd.xyfirecontrolapp.presenter.count.ILabelInfoCountPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.count.IProjectEventCountPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.count.IUserMaxEventCountPersenter;

/**
 * 数据统计节点获取生成工厂
 * Created by nd on 2018-07-16.
 */

public class CountModelFactory {

    /**
     * 获取用户事件和实时报警统计
     * @param persenter
     * @return
     */
    public static IUserMaxEventCountPersenter.IUserMaxEventCountModel getUserMaxEventCountModel(IUserMaxEventCountPersenter persenter){
        return new UserMaxEventCountModel(persenter);
    }

    /**
     * 获取项目实时报警和事件统计统计
     * @param persenter
     * @return
     */
    public static IProjectEventCountPersenter.IProjectEventCountModel getProjectEventCountModel(IProjectEventCountPersenter persenter){
        return new ProjectEventCountModel(persenter);
    }


    /**
     * 获取标签记录数据统计
     * @param persenter
     * @return
     */
    public static ILabelInfoCountPersenter.ILabelInfoCountModel getLabelInfoCountModel(ILabelInfoCountPersenter persenter){
        return new LabelInfoCountModel(persenter);
    }

}
