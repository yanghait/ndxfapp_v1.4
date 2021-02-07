package com.ynzhxf.nd.xyfirecontrolapp.model.share;

import com.ynzhxf.nd.xyfirecontrolapp.presenter.share.IFileShareFileTypePresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.share.IFileShareMyFileListPresenter;

public class FileShareFactory {

    // 文件分享文件分类列表
    public static IFileShareFileTypePresenter.IFileShareFileTypeModel getFileShareFileType(IFileShareFileTypePresenter presenter) {
        return new FileShareFileTypeModel(presenter);
    }

    // 文件分享我的文件列表
    public static IFileShareMyFileListPresenter.IFileShareMyFileListModel getFileShareMyFile(IFileShareMyFileListPresenter presenter) {
        return new FileShareMyFileListModel(presenter);
    }
}
