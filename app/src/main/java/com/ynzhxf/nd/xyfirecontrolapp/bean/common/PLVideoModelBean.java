package com.ynzhxf.nd.xyfirecontrolapp.bean.common;


/**
 * author hbzhou
 * date 2019/7/29 11:13
 */
public class PLVideoModelBean {

    /**
     * ID : 37d0cb36790441d7991ef64b5107914c
     * RoomId : 37d0cb36790441d7991ef64b5107914c
     * Title : 年度会议
     * Url : rtmp://192.168.50.153:10085/hls/37d0cb36790441d7991ef64b5107914c
     */

    private String ID;
    private String RoomId;
    private String Title;
    private String Url;
    private String Server;
    private String OrganizationName;
    private String Token;

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getOrganizationName() {
        return OrganizationName;
    }

    public void setOrganizationName(String organizationName) {
        OrganizationName = organizationName;
    }

    public String getServer() {
        return Server;
    }

    public void setServer(String server) {
        Server = server;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getRoomId() {
        return RoomId;
    }

    public void setRoomId(String RoomId) {
        this.RoomId = RoomId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }
}
