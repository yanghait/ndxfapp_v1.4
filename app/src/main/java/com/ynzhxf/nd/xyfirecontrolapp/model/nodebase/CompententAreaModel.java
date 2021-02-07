package com.ynzhxf.nd.xyfirecontrolapp.model.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ICompententAreaPersenter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 主管部门区域列表数据请求获取
 * Created by nd on 2018-07-17.
 */

class CompententAreaModel extends BaseModel implements ICompententAreaPersenter.ICompententAreaModel {

    private ICompententAreaPersenter persenter;
    public  CompententAreaModel(ICompententAreaPersenter persenter){
        this.persenter = persenter;
    }


    @Override
    public void requestCompententArea() {
        HttpUtils.getRetrofit().getCompententAreaList(GloblePlantformDatas.getInstance().getLoginInfoBean().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<List<ProjectNodeBean>,String[]>>() {
                               @Override
                               public void onNext(ResultBean<List<ProjectNodeBean>,String[]> resultBean) {
                                   persenter.callBackCompententArea(resultBean);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),ICompententAreaPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
