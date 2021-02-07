package com.ynzhxf.nd.xyfirecontrolapp.model.common;

import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.NewsBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.common.INewsListPersenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 新闻列表模型数据
 * Created by nd on 2018-07-28.
 */

class NewsListModel extends BaseModel implements INewsListPersenter.INewsListModel {

    private INewsListPersenter persenter;

    public  NewsListModel(INewsListPersenter persenter){
        this.persenter = persenter;
    }
    @Override
    public void requestNewsList(int pageSize) {
        HttpUtils.getRetrofit().getNewsList(GloblePlantformDatas.getInstance().getLoginInfoBean().getToken(),pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<PagingBean<NewsBean>,String>>() {
                               @Override
                               public void onNext(ResultBean<PagingBean<NewsBean>,String> result) {
                                   persenter.callBackNewsList(result);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),INewsListPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
