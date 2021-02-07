package com.ynzhxf.nd.xyfirecontrolapp.model.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectSystemBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectSystemListPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 系统列表获取
 * Created by nd on 2018-07-23.
 */

class ProjectSystemListModel extends BaseModel implements IProjectSystemListPersenter.IIProjectSystemListModel {

    private IProjectSystemListPersenter persenter;
    public ProjectSystemListModel(IProjectSystemListPersenter persenter){
        this.persenter = persenter;
    }

    @Override
    public void requestProjectSystemList(String proID) {
        HttpUtils.getRetrofit().getProjectSystemListByUserNameAndProID(HelperTool.getToken() ,HelperTool.getUsername() ,proID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<List<ProjectSystemBean>,String>>() {
                               @Override
                               public void onNext(ResultBean<List<ProjectSystemBean>,String> resultBean) {
                                   persenter.callBackProjectSettingRepaireOrToken(resultBean);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),IProjectSystemListPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
