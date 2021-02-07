package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.List;

/**
 * 主管部门区域数据获取桥梁
 * Created by nd on 2018-07-16.
 */

public interface ICompententAreaPersenter extends IBasePersenter{
    String TAG = "CompententArea";
    /**
     * 视图接口
     */
    interface ICompententAreaView extends IBaseView {
        /**
         *数据请求完成回调
         */
        void callBackCompententArea(ResultBean<List<ProjectNodeBean>, String[]> result);

    }

    /**
     * 模型接口
     */
    interface ICompententAreaModel{
        /**
         * 数据加载请求
         */
        void requestCompententArea();
    }

    /**
     * 发送数据请求
     */
    void doCompententArea();

    /**
     * 完成请求返回数据
     */
    void callBackCompententArea(ResultBean<List<ProjectNodeBean>,String[]> result);
}
