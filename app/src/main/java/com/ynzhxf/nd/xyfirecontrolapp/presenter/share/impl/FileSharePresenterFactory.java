package com.ynzhxf.nd.xyfirecontrolapp.presenter.share.impl;

import com.ynzhxf.nd.xyfirecontrolapp.presenter.share.IFileShareFileTypePresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.share.IFileShareMyFileListPresenter;

public class FileSharePresenterFactory {

    /**
     * 获取文件分类列表
     *
     * @param view
     * @return
     */
    public static IFileShareFileTypePresenter getFileShareFileTypePresenterImpl(IFileShareFileTypePresenter.IFileShareFileTypeView view) {
        return new FileShareFileTypeImpl(view);
    }

    /**
     * 获取我的文件列表
     *
     * @param view
     * @return
     */
    public static IFileShareMyFileListPresenter getFileShareMyFilePresenterImpl(IFileShareMyFileListPresenter.IFileShareMyFileListView view) {
        return new FileShareMyFileListImpl(view);
    }
}
