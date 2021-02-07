package com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance;

import com.ynzhxf.nd.xyfirecontrolapp.bean.BaseDataBean;

public class MaintenanceListInfoBean extends BaseDataBean {
    private String Token;
    private String State;
    private String PageIndex;
    private String PageSize;
    private String startTime;
    private String endTime;
    private String isWorking;
    private String projectId;
    private boolean isCompany;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getIsWorking() {
        return isWorking;
    }

    public void setIsWorking(String isWorking) {
        this.isWorking = isWorking;
    }

    public boolean isCompany() {
        return isCompany;
    }

    public void setCompany(boolean company) {
        isCompany = company;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(String pageIndex) {
        PageIndex = pageIndex;
    }

    public String getPageSize() {
        return PageSize;
    }

    public void setPageSize(String pageSize) {
        PageSize = pageSize;
    }

    @Override
    public String toString() {
        return "MaintenanceListInfoBean{" +
                "Token='" + Token + '\'' +
                ", State='" + State + '\'' +
                ", PageIndex='" + PageIndex + '\'' +
                ", PageSize='" + PageSize + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", isWorking='" + isWorking + '\'' +
                ", projectId='" + projectId + '\'' +
                ", isCompany=" + isCompany +
                '}';
    }
}
