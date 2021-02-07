package com.ynzhxf.nd.xyfirecontrolapp.model.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IUserHasAuthoryProjectPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 获取用户有权限的所有项目列表
 * Created by nd on 2018-08-01.
 */
class UserHasAuthoryProjectModel extends BaseModel implements IUserHasAuthoryProjectPersenter.IUserHasAuthoryProjectModel{
    private IUserHasAuthoryProjectPersenter persenter;
    public UserHasAuthoryProjectModel(IUserHasAuthoryProjectPersenter persenter){
        this.persenter = persenter;
    }


    @Override
    public void requestUserHasAuthoryProject() {
        HttpUtils.getRetrofit().getUserHasAuthoryProject(HelperTool.getToken() ,HelperTool.getUsername())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<List<ProjectNodeBean>,String>>() {
                               @Override
                               public void onNext(ResultBean<List<ProjectNodeBean>,String>  resultBean) {
                                   persenter.callBackUserHasAuthoryProject(resultBean);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),IUserHasAuthoryProjectPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
