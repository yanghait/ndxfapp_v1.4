package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.view;

import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.bean.SmartPowerInfo;

import java.util.List;

public interface ISmartPowerListView {
    void getSmartPowerListSuccess(List<SmartPowerInfo> data);
    void writeLableSuccess();
}
