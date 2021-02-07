package com.ynzhxf.nd.xyfirecontrolapp.presenter.common.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.NewsBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.common.CommonModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.common.INewsListPersenter;

/**
 * Created by nd on 2018-07-28.
 */

class NewsListPersenterImpl extends BasePersenter implements INewsListPersenter {
    private INewsListView view;
    private INewsListModel model;

    public NewsListPersenterImpl(INewsListView view){
        this.view = view;
        this.model = CommonModelFactory.getNewsListModel(this);
    }

    @Override
    public void callBackError(ResultBean<String, String> result, String action) {
        if(view !=null){
            callBackError(result , action);
        }
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void doNewsList(int pageSize) {
        model.requestNewsList(pageSize);
    }

    @Override
    public void callBackNewsList(ResultBean<PagingBean<NewsBean>, String> result) {
        if(view != null){
            view.callBackNewsList(result);
        }
    }
}
