package com.ynzhxf.nd.xyfirecontrolapp.presenter.count;

import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.Map;

/**
 *获取标签近30天记录数量统计
 * Created by nd on 2018-07-24.
 */

public interface ILabelInfoCountPersenter extends IBasePersenter {


    String TAG = "LabelInfoCount";

    interface ILabelInfoCountView extends IBaseView {

        /**
         *数据请求完成回调
         */
        void callBackLabelInfoCount(Map<String, String> result);

    }

    interface ILabelInfoCountModel{

        /**
         * 数据加载请求
         * @param labelID 标签ID
         */
        void requestLabelInfoCountCount(String labelID);
    }

    /**
     * 发送数据请求
     * @param labelID 标签ID
     */
    void doLabelInfoCount(String labelID);

    /**
     * 完成请求返回数据
     */
    void callBackLabelInfoCount(Map<String, String> result);


}
