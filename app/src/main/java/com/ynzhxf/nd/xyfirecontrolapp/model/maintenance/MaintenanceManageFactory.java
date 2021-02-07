package com.ynzhxf.nd.xyfirecontrolapp.model.maintenance;

import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.ICompanyBackFillParamsPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IMainCompanyOrderListPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IMainOwnerImpowerListPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IMaintenanceListPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IOwnerOrderDetailsPresenter;

public class MaintenanceManageFactory {

    //获取业主维保列表
    public static IMaintenanceListPresenter.IMaintenanceListModel getMainOwnerWorkList(IMaintenanceListPresenter presenter) {
        return new MaintenanceManageListModel(presenter);
    }

    //获取业主维保授权列表
    public static IMainOwnerImpowerListPresenter.IMainOwnerImpowerListModel getMainOwnerImpowerList(IMainOwnerImpowerListPresenter presenter) {
        return new OwnerImpowerListModel(presenter);
    }

    // 获取业主维保工单详情
    public static IOwnerOrderDetailsPresenter.IOwnerOrderDetailsModel getMainOwnerOrderDetails(IOwnerOrderDetailsPresenter presenter) {
        return new OwnerOrderDetailsModel(presenter);
    }

    // 获取维保公司工单列表
    public static IMainCompanyOrderListPresenter.ICompanyOrderListModel getMainCompanyOrderList(IMainCompanyOrderListPresenter presenter) {
        return new CompanyOrderListModel(presenter);
    }

    // 维保公司获取回填系统参数
    public static ICompanyBackFillParamsPresenter.ICompanyBackFillParamsModel getCompamyBackFillParams(ICompanyBackFillParamsPresenter presenter) {
        return new CompanyBackFillParamsModel(presenter);
    }
}
