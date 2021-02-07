package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.nodebase.NodeBaseModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ICompententSearchProjectPersenter;

/**
 * 主管部门区域ID和关键字获取项目列表分页
 * Created by nd on 2018-07-17.
 */

class CompententSearchProjectPersenterImpl extends BasePersenter implements ICompententSearchProjectPersenter {

    private ICompententSearchProjectModel model;

    private ICompententSearchProjectView view ;

    public CompententSearchProjectPersenterImpl(ICompententSearchProjectView view){
        this.view = view;
        this.model = NodeBaseModelFactory.getCompententSearchProjectModel(this);
    }


    @Override
    public void callBackError(ResultBean<String, String> result, String action) {
        if(view != null){
            view.callBackError(result,action);
        }
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void doCompententArea(int PageSize, String Token, String AreaID, String keyWord) {
        model.requestCompententSearchProject(PageSize,Token,AreaID,keyWord);
    }

    @Override
    public void callBackCompententSearchProject(ResultBean<PagingBean<ProjectNodeBean>, String> result) {
        if (view != null){
            view.callBackCompententSearchProject(result);
        }

    }
}
