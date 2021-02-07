package com.ynzhxf.nd.xyfirecontrolapp.bean.share;

public class FileShareFileTypeInputBean {

    private String Token;
    private String projectId;
    private String keyword;

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
