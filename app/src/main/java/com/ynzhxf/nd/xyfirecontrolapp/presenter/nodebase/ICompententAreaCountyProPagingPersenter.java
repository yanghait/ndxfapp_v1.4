package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

/**
 * 主管部门县区级项目数据分页获取
 * Created by nd on 2018-07-20.
 */

public interface ICompententAreaCountyProPagingPersenter extends IBasePersenter {
    String TAG = "CompententAreaCountyProPaging";
    interface ICompententAreaCountyProPagingView extends IBaseView{
        /**
         *数据请求完成回调
         */
        void callBackCompententAreaCountyProPaging(ResultBean<PagingBean<ProjectNodeBean>, String> result);

    }

    /**
     * 模型接口
     */
    interface ICompententAreaCountyProPagingModel{
        /**
         * 数据加载请求
         */
        void requestCompententAreaCountyProPaging(int pageSize,String areaID);
    }

    /**
     * 发送数据请求
     * @param pageSize 当前页面
     * @param areaID 区域ID
     */
    void doCompententAreaCountyProPaging(int pageSize,String areaID);

    /**
     * 完成请求返回数据
     */
    void callBackCompententAreaCountyProPaging(ResultBean<PagingBean<ProjectNodeBean>, String> result);
}
