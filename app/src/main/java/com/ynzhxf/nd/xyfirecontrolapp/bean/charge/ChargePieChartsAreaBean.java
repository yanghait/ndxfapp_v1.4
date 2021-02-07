package com.ynzhxf.nd.xyfirecontrolapp.bean.charge;


/**
 * author hbzhou
 * date 2019/10/30 11:31
 */
public class ChargePieChartsAreaBean {
    /**
     * AreaId : 2
     * AreaName : 昆明市
     * StatisticsValue : 20
     * StatisticsCount : 0
     */

    private String AreaId;
    private String AreaName;
    private int StatisticsValue;
    private int StatisticsCount;

    public String getAreaId() {
        return AreaId;
    }

    public void setAreaId(String AreaId) {
        this.AreaId = AreaId;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String AreaName) {
        this.AreaName = AreaName;
    }

    public int getStatisticsValue() {
        return StatisticsValue;
    }

    public void setStatisticsValue(int StatisticsValue) {
        this.StatisticsValue = StatisticsValue;
    }

    public int getStatisticsCount() {
        return StatisticsCount;
    }

    public void setStatisticsCount(int StatisticsCount) {
        this.StatisticsCount = StatisticsCount;
    }
}
