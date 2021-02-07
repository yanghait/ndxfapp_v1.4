package com.ynzhxf.nd.xyfirecontrolapp.bean.charge;


import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * author hbzhou
 * date 2019/10/29 10:20
 */
public class ChargeChartsListBean implements MultiItemEntity {
    private int modeltype;

    private ChargeChartsIndexEventData beanModel;

    private ChargeChartsHistoryAlarmListBean historyAlarmListBean;

    public ChargeChartsHistoryAlarmListBean getHistoryAlarmListBean() {
        return historyAlarmListBean;
    }

    public void setHistoryAlarmListBean(ChargeChartsHistoryAlarmListBean historyAlarmListBean) {
        this.historyAlarmListBean = historyAlarmListBean;
    }

    public ChargeChartsIndexEventData getBeanModel() {
        return beanModel;
    }

    public void setBeanModel(ChargeChartsIndexEventData beanModel) {
        this.beanModel = beanModel;
    }

    public int getModeltype() {
        return modeltype;
    }

    public void setModeltype(int modeltype) {
        this.modeltype = modeltype;
    }

    @Override
    public int getItemType() {
        return modeltype;
    }
}
