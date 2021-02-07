package com.ynzhxf.nd.xyfirecontrolapp.bean.share;

public class FileShareMyFileInputBean {
    private String Token;
    private String projectId;
    private String typeId;

    private String pageIndex;
    private String pageSize;
    private String keyword;
    private String startTime;

    private String endTime;

    private boolean isShareList;

    public boolean isShareList() {
        return isShareList;
    }

    public void setShareList(boolean shareList) {
        isShareList = shareList;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
        return "FileShareMyFileInputBean{" +
                "Token='" + Token + '\'' +
                ", projectId='" + projectId + '\'' +
                ", typeId='" + typeId + '\'' +
                ", pageIndex='" + pageIndex + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", keyword='" + keyword + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", isShareList=" + isShareList +
                '}';
    }
}
