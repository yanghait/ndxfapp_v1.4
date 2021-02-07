package com.ynzhxf.nd.xyfirecontrolapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.platform.LoginInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IUserInfoGetPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.impl.PlatfromPersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.SystemInfoActivity;

import java.util.List;

/**
 * 统计
 * Created by nd on 2018-07-11.
 */

public class PersonFragment extends BaseFragment implements IUserInfoGetPersenter.IUserInfoGetView {
    //用户名
    private TextView txtUsername;

    //用户姓名
    private TextView txtName;
    //所属单位
    private TextView txtCompany;
    //职务
    private TextView txtOccount;
    //电话
    private TextView txtPhone;
    //设置
    private TextView lySetting;

    private View rlSystemSetting;

    private IUserInfoGetPersenter persenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, null, false);
        txtUsername = view.findViewById(R.id.txt_username);
        txtName = view.findViewById(R.id.txt_name);
        txtCompany = view.findViewById(R.id.txt_company);
        txtOccount = view.findViewById(R.id.txt_occount);
        txtPhone = view.findViewById(R.id.txt_phone);
        rlSystemSetting = view.findViewById(R.id.system_setting);

        rlSystemSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AndPermission.with(getContext())
                        .runtime()
                        .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                Intent intent = new Intent(getContext(), SystemInfoActivity.class);
                                startActivity(intent);
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                ToastUtils.showLong("权限被拒绝,功能无法使用!");
                            }
                        })
                        .start();
            }
        });
        persenter = PlatfromPersenterFactory.getUserInfoGetPersenterImpl(this);
        addPersenter(persenter);
        persenter.dolUserInfoGetPersenter();
        return view;
    }

    public static PersonFragment newInstance() {
        return new PersonFragment();
    }

    @Override
    public void callBackLoginKeyGet(ResultBean<LoginInfoBean, String> resultBean) {
        if (resultBean.isSuccess()) {
            LoginInfoBean data = resultBean.getData();
            txtUsername.setText(data.getUserName());
            txtName.setText(data.getName());
            txtOccount.setText(data.getOccupation());
            txtCompany.setText(data.getOrgName());
            txtPhone.setText(data.getPhone());
        } else {
            HelperView.Toast(context, resultBean.getMessage());
        }
    }
}
