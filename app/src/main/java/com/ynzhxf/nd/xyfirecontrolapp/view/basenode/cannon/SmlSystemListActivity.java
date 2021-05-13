package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.ui.GridDividerDecoration;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.adapter.SmlSysAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.bean.SmlSystemInfo;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.SmlSysPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.view.ISmlSysListView;

import java.util.List;

public class SmlSystemListActivity extends BaseActivity implements ISmlSysListView {

    SmlSysPresenter smlSysPresenter;

    RecyclerView sml_sys_recycler;

    SmlSysAdapter smlSysAdapter;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sml_system_list);
        super.onCreate(savedInstanceState);
        smlSysPresenter = new SmlSysPresenter(this);
        initTitle();
        initLayout();
    }

    private void initTitle() {
        setBarTitle("系统列表");
    }

    private void initLayout() {
        sml_sys_recycler = this.findViewById(R.id.sml_sys_recycler);
        smlSysAdapter = new SmlSysAdapter();
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        GridDividerDecoration dividerDecoration = new GridDividerDecoration(2, getResources().getDimensionPixelSize(R.dimen.margin_top_bottom), true);
        sml_sys_recycler.addItemDecoration(dividerDecoration);
        sml_sys_recycler.setLayoutManager(manager);
        sml_sys_recycler.setAdapter(smlSysAdapter);
        dialog = showProgress(this, "加载中...", false);
        smlSysPresenter.loginToSml();

        smlSysAdapter.setOnItemClickListener((typeId,sysId) -> {
            Intent intent = null;
            if (typeId.equals("11000")) {
                intent = new Intent(this, WaterCannonListActivity.class);
                intent.putExtra("systemId", typeId);
                startActivity(intent);
            } else if (typeId.equals("665")) {
                intent = new Intent(this, SmartPowerListActivity.class);
                intent.putExtra("systemId", sysId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void loginSuccess() {
        smlSysPresenter.getSmlSysList();
    }

    @Override
    public void getSysListSuccess(List<SmlSystemInfo> data) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        smlSysAdapter.update(data);
    }
}