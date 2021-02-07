package com.ynzhxf.nd.xyfirecontrolapp.model.maintenance;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.CompanyOrderListInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MaintenListAllBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IMainCompanyOrderListPresenter;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CompanyOrderListModel extends BaseModel implements IMainCompanyOrderListPresenter.ICompanyOrderListModel {
    private IMainCompanyOrderListPresenter presenter;

    public CompanyOrderListModel(IMainCompanyOrderListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void requestCompanyOrderList(CompanyOrderListInputBean bean) {
        Observable<ResultBean<MaintenListAllBean, String>> observable;
        if (bean.isCharge()) {
            observable = HttpUtils.getRetrofit().getChargeOrderList(bean.getToken(), bean.getProjectId(), bean.getState(), bean.getPageIndex(),
                    bean.getPageSize(), bean.getSystemId(), bean.getStartTime(), bean.getEndTime());
        } else {
            observable = HttpUtils.getRetrofit().getCompanyOrderList(bean.getToken(), bean.getProjectId(), bean.getState(), bean.getPageIndex(),
                    bean.getPageSize(), bean.getSystemId(), bean.getStartTime(), bean.getEndTime(), bean.getIsWorking());
        }
        //com.blankj.utilcode.util.LogUtils.eTag("输出获取维保公司工单列表输入参数00567---",bean.toString()+"123456");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<MaintenListAllBean, String>>() {
                    @Override
                    public void onNext(ResultBean<MaintenListAllBean, String> resultBean) {
                        presenter.callBackCompanyOrderList(resultBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //com.blankj.utilcode.util.LogUtils.eTag("输出获取维保公司工单列表0056789---",e.getMessage()+"~~~"+e.getCause()+"~~~"+e.getLocalizedMessage());
                        presenter.callBackError(createResult(e), IMainCompanyOrderListPresenter.TAG);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
