package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.nodebase.NodeBaseModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ICompententAreaCountyProPagingPersenter;

/**
 * 主管部门县区级项目数据分页获取
 * Created by nd on 2018-07-20.
 */

class CompententAreaCountyProPagingPersenterImpl extends BasePersenter implements ICompententAreaCountyProPagingPersenter{

    private ICompententAreaCountyProPagingView view;

    private ICompententAreaCountyProPagingModel model;

    public CompententAreaCountyProPagingPersenterImpl(ICompententAreaCountyProPagingView view){
        this.view = view;
        this.model = NodeBaseModelFactory.getCompententAreaCountyProPagingModelImpl(this);
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
    public void doCompententAreaCountyProPaging(int pageSize, String areaID) {
        model.requestCompententAreaCountyProPaging(pageSize, areaID);
    }

    @Override
    public void callBackCompententAreaCountyProPaging(ResultBean<PagingBean<ProjectNodeBean>, String> result) {
        if(view != null){
            view.callBackCompententAreaCountyProPaging(result);
        }
    }
}
