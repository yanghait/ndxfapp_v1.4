package com.ynzhxf.nd.xyfirecontrolapp.presenter;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;

/**
 *
 * Created by nd on 2018-07-13.
 */

public interface IBasePersenter{


    /**
     * 处理异常回调
     * @param result
     */
    void callBackError(ResultBean<String,String> result,String action);
    /**
     * 释放对View引用
     */
    void detachView();

}
