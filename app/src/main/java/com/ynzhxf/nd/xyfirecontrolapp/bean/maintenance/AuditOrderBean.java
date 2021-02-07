package com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance;

import com.ynzhxf.nd.xyfirecontrolapp.bean.BaseDataBean;

public class AuditOrderBean extends BaseDataBean {

    /**
     * FaultType : 故障类型1
     * FaultPlace : -454545641564165
     * RepairMethod : 修复方法1
     * RepairContent : 31223415646541651
     */

    private String FaultType;
    private String FaultPlace;
    private String RepairMethod;
    private String RepairContent;

    public String getFaultType() {
        return FaultType;
    }

    public void setFaultType(String FaultType) {
        this.FaultType = FaultType;
    }

    public String getFaultPlace() {
        return FaultPlace;
    }

    public void setFaultPlace(String FaultPlace) {
        this.FaultPlace = FaultPlace;
    }

    public String getRepairMethod() {
        return RepairMethod;
    }

    public void setRepairMethod(String RepairMethod) {
        this.RepairMethod = RepairMethod;
    }

    public String getRepairContent() {
        return RepairContent;
    }

    public void setRepairContent(String RepairContent) {
        this.RepairContent = RepairContent;
    }
}
