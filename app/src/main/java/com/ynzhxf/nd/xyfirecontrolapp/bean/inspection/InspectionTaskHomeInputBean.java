package com.ynzhxf.nd.xyfirecontrolapp.bean.inspection;

public class InspectionTaskHomeInputBean {

    private String Token;
    private String State;

    private String PageIndex;

    private String PageSize;

    private String projectId;

    private String startTime;

    private String endTime;

    private String inspectTypeId;

    private boolean isCompany;

    private boolean isCompanyForOwner;

    public boolean isCompanyForOwner() {
        return isCompanyForOwner;
    }

    public void setCompanyForOwner(boolean companyForOwner) {
        isCompanyForOwner = companyForOwner;
    }

    public boolean isCompany() {
        return isCompany;
    }

    public void setCompany(boolean company) {
        isCompany = company;
    }

    public String getInspectTypeId() {
        return inspectTypeId;
    }

    public void setInspectTypeId(String inspectTypeId) {
        this.inspectTypeId = inspectTypeId;
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    @Override
    public String toString() {
        return "InspectionTaskHomeInputBean{" +
                "Token='" + Token + '\'' +
                ", State='" + State + '\'' +
                ", PageIndex='" + PageIndex + '\'' +
                ", PageSize='" + PageSize + '\'' +
                ", projectId='" + projectId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", inspectTypeId='" + inspectTypeId + '\'' +
                ", isCompany=" + isCompany +
                '}';
    }
}
