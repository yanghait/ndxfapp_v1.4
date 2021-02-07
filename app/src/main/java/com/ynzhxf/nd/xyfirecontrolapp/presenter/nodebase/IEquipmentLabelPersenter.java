package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.TreeGridBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.LabelNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.List;

/**
 * 系统下的设备和标签列表获取(一般系统)
 * Created by nd on 2018-07-23.
 */

public interface IEquipmentLabelPersenter extends IBasePersenter {

    String TAG = "EquipmentLabel";

    /**
     * 视图接口
     */
    interface IEquipmentLabelView extends IBaseView {
        /**
         *数据请求完成回调
         */
        void callBackEquipmentLabel(ResultBean<List<TreeGridBean<LabelNodeBean>>,String> result);

    }

    /**
     * 模型接口
     */
    interface IEquipmentLabelModel{

        /**
         * 数据请求
         * @param proSysID 系统ID
         */
        void requestEquipmentLabel(String proSysID);
    }

    /**
     * 发送数据请求
     * @param proSysID 系统ID
     */
    void doRequestEquipmentLabel(String proSysID );


    /**
     * 完成请求返回数据
     */
    void callBackProjectSettingRepaireOrToken(ResultBean<List<TreeGridBean<LabelNodeBean>>,String> result);
}
