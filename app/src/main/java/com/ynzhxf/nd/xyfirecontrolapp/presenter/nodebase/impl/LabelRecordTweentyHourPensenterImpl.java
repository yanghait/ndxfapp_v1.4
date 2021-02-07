package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.RealDataLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.nodebase.NodeBaseModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ILabelRecordTweentyHourPersenter;

import java.util.List;

/**
 * 标签历史记录数据获取
 * Created by nd on 2018-07-24.
 */

class LabelRecordTweentyHourPensenterImpl extends BasePersenter implements ILabelRecordTweentyHourPersenter {

    private ILabelRecordTweentyHourView view;
    private ILabelRecordTweentyHourModel model;

    public LabelRecordTweentyHourPensenterImpl(ILabelRecordTweentyHourView view) {
        this.view = view;
        this.model = NodeBaseModelFactory.getLabelRecordTweentyHourModel(this);
    }


    @Override
    public void callBackError(ResultBean<String, String> result, String action) {
        if (view != null) {
            view.callBackError(result, action);
        }
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void doLabelRecordTweentyHour(String labelID, String startTime, String endTime) {
        model.requestLabelWriteValue(labelID, startTime, endTime);
    }

    @Override
    public void callBackLabelRecordTweentyHour(ResultBean<List<RealDataLogBean>, String> result) {
        if (view != null) {
            view.callBackLabelRecordTweentyHour(result);
        }
    }
}
