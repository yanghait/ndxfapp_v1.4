package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.nodebase.NodeBaseModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ILabelWriteValuePersenter;

/**
 *
 *对下位标签进行写值操作
 * Created by nd on 2018-07-24.
 */
class LabelWriteValuePersenterImpl extends BasePersenter implements ILabelWriteValuePersenter{

    private ILabelWriteValueView view;

    private ILabelWriteValueModel model;

    public LabelWriteValuePersenterImpl(ILabelWriteValueView view){
        this.view = view;
        this.model = NodeBaseModelFactory.getLabelWriteValueModel(this);
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
    public void doLabelWriteValue(String labelID, String confirmPwd) {
        model.requestLabelWriteValue(labelID , confirmPwd);
    }

    @Override
    public void callBackLabelWriteValue(ResultBean<String, String> result) {
        if(view != null){
            view.callBackLabelWriteValue(result);
        }
    }
}
