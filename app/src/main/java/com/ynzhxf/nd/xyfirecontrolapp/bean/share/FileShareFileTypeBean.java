package com.ynzhxf.nd.xyfirecontrolapp.bean.share;

public class FileShareFileTypeBean {

    /**
     * F_Name : 标题啊啊啊
     * F_projectid : 94b6970a772448cea351e438d4e9487e
     * F_Describe : 啊啊啊啊啊啊啊啊
     * F_DelState : null
     * ID : 07b94a6af3194e18ae703cf85aea07e9
     * IsNew : true
     */

    private String F_Name;
    private String F_projectid;
    private String F_Describe;
    private Object F_DelState;
    private String ID;
    private boolean IsNew;

    public String getF_Name() {
        return F_Name;
    }

    public void setF_Name(String F_Name) {
        this.F_Name = F_Name;
    }

    public String getF_projectid() {
        return F_projectid;
    }

    public void setF_projectid(String F_projectid) {
        this.F_projectid = F_projectid;
    }

    public String getF_Describe() {
        return F_Describe;
    }

    public void setF_Describe(String F_Describe) {
        this.F_Describe = F_Describe;
    }

    public Object getF_DelState() {
        return F_DelState;
    }

    public void setF_DelState(Object F_DelState) {
        this.F_DelState = F_DelState;
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

    @Override
    public String toString() {
        return "FileShareFileTypeBean{" +
                "F_Name='" + F_Name + '\'' +
                ", F_projectid='" + F_projectid + '\'' +
                ", F_Describe='" + F_Describe + '\'' +
                ", F_DelState=" + F_DelState +
                ", ID='" + ID + '\'' +
                ", IsNew=" + IsNew +
                '}';
    }
}
