package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.bean;

import com.google.gson.annotations.SerializedName;

public class SmlSystemInfo {
    @SerializedName("ProjectSystemId")
    String ProjectSystemId = "";
    @SerializedName("ProjectSystemName")
    String ProjectSystemName = "";
    @SerializedName("ProjectSystemTypeID")
    String ProjectSystemTypeID;
    @SerializedName("AlarmCount")
    int AlarmCount = 0;
    @SerializedName("TagCount")
    int TagCount = 0;

    public String getProjectSystemId() {
        return ProjectSystemId;
    }

    public String getProjectSystemTypeID() {
        return ProjectSystemTypeID;
    }

    public String getProjectSystemName() {
        return ProjectSystemName;
    }

    public int getAlarmCount() {
        return AlarmCount;
    }

    public int getTagCount() {
        return TagCount;
    }
}
