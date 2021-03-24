package com.ynzhxf.nd.xyfirecontrolapp.bean.inspection;

import com.google.gson.annotations.SerializedName;

public class CheckPointBean {
    @SerializedName("Name")
    String Name = "";
    @SerializedName("InspectItemTypes")
    String InspectItemTypes = "";
    @SerializedName("ProjectId")
    String ProjectId = "";
    @SerializedName("AreaId")
    String AreaId = "";
    @SerializedName("ID")
    String ID = "";
    @SerializedName("QrCode")
    String QrCode = "";

    public String getName() {
        return Name;
    }

    public String getInspectItemTypes() {
        return InspectItemTypes;
    }

    public String getProjectId() {
        return ProjectId;
    }

    public String getAreaId() {
        return AreaId;
    }

    public String getID() {
        return ID;
    }

    public String getQrCode() {
        return QrCode;
    }
}
