package com.ynzhxf.nd.xyfirecontrolapp.bean.common;

import java.io.Serializable;

/**
 * 新闻实体对象
 * Created by nd on 2018-07-28.
 */

public class NewsBean implements Serializable,Cloneable {

    ///<summary>
    ///新闻ID
    /// </summary>
    private String ID;

    ///<summary>
    ///新闻标题
    /// </summary>
    private String NewsTitle;

    ///<summary>
    ///新闻内容
    /// </summary>
    private String NewsContent;

    ///<summary>
    ///新闻图片
    /// </summary>
    private String NewsImg;

    ///<summary>
    ///发布时间
    /// </summary>
    private String NewsModifyTime;

    ///<summary>
    ///组织机构ID
    /// </summary>
    private String PID;

    ///<summary>
    ///组织机构名称
    /// </summary>
    private String PName;

    ///<summary>
    ///发布人ID
    /// </summary>
    private String UID ;

    ///<summary>
    ///发布人名称
    /// </summary>
    private String UName ;

    ///<summary>
    ///审核标识
    /// </summary>
    private boolean IsAudit;

    ///<summary>
    ///点击量
    /// </summary>
    private long ClickAmount;

    private String NewsCreateTimeStr;

    public String getNewsCreateTimeStr() {
        return NewsCreateTimeStr;
    }

    public void setNewsCreateTimeStr(String newsCreateTimeStr) {
        NewsCreateTimeStr = newsCreateTimeStr;
    }

    public String getNewsID() {
        return ID;
    }

    public void setNewsID(String newsID) {
        ID = newsID;
    }

    public String getNewsTitle() {
        return NewsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        NewsTitle = newsTitle;
    }

    public String getNewsContent() {
        return NewsContent;
    }

    public void setNewsContent(String newsContent) {
        NewsContent = newsContent;
    }

    public String getNewsImg() {
        return NewsImg;
    }

    public void setNewsImg(String newsImg) {
        NewsImg = newsImg;
    }

    public String getNewsModifyTime() {
        return NewsModifyTime;
    }

    public void setNewsModifyTime(String newsModifyTime) {
        NewsModifyTime = newsModifyTime;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getPName() {
        return PName;
    }

    public void setPName(String PName) {
        this.PName = PName;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUName() {
        return UName;
    }

    public void setUName(String UName) {
        this.UName = UName;
    }

    public boolean isAudit() {
        return IsAudit;
    }

    public void setAudit(boolean audit) {
        IsAudit = audit;
    }

    public long getClickAmount() {
        return ClickAmount;
    }

    public void setClickAmount(long clickAmount) {
        ClickAmount = clickAmount;
    }
}
