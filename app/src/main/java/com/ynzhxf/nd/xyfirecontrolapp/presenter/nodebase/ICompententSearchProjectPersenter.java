package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

/**
 * 主管部门区域ID和关键字获取项目列表分页
 * Created by nd on 2018-07-16.
 */

public interface ICompententSearchProjectPersenter extends IBasePersenter{
    String  TAG = "CompententSearch";
    /**
     * 视图接口
     */
    interface ICompententSearchProjectView extends IBaseView {
        /**
         *数据请求完成回调
         */
        void callBackCompententSearchProject(ResultBean<PagingBean<ProjectNodeBean>, String> result);

    }

    /**
     * 模型接口
     */
    interface ICompententSearchProjectModel{

        /**
         * 数据请求
         * @param PageSize 当前页码
         * @param Token 令牌
         * @param AreaID 区域ID
         * @param keyWord 关键字
         */
        void requestCompententSearchProject(int PageSize, String Token, String AreaID,String keyWord);
    }

    /**
     * 发送数据请求
     */
    void doCompententArea(int PageSize, String Token, String AreaID,String keyWord);

    /**
     * 完成请求返回数据
     */
    void callBackCompententSearchProject(ResultBean<PagingBean<ProjectNodeBean>, String> result);
}
