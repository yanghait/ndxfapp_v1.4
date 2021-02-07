package com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance;

import com.ynzhxf.nd.xyfirecontrolapp.bean.BaseDataBean;

public class MaintenListBackBean extends BaseDataBean {

    /**
     * ProjectId : 94b6970a772448cea351e438d4e9487e
     * SystemId : b60e85b5185247cc8438157e65cfd069
     * Code : 20181115140250302930
     * StartUserName : 赵永志
     * StartUserTel : 222
     * ProjectSystemAuthorized_Id : c89efef0f71d447d8c84cfdf76616668
     * FaultPlace : 暂无数据
     * RepairContent : 暂无数据
     * OtherContent : 暂无数据
     * State : 10
     * IsShowPosition : false
     * EndTypeShow :
     * EndTimeShow : -
     * EvaluateLevel : 0
     * ProjectNameShow : 云南捷诺科技
     * ProjectSystemNameShow : 消防给水系统
     * StartTimeShow : 2018-11-15 14:02
     * MaintenancePersonConfirmTimeShow : -
     * AcceptUserInfoShow : 暂无数据
     * AcceptUserInfoTel : -
     * StateShow : 待确认
     * IsReminderShow : 未催办
     * StateIconUrl : /Uploads/Images/Icon/o_1cn5qo7cn10di1n8210vl9d71s6m23.png
     */
    private String ProjectId;
    private String SystemId;
    private String Code;
    private String StartUserName;
    private String StartUserTel;
    private String ProjectSystemAuthorized_Id;
    private String FaultPlace;
    private String RepairContent;
    private String OtherContent;
    private int State;
    private boolean IsShowPosition;
    private String EndTypeShow;
    private String EndTimeShow;
    private int EvaluateLevel;
    private String ProjectNameShow;
    private String ProjectSystemNameShow;
    private String StartTimeShow;
    private String MaintenancePersonConfirmTimeShow;
    private String AcceptUserInfoShow;
    private String AcceptUserInfoTel;
    private String StateShow;
    private String IsReminderShow;
    private boolean IsReminder;

    public boolean isReminder() {
        return IsReminder;
    }

    public void setReminder(boolean reminder) {
        IsReminder = reminder;
    }

