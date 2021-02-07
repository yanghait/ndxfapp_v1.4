package com.ynzhxf.nd.xyfirecontrolapp.bean.message;

import com.ynzhxf.nd.xyfirecontrolapp.bean.BaseDataBean;

/**
 * 用户消息推送记录
 * Created by nd on 2018-08-02.
 */

public class UserMessagePushLogBean extends BaseDataBean{

    /// <summary>
    /// 关联用户ID
    /// </summary>
    private String UserID;
    /// <summary>
    /// 消息ID
    /// </summary>
    private String AppPushMsgLogID;

    //消息体
    private MessagePushLogBean AppPushMsgLogObj;

    //是否查看
    private boolean HasSee;

    /// <summary>
    /// 首次推送时间
    /// </summary>

    private String  FirstPushTimeStr;

    /// <summary>
    /// 最后一次推送时间
    /// </summary>
    private String LastPushTimeStr;

    private String MaintenanceState;
    private String ProjectId;

    public String getMaintenanceState() {
        return MaintenanceState;
    }

    public void setMaintenanceState(String maintenanceState) {
        MaintenanceState = maintenanceState;
    }

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String projectId) {
        ProjectId = projectId;
    }

    /// <summary>
    /// 推送次数
    /// </summary>
    private int PushCount;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getAppPushMsgLogID() {
        return AppPushMsgLogID;
    }

    public void setAppPushMsgLogID(String appPushMsgLogID) {
        AppPushMsgLogID = appPushMsgLogID;
    }

    public MessagePushLogBean getAppPushMsgLogObj() {
        return AppPushMsgLogObj;
    }

    public void setAppPushMsgLogObj(MessagePushLogBean appPushMsgLogObj) {
        AppPushMsgLogObj = appPushMsgLogObj;
    }

    public boolean isHasSee() {
        return HasSee;
    }

    public void setHasSee(boolean hasSee) {
        HasSee = hasSee;
    }

    public String getFirstPushTimeStr() {
        return FirstPushTimeStr;
    }

    public void setFirstPushTimeStr(String firstPushTimeStr) {
        FirstPushTimeStr = firstPushTimeStr;
    }

    public String getLastPushTimeStr() {
        return LastPushTimeStr;
    }

    public void setLastPushTimeStr(String lastPushTimeStr) {
        LastPushTimeStr = lastPushTimeStr;
    }

    public int getPushCount() {
        return PushCount;
    }

    public void setPushCount(int pushCount) {
        PushCount = pushCount;
    }
}
