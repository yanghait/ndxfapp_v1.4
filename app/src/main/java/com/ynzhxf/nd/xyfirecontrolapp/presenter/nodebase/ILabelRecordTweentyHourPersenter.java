package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.RealDataLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.List;

/**
 * 获取标签24小时内的数据记录
 * Created by nd on 2018-07-19.
 */

public interface ILabelRecordTweentyHourPersenter extends IBasePersenter {
    String TAG = "LabelRecordTweentyHour";

    /**
     * 视图接口
     */
    interface ILabelRecordTweentyHourView extends IBaseView {
        /**
         * 数据请求完成回调
         */
        void callBackLabelRecordTweentyHour(ResultBean<List<RealDataLogBean>, String> result);

    }

    /**
     * 模型接口
     */
    interface ILabelRecordTweentyHourModel {

        /**
         * 数据请求
         *
         * @param labelID 标签ID
         */
        void requestLabelWriteValue(String labelID, String startTime, String endTime);
    }

    /**
     * 发送数据请求
     *
     * @param labelID 标签ID
     */
    void doLabelRecordTweentyHour(String labelID, String startTime, String endTime);


    /**
     * 完成请求返回数据
     */
    void callBackLabelRecordTweentyHour(ResultBean<List<RealDataLogBean>, String> result);
}
