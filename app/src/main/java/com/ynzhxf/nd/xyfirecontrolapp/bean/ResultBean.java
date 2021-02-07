package com.ynzhxf.nd.xyfirecontrolapp.bean;

import java.io.Serializable;

/**
 * Created by nd on 2018-07-12.
 */

public class ResultBean<T,H> implements Serializable {
    /**
     * 参数
     */
    private String pars;
    /**
     * 返回消息
     */
    private String message;

    /**
     * 请求是否成功
     */
    private boolean success;
    /**
     * 请求返回数据
     */
    private T data;

    /**
     * 是否登陆
     */
    private boolean isLogin;

    /**
     * 登陆消息
     */
    private String loginMsg;

    //状态码
    private int code;

    /**
     * 额外数据
     */
    private H extra;

    public String getPars() {
        return pars;
    }

    public void setPars(String pars) {
        this.pars = pars;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getLoginMsg() {
        return loginMsg;
    }

    public void setLoginMsg(String loginMsg) {
        this.loginMsg = loginMsg;
    }

    public H getExtra() {
        return extra;
    }

    public void setExtra(H extra) {
        this.extra = extra;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
