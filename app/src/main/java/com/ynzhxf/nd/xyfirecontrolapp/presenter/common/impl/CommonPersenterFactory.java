package com.ynzhxf.nd.xyfirecontrolapp.presenter.common.impl;

import com.ynzhxf.nd.xyfirecontrolapp.presenter.common.INewsListPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.common.INewsURLPersenter;

/**
 * 平台通用小功能，数据获取桥梁生成工厂
 * Created by nd on 2018-07-28.
 */

public class CommonPersenterFactory {

    /**
     * 获取新闻列表数据模型
     * @param view
     * @return
     */
    public static INewsListPersenter getNewListPersenter(INewsListPersenter.INewsListView view){
        return new NewsListPersenterImpl(view);
    }

    /**
     * 获取新闻远程地址链接地址
     * @param view
     * @return
     */
    public  static INewsURLPersenter getNewsURLPersenter(INewsURLPersenter.INewsURLView view){
        return new NewsURLPersenterImpl(view);
    }
}
