package com.ynzhxf.nd.xyfirecontrolapp.presenter.common;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

/**
 *
 * 获取新闻的网页地址，并对阅读量加1
 * Created by nd on 2018-07-28.
 */

public interface INewsURLPersenter extends IBasePersenter {

    String TAG = "NewsURL";
    /**
     * 视图接口
     */
    interface INewsURLView extends IBaseView {
        /**
         *数据请求完成回调
         */
        void callBackNewsURL(ResultBean<String, String> result);

    }

    /**
     * 模型接口
     */
    interface INewsURLModel{

        /**
         * 数据加载请求
         * @param newsID 新闻ID
         */
        void requestNewsURL(String newsID);
    }

    /**
     * 发送数据请求
     * @param  newsID 新闻ID
     */
    void doNewsURL(String newsID);

    /**
     * 完成请求返回数据
     */
    void callBackNewsURL(ResultBean<String, String> result);
}
