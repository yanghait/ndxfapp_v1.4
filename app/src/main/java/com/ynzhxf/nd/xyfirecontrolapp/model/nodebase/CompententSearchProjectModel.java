package com.ynzhxf.nd.xyfirecontrolapp.model.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ICompententSearchProjectPersenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 主管部门区域ID和关键字获取项目列表分页
 * Created by nd on 2018-07-18.
 */

public class CompententSearchProjectModel extends BaseModel implements ICompententSearchProjectPersenter.ICompententSearchProjectModel {

    private ICompententSearchProjectPersenter persenter;

    public CompententSearchProjectModel(ICompententSearchProjectPersenter persenter){
        this.persenter = persenter;
    }
    @Override
    public void requestCompententSearchProject(int PageSize, String Token, String AreaID, String keyWord) {
        HttpUtils.getRetrofit().getAreaAndKeywordsProjectList(PageSize,GloblePlantformDatas.getInstance().getLoginInfoBean().getToken(),AreaID,keyWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<PagingBean<ProjectNodeBean>,String>>() {
                               @Override
                               public void onNext(ResultBean<PagingBean<ProjectNodeBean>,String> resultBean) {
                                   persenter.callBackCompententSearchProject(resultBean);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),ICompententSearchProjectPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
