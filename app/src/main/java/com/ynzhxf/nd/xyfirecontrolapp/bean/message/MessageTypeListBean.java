package com.ynzhxf.nd.xyfirecontrolapp.bean.message;

public class MessageTypeListBean {


    /**
     * UploadPath : /Uploads/Images/Icon/o_1cr27tjfgjjts7h7ta9m8an07.png
     * TypeId : b65e19f22b3e4137b71d36a972e91aad
     * Type : null
     * Value : 0
     * Name : 历史报警
     * ID : 69
     * IsNew : true
     */

    private String UploadPath;
    private String TypeId;
    private Object Type;
    private String Value;
    private String Name;
    private String ID;
    private boolean IsNew;

    public String getUploadPath() {
        return UploadPath;
    }

    public void setUploadPath(String UploadPath) {
        this.UploadPath = UploadPath;
    }

    public String getTypeId() {
        return TypeId;
    }

    public void setTypeId(String TypeId) {
        this.TypeId = TypeId;
    }

    public Object getType() {
        return Type;
    }

    public void setType(Object Type) {
        this.Type = Type;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
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