    private String StateIconUrl;

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String ProjectId) {
        this.ProjectId = ProjectId;
    }

    public String getSystemId() {
        return SystemId;
    }

    public void setSystemId(String SystemId) {
        this.SystemId = SystemId;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getStartUserName() {
        return StartUserName;
    }

    public void setStartUserName(String StartUserName) {
        this.StartUserName = StartUserName;
    }

    public String getStartUserTel() {
        return StartUserTel;
    }

    public void setStartUserTel(String StartUserTel) {
        this.StartUserTel = StartUserTel;
    }

    public String getProjectSystemAuthorized_Id() {
        return ProjectSystemAuthorized_Id;
    }

    public void setProjectSystemAuthorized_Id(String ProjectSystemAuthorized_Id) {
        this.ProjectSystemAuthorized_Id = ProjectSystemAuthorized_Id;
    }

    public String getFaultPlace() {
        return FaultPlace;
    }

    public void setFaultPlace(String FaultPlace) {
        this.FaultPlace = FaultPlace;
    }

    public String getRepairContent() {
        return RepairContent;
    }

    public void setRepairContent(String RepairContent) {
        this.RepairContent = RepairContent;
    }

    public String getOtherContent() {
        return OtherContent;
    }

    public void setOtherContent(String OtherContent) {
        this.OtherContent = OtherContent;
    }

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }

    public boolean isIsShowPosition() {
        return IsShowPosition;
    }

    public void setIsShowPosition(boolean IsShowPosition) {
        this.IsShowPosition = IsShowPosition;
    }

    public String getEndTypeShow() {
        return EndTypeShow;
    }

    public void setEndTypeShow(String EndTypeShow) {
        this.EndTypeShow = EndTypeShow;
    }

    public String getEndTimeShow() {
        return EndTimeShow;
    }

    public void setEndTimeShow(String EndTimeShow) {
        this.EndTimeShow = EndTimeShow;
    }

    public int getEvaluateLevel() {
        return EvaluateLevel;
    }

    public void setEvaluateLevel(int EvaluateLevel) {
        this.EvaluateLevel = EvaluateLevel;
    }

    public String getProjectNameShow() {
        return ProjectNameShow;
    }

    public void setProjectNameShow(String ProjectNameShow) {
        this.ProjectNameShow = ProjectNameShow;
    }

    public String getProjectSystemNameShow() {
        return ProjectSystemNameShow;
    }

    public void setProjectSystemNameShow(String ProjectSystemNameShow) {
        this.ProjectSystemNameShow = ProjectSystemNameShow;
    }

    public String getStartTimeShow() {
        return StartTimeShow;
    }

    public void setStartTimeShow(String StartTimeShow) {
        this.StartTimeShow = StartTimeShow;
    }

    public String getMaintenancePersonConfirmTimeShow() {
        return MaintenancePersonConfirmTimeShow;
    }

    public void setMaintenancePersonConfirmTimeShow(String MaintenancePersonConfirmTimeShow) {
        this.MaintenancePersonConfirmTimeShow = MaintenancePersonConfirmTimeShow;
    }

    public String getAcceptUserInfoShow() {
        return AcceptUserInfoShow;
    }

    public void setAcceptUserInfoShow(String AcceptUserInfoShow) {
        this.AcceptUserInfoShow = AcceptUserInfoShow;
    }

    public String getAcceptUserInfoTel() {
        return AcceptUserInfoTel;
    }

    public void setAcceptUserInfoTel(String AcceptUserInfoTel) {
        this.AcceptUserInfoTel = AcceptUserInfoTel;
    }

    public String getStateShow() {
        return StateShow;
    }

    public void setStateShow(String StateShow) {
        this.StateShow = StateShow;
    }

    public String getIsReminderShow() {
        return IsReminderShow;
    }

    public void setIsReminderShow(String IsReminderShow) {
        this.IsReminderShow = IsReminderShow;
    }

    public String getStateIconUrl() {
        return StateIconUrl;
    }

    public void setStateIconUrl(String StateIconUrl) {
        this.StateIconUrl = StateIconUrl;
    }

    @Override
    public String toString() {
        return "MaintenListBackBean{" +
                "ProjectId='" + ProjectId + '\'' +
                ", SystemId='" + SystemId + '\'' +
                ", Code='" + Code + '\'' +
                ", StartUserName='" + StartUserName + '\'' +
                ", StartUserTel='" + StartUserTel + '\'' +
                ", ProjectSystemAuthorized_Id='" + ProjectSystemAuthorized_Id + '\'' +
                ", FaultPlace='" + FaultPlace + '\'' +
                ", RepairContent='" + RepairContent + '\'' +
                ", OtherContent='" + OtherContent + '\'' +
                ", State=" + State +
                ", IsShowPosition=" + IsShowPosition +
                ", EndTypeShow='" + EndTypeShow + '\'' +
                ", EndTimeShow='" + EndTimeShow + '\'' +
                ", EvaluateLevel=" + EvaluateLevel +
                ", ProjectNameShow='" + ProjectNameShow + '\'' +
                ", ProjectSystemNameShow='" + ProjectSystemNameShow + '\'' +
                ", StartTimeShow='" + StartTimeShow + '\'' +
                ", MaintenancePersonConfirmTimeShow='" + MaintenancePersonConfirmTimeShow + '\'' +
                ", AcceptUserInfoShow='" + AcceptUserInfoShow + '\'' +
                ", AcceptUserInfoTel='" + AcceptUserInfoTel + '\'' +
                ", StateShow='" + StateShow + '\'' +
                ", IsReminderShow='" + IsReminderShow + '\'' +
                ", IsReminder='" + IsReminder + '\'' +
                ", StateIconUrl='" + StateIconUrl + '\'' +
                '}';
    }
}
