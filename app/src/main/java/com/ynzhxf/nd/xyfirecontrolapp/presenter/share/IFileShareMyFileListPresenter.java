package com.ynzhxf.nd.xyfirecontrolapp.presenter.share;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareMyFileBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareMyFileInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.List;

public interface IFileShareMyFileListPresenter extends IBasePersenter {

    String TAG = "IFileShareMyFileListPresenter";

    interface IFileShareMyFileListView extends IBaseView {
        /**
         * 文件分享我的文件回调
         *
         * @param
         */
        void callBackFileShareMyFileList(ResultBean<List<FileShareMyFileBean>, String> resultBean);

    }

    interface IFileShareMyFileListModel {
        /**
         * 请求文件分享我的文件
         *
         * @param bean
         */
        void requestFileShareMyFileList(FileShareMyFileInputBean bean);
    }

    /**
     * 文件分享我的文件
     *
     * @param bean
     */
    void doFileShareMyFileList(FileShareMyFileInputBean bean);

    /**
     * 获取文件分享我的文件回调
     *
     * @param
     */
    void callBackFileShareMyFileList(ResultBean<List<FileShareMyFileBean>, String> resultBean);
}
