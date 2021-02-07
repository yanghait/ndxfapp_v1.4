package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.FireAlarmHostAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectSystemBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmBuildFloorCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmHostBuildCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmHostBuildInfoCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmHostFloorCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IFireAlarmHostListPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;

import java.util.ArrayList;
import java.util.List;

/**
 * 火灾报警主机列表
 */
public class FireAlarmHostActivity extends BaseActivity implements IFireAlarmHostListPersenter.IFireAlarmHostListView, FireAlarmHostAdapter.IFireAlarmHostClick {
    //火灾报警系统对象
    private ProjectSystemBean projectSystemBean;

    //主机列表
    private RecyclerView recyclerView;

    //获取火灾报警数据桥梁
    private IFireAlarmHostListPersenter persenter;

    //火灾报警主机适配器
    private FireAlarmHostAdapter hostAdapter;

    //火灾报警主机数据最终获取
    private List<FireAlarmBuildFloorCountBean> dataList;

    private ProgressDialog dialog;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fire_alarm_host);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Object queryObj = intent.getSerializableExtra("data");
        if (queryObj == null) {
            HelperView.Toast(this, "未选择系统！");
            return;
        }
        persenter = NodeBasePersenterFactory.getFireAlarmHostListPersenterImpl(this);
        addPersenter(persenter);
        projectSystemBean = (ProjectSystemBean) queryObj;
        setBarTitle(projectSystemBean.getName());
        recyclerView = findViewById(R.id.rv_list);
        dataList = new ArrayList<>();
        initView();
        //发送数据请求
        dialog = showProgress(this, "加载中...", false);
        persenter.doFireAlarmHostList(projectSystemBean.getID());
    }

    private void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String action) {
        super.callBackError(resultBean, action);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        //LogUtils.showLoge("输出data数量9999---", "error~~~");
    }

    @Override
    public void callBackFireAlarmHostList(ResultBean<List<FireAlarmHostBuildInfoCountBean>, String> result) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        try {
            if (result.isSuccess()) {
                List<FireAlarmHostBuildInfoCountBean> datas = result.getData();

               // LogUtils.showLoge("输出data数量1212---", datas.size() + "~~~");
                for (int i = 0; i < datas.size(); i++) {
                    FireAlarmHostBuildInfoCountBean queryHost1 = datas.get(i);
                    FireAlarmBuildFloorCountBean queryHost = new FireAlarmBuildFloorCountBean();
                    queryHost.setProjectSystemID(projectSystemBean.getID());
                    queryHost.setHostState(queryHost1.getHostObj().getHostState());
                    queryHost.setHostStateStr(queryHost1.getHostObj().getHostStateStr());
                    queryHost.setHostPortStateStr(queryHost1.getHostObj().getCodeTableId());
                    queryHost.setFireHostId(queryHost1.getHostObj().getHostId());
                    queryHost.setFireHostName(queryHost1.getHostObj().getDescribe());
                    for (int j = 0; j < queryHost1.getBuildList().size(); j++) {
                        FireAlarmHostBuildCountBean queryBuild = queryHost1.getBuildList().get(j);
                        for (int k = 0; k < queryBuild.getFloorCountInfoList().size(); k++) {
                            FireAlarmHostFloorCountBean floor = queryBuild.getFloorCountInfoList().get(k);
                            queryHost.setEquipentCount(queryHost.getEquipentCount() + floor.getInfoCount().getEquipentCount());
                            queryHost.setNormalCount(queryHost.getNormalCount() + floor.getInfoCount().getNormalCount());
                            queryHost.setActionCount(queryHost.getActionCount() + floor.getInfoCount().getActionCount());
                            queryHost.setFaultCount(queryHost.getFaultCount() + floor.getInfoCount().getFaultCount());
                            queryHost.setCloseCount(queryHost.getCloseCount() + floor.getInfoCount().getCloseCount());
                            queryHost.setManageCount(queryHost.getManageCount() + floor.getInfoCount().getManageCount());
                            queryHost.setOpenCount(queryHost.getOpenCount() + floor.getInfoCount().getOpenCount());
                            queryHost.setFeedCount(queryHost.getFeedCount() + floor.getInfoCount().getFeedCount());
                            queryHost.setFireCount(queryHost.getFireCount() + floor.getInfoCount().getFireCount());
                            queryHost.setOutLineCount(queryHost.getOutLineCount() + floor.getInfoCount().getOutLineCount());
                        }
                    }
                    dataList.add(queryHost);
                    hostAdapter = new FireAlarmHostAdapter(dataList, this, getResources().getColor(R.color.flat_alizarin), getResources().getColor(R.color.flat_peterriver));
                    recyclerView.setAdapter(hostAdapter);
                }
                if (dataList.size() == 0) {
                    showNoDataView();
                    return;
                } else {
                    hideNoDataView();
                }
            } else {
                if (dataList.size() == 0) {
                    showNoDataView();
                    return;
                } else {
                    hideNoDataView();
                }
                HelperView.Toast(this, result.getMessage());
            }
        } catch (Exception e) {
            HelperView.Toast(this, "获取报警主机失败：" + result.getMessage());
        }
    }

    /**
     * 点击报警主机列表回调
     *
     * @param query
     */
    @Override
    public void fireAlarmHostClick(FireAlarmBuildFloorCountBean query) {
        Intent intent = new Intent(this, FireAlarmHostBuildActivity.class);
        intent.putExtra("data", query);
        startActivity(intent);
    }

    @Override
    public void fireAlarmDeviceClick(FireAlarmBuildFloorCountBean query) {
        Intent intent = new Intent(this, FireAlarmHistoryInfoActivity.class);
        intent.putExtra("projectSystemID", query.getProjectSystemID());
        intent.putExtra("hostID", query.getFireHostId());
        intent.putExtra("resource", "1");
        intent.putExtra("positionName", query.getFireHostName());
        intent.putExtra("userID", projectSystemBean.getParentID());

        intent.putExtra("isFromAlarm", true);
        startActivity(intent);
    }

    @Override
    public void fireAlarmDevicePortClick(FireAlarmBuildFloorCountBean query) {

        Intent intent = new Intent(FireAlarmHostActivity.this, ProjectInfoMessageDataActivity.class);

        intent.putExtra("ID", query.getProjectSystemID());

        intent.putExtra("Name", query.getFireHostName());

        intent.putExtra("labelType", 4);

        intent.putExtra("isFromAlarmPort", true);

        startActivity(intent);

    }
}
