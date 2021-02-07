package com.ynzhxf.nd.xyfirecontrolapp.presenter;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;

/**
 * Created by nd on 2018-07-14.
 */

public interface IBaseView {

    /**
     * 视图对请求过程中发生的服务异常的处理
     * @param resultBean
     * @param code 请求标识
     */
    void callBackError(ResultBean<String,String> resultBean,String code);
}
