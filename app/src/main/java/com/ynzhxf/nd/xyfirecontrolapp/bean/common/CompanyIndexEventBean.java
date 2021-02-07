package com.ynzhxf.nd.xyfirecontrolapp.bean.common;


/**
 * author hbzhou
 * date 2019/6/28 13:05
 */
public class CompanyIndexEventBean {

    /**
     * Recent24HourCount : 0
     * WorkOrderSum : 210
     * InspectionTaskSum : 52
     * ProjectOnlineSum : 21
     */

    private String Recent24HourCount;
    private String WorkOrderSum;
    private String InspectionTaskSum;
    private String ProjectOnlineSum;

    public String getRecent24HourCount() {
        return Recent24HourCount;
    }

    public void setRecent24HourCount(String Recent24HourCount) {
        this.Recent24HourCount = Recent24HourCount;
    }

    public String getWorkOrderSum() {
        return WorkOrderSum;
    }

    public void setWorkOrderSum(String WorkOrderSum) {
        this.WorkOrderSum = WorkOrderSum;
    }

    public String getInspectionTaskSum() {
        return InspectionTaskSum;
    }

    public void setInspectionTaskSum(String InspectionTaskSum) {
        this.InspectionTaskSum = InspectionTaskSum;
    }

    public String getProjectOnlineSum() {
        return ProjectOnlineSum;
    }

    public void setProjectOnlineSum(String ProjectOnlineSum) {
        this.ProjectOnlineSum = ProjectOnlineSum;
    }
}
