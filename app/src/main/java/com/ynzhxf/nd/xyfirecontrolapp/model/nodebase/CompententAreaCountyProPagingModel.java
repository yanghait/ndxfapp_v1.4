package com.ynzhxf.nd.xyfirecontrolapp.model.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ICompententAreaCountyProPagingPersenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 使用县区级ID，分页获取该节点下的项目列表
 * Created by nd on 2018-07-20.
 */

 class CompententAreaCountyProPagingModel extends BaseModel implements ICompententAreaCountyProPagingPersenter.ICompententAreaCountyProPagingModel{

    private ICompententAreaCountyProPagingPersenter persenter;

    public CompententAreaCountyProPagingModel(ICompententAreaCountyProPagingPersenter persenter){
        this.persenter = persenter;
    }

    @Override
    public void requestCompententAreaCountyProPaging(int pageSize, String areaID) {
        HttpUtils.getRetrofit().getAreaCountyProjectList(GloblePlantformDatas.getInstance().getLoginInfoBean().getToken(),pageSize,areaID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<PagingBean<ProjectNodeBean>,String>>() {
                               @Override
                               public void onNext(ResultBean<PagingBean<ProjectNodeBean>,String>  resultBean) {
                                   persenter.callBackCompententAreaCountyProPaging(resultBean);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),ICompententAreaCountyProPagingPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
