package com.ynzhxf.nd.xyfirecontrolapp.presenter.common.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.common.CommonModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.common.INewsURLPersenter;

/**
 * Created by nd on 2018-07-28.
 */
class NewsURLPersenterImpl extends BasePersenter implements INewsURLPersenter{

    private INewsURLView view;
    private INewsURLModel model;

    public NewsURLPersenterImpl(INewsURLView view){
        this.view = view;
        this.model = CommonModelFactory.getNewsURLModel(this);
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
    public void doNewsURL(String newsID) {
        model.requestNewsURL(newsID);
    }

    @Override
    public void callBackNewsURL(ResultBean<String, String> result) {
        if(view !=null){
            view.callBackNewsURL(result);
        }
    }
}
