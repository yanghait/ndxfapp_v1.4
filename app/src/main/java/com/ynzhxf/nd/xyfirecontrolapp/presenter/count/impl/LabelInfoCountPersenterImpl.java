package com.ynzhxf.nd.xyfirecontrolapp.presenter.count.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.count.CountModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.count.ILabelInfoCountPersenter;

import java.util.Map;

/**
 * 获取标签信息数量统计
 * Created by nd on 2018-07-24.
 */

class LabelInfoCountPersenterImpl extends BasePersenter implements ILabelInfoCountPersenter{

    private ILabelInfoCountView view;

    private ILabelInfoCountModel model;

    public LabelInfoCountPersenterImpl(ILabelInfoCountView view){
        this.view = view;
        this.model = CountModelFactory.getLabelInfoCountModel(this);
    }

    @Override
    public void callBackError(ResultBean<String, String> result, String action) {
        if(view != null){
            view.callBackError(result , action);
        }
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void doLabelInfoCount(String labelID) {
        model.requestLabelInfoCountCount(labelID);
    }

    @Override
    public void callBackLabelInfoCount(Map<String, String> result) {
        if(view != null){
            view.callBackLabelInfoCount(result);
        }
    }
}
