package com.ynzhxf.nd.xyfirecontrolapp.model.share;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareMyFileBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareMyFileInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.share.IFileShareMyFileListPresenter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class FileShareMyFileListModel extends BaseModel implements IFileShareMyFileListPresenter.IFileShareMyFileListModel {

    private IFileShareMyFileListPresenter presenter;

    public FileShareMyFileListModel(IFileShareMyFileListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void requestFileShareMyFileList(FileShareMyFileInputBean bean) {
        Observable<ResultBean<List<FileShareMyFileBean>, String>> observable;
        if (bean.isShareList()) {
            observable = HttpUtils.getRetrofit().getFileShareAllFileList(bean.getToken(), bean.getProjectId(), bean.getTypeId(), bean.getPageIndex(), bean.getPageSize(), bean.getKeyword()
                    , bean.getStartTime(), bean.getEndTime());
        } else {
            observable = HttpUtils.getRetrofit().getFileShareMyFileList(bean.getToken(), bean.getProjectId(), bean.getTypeId(), bean.getPageIndex(), bean.getPageSize(), bean.getKeyword()
                    , bean.getStartTime(), bean.getEndTime());
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<List<FileShareMyFileBean>, String>>() {
                    @Override
                    public void onNext(ResultBean<List<FileShareMyFileBean>, String> resultBean) {
                        presenter.callBackFileShareMyFileList(resultBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        presenter.callBackError(createResult(e), IFileShareMyFileListPresenter.TAG);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
