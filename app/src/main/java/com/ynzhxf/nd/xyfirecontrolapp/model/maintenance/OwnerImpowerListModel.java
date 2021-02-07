package com.ynzhxf.nd.xyfirecontrolapp.model.maintenance;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.OwnerImpowerInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.OwnerImpowerListBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IMainOwnerImpowerListPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class OwnerImpowerListModel extends BaseModel implements IMainOwnerImpowerListPresenter.IMainOwnerImpowerListModel {
    private IMainOwnerImpowerListPresenter presenter;

    public OwnerImpowerListModel(IMainOwnerImpowerListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void requestgetMainOwnerImpowerList(OwnerImpowerInputBean bean) {
        HttpUtils.getRetrofit().getOwnerImpowerList(HelperTool.getToken(), bean.getProjectID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<List<OwnerImpowerListBean>, String>>() {
                    @Override
                    public void onNext(ResultBean<List<OwnerImpowerListBean>, String> resultBean) {
                        presenter.callBackMainOwnerImpowerList(resultBean);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        presenter.callBackError(createResult(throwable), IMainOwnerImpowerListPresenter.TAG);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
