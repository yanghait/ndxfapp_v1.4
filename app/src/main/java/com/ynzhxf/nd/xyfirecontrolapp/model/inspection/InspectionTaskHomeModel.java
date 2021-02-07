package com.ynzhxf.nd.xyfirecontrolapp.model.inspection;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionTaskHomeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionTaskHomeInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.inspection.ITaskInspectionHomePresenter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class InspectionTaskHomeModel extends BaseModel implements ITaskInspectionHomePresenter.ITaskInspectionHomeListModel {

    private ITaskInspectionHomePresenter presenter;

    public InspectionTaskHomeModel(ITaskInspectionHomePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void requestTaskInspectionHomeList(InspectionTaskHomeInputBean bean) {

        Observable<ResultBean<List<InspectionTaskHomeBean>, String>> observable;
        if (bean.isCompany()) {
            if (bean.isCompanyForOwner()) {
                observable = HttpUtils.getRetrofit().getCompanyInspectForOwnerTaskHomeList(bean.getToken(), bean.getProjectId(), bean.getState(), bean.getPageIndex(), bean.getPageSize(),
                        bean.getStartTime(), bean.getEndTime(), bean.getInspectTypeId());
            } else {
                observable = HttpUtils.getRetrofit().getCompanyInspectionTaskHomeList(bean.getToken(), bean.getProjectId(), bean.getState(), bean.getPageIndex(), bean.getPageSize(),
                        bean.getStartTime(), bean.getEndTime(), bean.getInspectTypeId());
            }
        } else {
            observable = HttpUtils.getRetrofit().getInspectionTaskHomeList(bean.getToken(), bean.getProjectId(), bean.getState(), bean.getPageIndex(), bean.getPageSize(),
                    bean.getStartTime(), bean.getEndTime(), bean.getInspectTypeId());
        }
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<List<InspectionTaskHomeBean>, String>>() {
                    @Override
                    public void onNext(ResultBean<List<InspectionTaskHomeBean>, String> listStringResultBean) {
                        presenter.callBackTaskInspectionHomeList(listStringResultBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        presenter.callBackError(createResult(e), ITaskInspectionHomePresenter.TAG);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
