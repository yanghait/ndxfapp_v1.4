package com.ynzhxf.nd.xyfirecontrolapp.model;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

/**
 * Created by nd on 2018-07-13.
 */

public class BaseModel {

    /**
     * 根据请求异常创建一个返回对象
     * @param e 异常对象
     * @return
     */
    public ResultBean<String,String> createResult(Throwable e){
        ResultBean<String,String> resultBean = new ResultBean<>();
        if(e instanceof ConnectException){ //网络异常
            resultBean.setCode(-2);
            resultBean.setMessage("请检查网络设置！");
        }else if (e instanceof HttpException){//HTTP网络请求异常
            HttpException query = (HttpException)e;
            int code = query.code();
            resultBean.setCode(query.code());
            switch (code){
                case 401:
                    resultBean.setMessage("未授权或授权超时！");
                    break;
                case 404:
                    resultBean.setMessage("接口定义错误！");
                case 500:
                    resultBean.setMessage("服务异常");
                break;
                default:
                    resultBean.setMessage(e.getMessage()+":"+code);
            }
        }else if (e instanceof SocketTimeoutException){
            resultBean.setMessage("请求超时！");
            resultBean.setCode(-2);
        }else {
            resultBean.setCode(-1);
            resultBean.setMessage("未知异常："+e.getMessage());
        }

        return resultBean;
    }


}
