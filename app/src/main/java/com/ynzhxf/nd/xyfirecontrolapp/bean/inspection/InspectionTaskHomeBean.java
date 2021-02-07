package com.ynzhxf.nd.xyfirecontrolapp.bean.inspection;

import com.ynzhxf.nd.xyfirecontrolapp.bean.BaseDataBean;

public class InspectionTaskHomeBean extends BaseDataBean {

    /**
     * ID : 3eb8993493ba477e961e159d3a7c45a5
     * Name : 每日巡查 9:00~11:00
     * ProjectId : 3a425cb5c4d14cd399c8b5d54d0e2d52
     * InspectTypeId : aa697273780a4e558330d509ee2862ac
     * ChargeManID : 747b9f297d20488aba74785528a056d5
     * ChargeManName : 测试账号2
     * ChargeManPhone : 13669735360
     * ChargeManShow : 测试账号2(13669735360)
     * StartTime : /Date(1546650000000)/
     * StartTimeShow : 2019年01月05日 09:00
     * EndTimeShow : 2019年01月05日 11:00
     * EndTime : /Date(1546657200000)/
     * State : 0
     * StateShow : 待巡检
     * Result : null
     * ResultShow : -
     * CurrentUserType : 3
     */

    private String Name;
    private String ProjectId;
    private String ProjectName;
    private String InspectTypeId;
    private String ChargeManID;
    private String ChargeManName;
    private String ChargeManPhone;
    private String ChargeManShow;
    private String StartTime;
    private String StartTimeShow;
    private String EndTimeShow;
    private String EndTime;
    private int State;
    private String StateShow;
    private int Result;
    private String ResultShow;
    private int CurrentUserType;
    private String CompanyName;
    private String StateCount;

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getStateCount() {
        return StateCount;
    }

    public void setStateCount(String stateCount) {
        StateCount = stateCount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String ProjectId) {
        this.ProjectId = ProjectId;
    }

    public String getInspectTypeId() {
        return InspectTypeId;
    }

    public void setInspectTypeId(String InspectTypeId) {
        this.InspectTypeId = InspectTypeId;
    }

    public String getChargeManID() {
        return ChargeManID;
    }

    public void setChargeManID(String ChargeManID) {
        this.ChargeManID = ChargeManID;
    }

    public String getChargeManName() {
        return ChargeManName;
    }

    public void setChargeManName(String ChargeManName) {
        this.ChargeManName = ChargeManName;
    }

    public String getChargeManPhone() {
        return ChargeManPhone;
    }

    public void setChargeManPhone(String ChargeManPhone) {
        this.ChargeManPhone = ChargeManPhone;
    }

    public String getChargeManShow() {
        return ChargeManShow;
    }

    public void setChargeManShow(String ChargeManShow) {
        this.ChargeManShow = ChargeManShow;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public String getStartTimeShow() {
        return StartTimeShow;
    }

    public void setStartTimeShow(String StartTimeShow) {
        this.StartTimeShow = StartTimeShow;
    }

    public String getEndTimeShow() {
        return EndTimeShow;
    }

    public void setEndTimeShow(String EndTimeShow) {
        this.EndTimeShow = EndTimeShow;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
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

    public int getCurrentUserType() {
        return CurrentUserType;
    }

    public void setCurrentUserType(int CurrentUserType) {
        this.CurrentUserType = CurrentUserType;
    }
}
