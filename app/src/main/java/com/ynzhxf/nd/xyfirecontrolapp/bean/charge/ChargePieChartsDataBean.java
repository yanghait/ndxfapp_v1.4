package com.ynzhxf.nd.xyfirecontrolapp.bean.charge;


import java.util.List;

/**
 * author hbzhou
 * date 2019/10/30 11:33
 */
public class ChargePieChartsDataBean {

    private int AreaRealAlarmSum;
    private List<ChargePieChartsAreaBean> LsAreaRealAlarmCount;
    private int BusinessTypeRealAlarmSum;
    private List<ChargePieChartsTradeBean> LsBusinessTypeRealAlarmCount;

    public int getAreaRealAlarmSum() {
        return AreaRealAlarmSum;
    }

    public void setAreaRealAlarmSum(int areaRealAlarmSum) {
        AreaRealAlarmSum = areaRealAlarmSum;
    }

    public List<ChargePieChartsAreaBean> getLsAreaRealAlarmCount() {
        return LsAreaRealAlarmCount;
    }

    public void setLsAreaRealAlarmCount(List<ChargePieChartsAreaBean> lsAreaRealAlarmCount) {
        LsAreaRealAlarmCount = lsAreaRealAlarmCount;
    }

    public int getBusinessTypeRealAlarmSum() {
        return BusinessTypeRealAlarmSum;
    }

    public void setBusinessTypeRealAlarmSum(int businessTypeRealAlarmSum) {
        BusinessTypeRealAlarmSum = businessTypeRealAlarmSum;
    }

    public List<ChargePieChartsTradeBean> getLsBusinessTypeRealAlarmCount() {
        return LsBusinessTypeRealAlarmCount;
    }

    public void setLsBusinessTypeRealAlarmCount(List<ChargePieChartsTradeBean> lsBusinessTypeRealAlarmCount) {
        LsBusinessTypeRealAlarmCount = lsBusinessTypeRealAlarmCount;
    }
}
