package com.ynzhxf.nd.xyfirecontrolapp.bean.message;

import com.ynzhxf.nd.xyfirecontrolapp.bean.BaseDataBean;

/**
 * 消息内容对象
 * Created by nd on 2018-08-01.
 */

public class MessagePushLogBean extends BaseDataBean{

    /// <summary>
    /// 创建时间
    /// </summary>
    private String CreateTime;

    /// <summary>
    /// 推送消息标题
    /// </summary>
    private String MessageTitle ;

    /// <summary>
    /// 推送消息内容
    /// </summary>
    private String MessageContent;

    /// <summary>
    /// 额外的内容
    /// </summary>
    private String MessageExtra;

    /// <summary>
    /// 推送消息的类型
    /// </summary>
    public String PushMessageTypeID;

    /// <summary>
    /// 该消息关联的ID
    /// </summary>
    private String RelationID;


    private String AlarmLogID;

    /// <summary>
    /// 激光推送的消息ID
    /// </summary>
    private String JPushMessageID;

    /// <summary>
    /// 是否要使用浏览器打开
    /// </summary>
    private boolean IsExternalLink;


    /// <summary>
    /// 要打开链接的地址
    /// </summary>
    private String TargetUrl;

    /// <summary>
    /// 该消息是否推送成功（指是消息是否发送到推送平台）
    /// </summary>
    private boolean IsPushSuccess;

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getMessageTitle() {
        return MessageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        MessageTitle = messageTitle;
    }

    public String getMessageContent() {
        return MessageContent;
    }

    public void setMessageContent(String messageContent) {
        MessageContent = messageContent;
    }

    public String getMessageExtra() {
        return MessageExtra;
    }

    public void setMessageExtra(String messageExtra) {
        MessageExtra = messageExtra;
    }

    public String getPushMessageTypeID() {
        return PushMessageTypeID;
    }

    public void setPushMessageTypeID(String pushMessageTypeID) {
        PushMessageTypeID = pushMessageTypeID;
    }

    public String getRelationID() {
        return RelationID;
    }

    public void setRelationID(String relationID) {
        RelationID = relationID;
    }

    public String getAlarmLogID() {
        return AlarmLogID;
    }

    public void setAlarmLogID(String alarmLogID) {
        AlarmLogID = alarmLogID;
    }


    public boolean isExternalLink() {
        return IsExternalLink;
    }

    public void setExternalLink(boolean externalLink) {
        IsExternalLink = externalLink;
    }

    public String getTargetUrl() {
        return TargetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        TargetUrl = targetUrl;
    }

    public boolean isPushSuccess() {
        return IsPushSuccess;
    }

    public void setPushSuccess(boolean pushSuccess) {
        IsPushSuccess = pushSuccess;
    }

    public String getJPushMessageID() {
        return JPushMessageID;
    }

    public void setJPushMessageID(String JPushMessageID) {
        this.JPushMessageID = JPushMessageID;
    }
}
