package com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance;

public class OwnerOrderDetailsInputBean {
    private String Token;
    private String workOrderId;
    private int detailType;

    public int getDetailType() {
        return detailType;
    }

    public void setDetailType(int detailType) {
        this.detailType = detailType;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
}
