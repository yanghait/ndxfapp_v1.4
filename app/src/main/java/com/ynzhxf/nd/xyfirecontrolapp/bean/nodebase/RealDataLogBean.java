package com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.BaseDataBean;

/**
 * 标签数据记录实体
 * Created by nd on 2018-07-24.
 */

public class RealDataLogBean extends BaseDataBean{
    /**
     * 记录值
     */
    private String Value;

    /**
     * 翻译值
     */
    private String TranseValue;

    /**
     * 标签ID
     */
    private String LabelTagID;

    /**
     * 时间字符串
     */

    private String TimeStr;

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getTranseValue() {
        return TranseValue;
    }

    public void setTranseValue(String transeValue) {
        TranseValue = transeValue;
    }

    public String getLabelTagID() {
        return LabelTagID;
    }

    public void setLabelTagID(String labelTagID) {
        LabelTagID = labelTagID;
    }

    public String getTimeStr() {
        return TimeStr;
    }

    public void setTimeStr(String timeStr) {
        TimeStr = timeStr;
    }


}
