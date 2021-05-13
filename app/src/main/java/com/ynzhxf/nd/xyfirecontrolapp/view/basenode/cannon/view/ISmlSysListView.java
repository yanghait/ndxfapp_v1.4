package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.view;

import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.bean.SmlSystemInfo;

import java.util.List;

public interface ISmlSysListView {

     void loginSuccess();
     void getSysListSuccess(List<SmlSystemInfo> data);
}
