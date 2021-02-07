package com.ynzhxf.nd.xyfirecontrolapp.bean.inspection;

import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.BaseNodeBean;


/**
 * author hbzhou
 * date 2019/1/7 20:54
 */
public class InspectionItemListBean extends BaseNodeBean {
    /**
     * ProjectId : 3a425cb5c4d14cd399c8b5d54d0e2d52
     * TaskId : null
     * AreaId : null
     * InspectorId : 909c7188f67542f2b1ce45666bcf1c22
     * InspectItemTypes : 87c16824db4945fdbc2c3945b7c1cdaa,c62f9a41df4a411ebb26b09aeded13a6
     * InspectorName : test132
     * InspectorPhone : null
     * Result : null
     * ResultShow :
     * State : 0
     * StateShow :
     * OverTimeShow :
     * Remark : 123123
     */

    private String ProjectId;
    private String TaskId;
    private String AreaId;
    private String InspectorId;
    private String InspectItemTypes;
    private String InspectorName;
    private Object InspectorPhone;
    private int Result;
    private String ResultShow;
    private int State;
    private String StateShow;
    private String OverTimeShow;
    private String Remark;

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String ProjectId) {
        this.ProjectId = ProjectId;
    }

    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String TaskId) {
        this.TaskId = TaskId;
    }

    public String getAreaId() {
        return AreaId;
    }

    public void setAreaId(String AreaId) {
        this.AreaId = AreaId;
    }

    public String getInspectorId() {
        return InspectorId;
    }

    public void setInspectorId(String InspectorId) {
        this.InspectorId = InspectorId;
    }

    public String getInspectItemTypes() {
        return InspectItemTypes;
    }

    public void setInspectItemTypes(String InspectItemTypes) {
        this.InspectItemTypes = InspectItemTypes;
    }

    public String getInspectorName() {
        return InspectorName;
    }

    public void setInspectorName(String InspectorName) {
        this.InspectorName = InspectorName;
    }

    public Object getInspectorPhone() {
        return InspectorPhone;
    }

    public void setInspectorPhone(Object InspectorPhone) {
        this.InspectorPhone = InspectorPhone;
    }

    public int getResult() {
        return Result;
    }

    public void setResult(int Result) {
        this.Result = Result;
    }

    public String getResultShow() {
        return ResultShow;
    }

    public void setResultShow(String ResultShow) {
        this.ResultShow = ResultShow;
    }

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }

    public String getStateShow() {
        return StateShow;
    }

    public void setStateShow(String StateShow) {
        this.StateShow = StateShow;
    }

    public String getOverTimeShow() {
        return OverTimeShow;
    }

    public void setOverTimeShow(String OverTimeShow) {
        this.OverTimeShow = OverTimeShow;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }
}
