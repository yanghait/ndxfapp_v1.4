package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise;

public interface FileDownloadCallBack {
    void onStart(int position);

    void onCompleted(int position);

    void onError(int position);
}
