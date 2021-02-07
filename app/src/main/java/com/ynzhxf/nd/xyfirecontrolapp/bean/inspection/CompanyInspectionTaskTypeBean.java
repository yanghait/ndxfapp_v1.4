package com.ynzhxf.nd.xyfirecontrolapp.bean.inspection;


/**
 * author hbzhou
 * date 2019/1/23 14:44
 */
public class CompanyInspectionTaskTypeBean {

    /**
     * Name : 两年检
     * Value : 60
     * ID : cb456f4b13fd4cbdb95a05e881b75f6a
     * IsNew : true
     */

    private String Name;
    private int Value;
    private String ID;
    private boolean IsNew;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getValue() {
        return Value;
    }

    public void setValue(int Value) {
        this.Value = Value;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isIsNew() {
        return IsNew;
    }

    public void setIsNew(boolean IsNew) {
        this.IsNew = IsNew;
    }
}
