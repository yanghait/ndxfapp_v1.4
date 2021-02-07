package com.ynzhxf.nd.xyfirecontrolapp.bean.common;


import java.util.List;

/**
 * author hbzhou
 * date 2019/5/29 17:08
 */
public class ChargeIndexBarChartBean {

    private List<String> LsAreaProject;
    private List<String> LsAlarmCount;
    private List<String> LsHighRiskCount;
    private List<String> LsLowEquipDetectCount;

    public List<String> getLsAreaProject() {
        return LsAreaProject;
    }

    public void setLsAreaProject(List<String> LsAreaProject) {
        this.LsAreaProject = LsAreaProject;
    }

    public List<String> getLsAlarmCount() {
        return LsAlarmCount;
    }

    public void setLsAlarmCount(List<String> LsAlarmCount) {
        this.LsAlarmCount = LsAlarmCount;
    }

    public List<String> getLsHighRiskCount() {
        return LsHighRiskCount;
    }

    public void setLsHighRiskCount(List<String> LsHighRiskCount) {
        this.LsHighRiskCount = LsHighRiskCount;
    }

    public List<String> getLsLowEquipDetectCount() {
        return LsLowEquipDetectCount;
    }

    public void setLsLowEquipDetectCount(List<String> LsLowEquipDetectCount) {
        this.LsLowEquipDetectCount = LsLowEquipDetectCount;
    }
}
