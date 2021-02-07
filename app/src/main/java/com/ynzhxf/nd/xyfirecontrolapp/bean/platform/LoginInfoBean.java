package com.ynzhxf.nd.xyfirecontrolapp.bean.platform;

import com.ynzhxf.nd.xyfirecontrolapp.bean.BaseDataBean;

/**
 * Created by nd on 2018-07-12.
 */

public class LoginInfoBean extends BaseDataBean {
    /**
     * 用户名
     */
    private String UserName;
    /**
     * 登陆密码
     */
    private String UserPwd;
    /**
     *设备类型
     */
    private String DevicePlatform;
    /**
     * 设备ID
     */
    private String DeviceUUID;
    /**
     * 令牌
     */
    private String Token;
    /**
     * 用户组织架构类型
     */
    private String UserOrganizationType;

    /**
     * 用户组织名称
     */
    private String OrgName;

    /**
     * 用户组织地址
     */
    private String Orgddress;

    /**
     * 用户姓名
     */
    private String Name;
    /**
     * 用户职务
     */
    private String occupation;

    /**
     * 用户电话
     */
    private String Phone;

    /**
     * 平台登陆令牌
     */
    private String Key;
    /**
     * 验证码
     */
    private String Code;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPwd() {
        return UserPwd;
    }

    public void setUserPwd(String userPwd) {
        UserPwd = userPwd;
    }

    public String getDevicePlatform() {
        return DevicePlatform;
    }

    public void setDevicePlatform(String devicePlatform) {
        DevicePlatform = devicePlatform;
    }

    public String getDeviceUUID() {
        return DeviceUUID;
    }

    public void setDeviceUUID(String deviceUUID) {
        DeviceUUID = deviceUUID;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getUserOrganizationType() {
        return UserOrganizationType;
    }

    public void setUserOrganizationType(String userOrganizationType) {
        UserOrganizationType = userOrganizationType;
    }

    public String getOrgName() {
        return OrgName;
    }

    public void setOrgName(String orgName) {
        OrgName = orgName;
    }

    public String getOrgddress() {
        return Orgddress;
    }

    public void setOrgddress(String orgddress) {
        Orgddress = orgddress;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    @Override
    public String toString() {
        return "LoginInfoBean{" +
                "UserName='" + UserName + '\'' +
                ", UserPwd='" + UserPwd + '\'' +
                ", DevicePlatform='" + DevicePlatform + '\'' +
                ", DeviceUUID='" + DeviceUUID + '\'' +
                ", Token='" + Token + '\'' +
                ", UserOrganizationType='" + UserOrganizationType + '\'' +
                ", OrgName='" + OrgName + '\'' +
                ", Orgddress='" + Orgddress + '\'' +
                ", Name='" + Name + '\'' +
                ", occupation='" + occupation + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Key='" + Key + '\'' +
                ", Code='" + Code + '\'' +
                '}';
    }
}
