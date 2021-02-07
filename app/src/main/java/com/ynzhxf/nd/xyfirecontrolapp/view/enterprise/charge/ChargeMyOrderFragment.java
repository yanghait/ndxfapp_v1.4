package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.charge;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.MaintenanceCompanyActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company.CompanyMyOrderListFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class ChargeMyOrderFragment extends CompanyMyOrderListFragment {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void initFlagCharge() {
        MaintenanceCompanyActivity.detailType = 3;
        context = getActivity();
    }

    @Override
    public void initInputBeanType() {
        inputBean.setCharge(true);
    }

    public static ChargeMyOrderFragment newInstance(String state) {
        Bundle args = new Bundle();
        args.putString("state", state);
        ChargeMyOrderFragment fragment = new ChargeMyOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private static void initTestData(String projectId) {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("state", "90");
        params.put("pageIndex", "1");
        params.put("pageSize", "20");
        params.put("projectId", projectId);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_CHARGE_GET_ORDER_LIST))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //HelperView.Toast(MaintenanceCompanyActivity.this, e.getMessage());
                        LogUtils.showLoge("维保主管部门列表错误1111---", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("维保主管部门列表返回0909---", response);
                    }
                });
    }
}
