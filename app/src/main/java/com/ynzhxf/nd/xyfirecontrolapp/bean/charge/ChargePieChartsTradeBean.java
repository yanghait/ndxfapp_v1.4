package com.ynzhxf.nd.xyfirecontrolapp.bean.charge;


/**
 * author hbzhou
 * date 2019/10/30 09:50
 */
public class ChargePieChartsTradeBean {
    private String BusinessTypeName;
    private int StatisticsCount;
    private int StatisticsValue;
    private String BusinessTypeId;
    private int color;
    private String label;

    public int getStatisticsCount() {
        return StatisticsCount;
    }

    public void setStatisticsCount(int statisticsCount) {
        StatisticsCount = statisticsCount;
    }

    public String getBusinessTypeName() {
        return BusinessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        BusinessTypeName = businessTypeName;
    }

    public int getStatisticsValue() {
        return StatisticsValue;
    }

    public void setStatisticsValue(int statisticsValue) {
        StatisticsValue = statisticsValue;
    }

    public String getBusinessTypeId() {
        return BusinessTypeId;
    }

    public void setBusinessTypeId(String businessTypeId) {
        BusinessTypeId = businessTypeId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
