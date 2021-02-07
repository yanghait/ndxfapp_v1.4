package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.FireAlarmBuildAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.FireAlarmFloorAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmBuildFloorCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmHostBuildCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmHostBuildInfoCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmHostFloorCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IFireAlarmBuildListPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 火灾报警建筑物列表
 */
public class FireAlarmHostBuildActivity extends BaseActivity implements FireAlarmBuildAdapter.IFireAlarmBuildClick,IFireAlarmBuildListPersenter.IFireAlarmBuildListView , FireAlarmFloorAdapter.IFireAlarmFloorClick{

    //建筑物显示列表
    private RecyclerView rvBuild;

    //楼层空白列表容器
    private  View lyWhiteCon;
    //楼层显示列表
    private RecyclerView rvFloor;
    //弹出楼层空白处的遮罩
    private View lyWhite;
    //选择的建筑名称
    private TextView txtBuildName;

    //火灾报警信息
    private FireAlarmBuildFloorCountBean dataBean;

    private List<FireAlarmBuildFloorCountBean> dataList;

    private FireAlarmBuildAdapter buildAdapter;

    private FireAlarmHostBuildInfoCountBean buildDatas;

    private IFireAlarmBuildListPersenter persenter;

    private Map<String , List<FireAlarmBuildFloorCountBean>> floorDatas;

    private FireAlarmFloorAdapter floorAdapter;

    private int[] floorColors;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fire_alarm_host_build);
        super.onCreate(savedInstanceState);
        setBarTitle("建筑物列表");
        Intent intent = getIntent();
        Object queryObj = intent.getSerializableExtra("data");
        if(queryObj == null){
            HelperView.Toast(this , "火灾报警主机没有找到！");
            return;
        }
        dataBean = (FireAlarmBuildFloorCountBean)queryObj;
        rvBuild = findViewById(R.id.rv_huild_list);
        lyWhiteCon = findViewById(R.id.ly_white_con);
        rvFloor = findViewById(R.id.rv_floor_list);
        lyWhite = findViewById(R.id.ly_white);
        txtBuildName = findViewById(R.id.txt_build_name);
        dataList = new ArrayList<>();
        floorDatas = new HashMap<>();
        persenter = NodeBasePersenterFactory.getFireAlarmBuildListPersenterImpl(this);
        addPersenter(persenter);
        initView();
        persenter.doFireAlarmBuildList(dataBean.getProjectSystemID() , dataBean.getFireHostId());
        //showProgressDig(false);
        dialog = showProgress(this,"加载中...",false);
        floorColors = new int[3];
        floorColors[0] = getResources().getColor(R.color.fire_fire); //掉线时候的颜色
        floorColors[1] = getResources().getColor(R.color.black);//正常颜色
        floorColors[2] = getResources().getColor(R.color.fire_fire);//异常时候的颜色

    }

    private void initView(){
        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.VERTICAL);
        rvBuild.setLayoutManager(manager1);

        LinearLayoutManager manager2 = new LinearLayoutManager(this);
        manager2.setOrientation(LinearLayoutManager.VERTICAL);
        rvFloor.setLayoutManager(manager2);

        lyWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyWhiteCon.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void fireAlarmBuildClick(FireAlarmBuildFloorCountBean query) {
        List<FireAlarmBuildFloorCountBean> tempList = floorDatas.get(query.getBuildName());
        if(tempList != null && tempList.size() >0){
            txtBuildName.setText(query.getBuildName());
            floorAdapter = new FireAlarmFloorAdapter(tempList ,this );
            rvFloor.setAdapter(floorAdapter);
            lyWhiteCon.setVisibility(View.VISIBLE);
        }else {
            HelperView.Toast(this ,"无楼层信息");
        }
    }

    /**
     * 楼层列表点击
     * @param query
     */
    @Override
    public void fireAlarmFloorClick(FireAlarmBuildFloorCountBean query) {
        Intent intent = new Intent(this , FireAlarmPointListActivity.class);
        intent.putExtra("data" , query);
        startActivity(intent);
    }

    /**
     * 完成火灾报警数据请求回调
     * @param result
     */
    @Override
    public void callBackFireAlarmBuildList(ResultBean<FireAlarmHostBuildInfoCountBean, String> result) {
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        try{
            if(result.isSuccess()){
                FireAlarmHostBuildInfoCountBean datas = result.getData();
                buildDatas = datas;
                for(int i=0 ; i < datas.getBuildList().size();i++) {
                    FireAlarmHostBuildCountBean queryBuild1 = datas.getBuildList().get(i);
                    FireAlarmBuildFloorCountBean queryBuild = new FireAlarmBuildFloorCountBean();
                    queryBuild.setProjectSystemID(dataBean.getProjectSystemID());
                    queryBuild.setFireHostId(dataBean.getFireHostId());
                    queryBuild.setBuildName(queryBuild1.getBuildName());
                    queryBuild.setFloorCount(queryBuild1.getFloorCountInfoList().size());

                    List<FireAlarmBuildFloorCountBean> buildFloors = new ArrayList<>();

                    for (int j = 0; j < queryBuild1.getFloorCountInfoList().size(); j++) {
                        FireAlarmHostFloorCountBean floor = queryBuild1.getFloorCountInfoList().get(j);
                        queryBuild.setEquipentCount(queryBuild.getEquipentCount() + floor.getInfoCount().getEquipentCount());
                        queryBuild.setNormalCount(queryBuild.getNormalCount() + floor.getInfoCount().getNormalCount());
                        queryBuild.setActionCount(queryBuild.getActionCount() + floor.getInfoCount().getActionCount());
                        queryBuild.setFaultCount(queryBuild.getFaultCount() + floor.getInfoCount().getFaultCount());
                        queryBuild.setCloseCount(queryBuild.getCloseCount() + floor.getInfoCount().getCloseCount());
                        queryBuild.setManageCount(queryBuild.getManageCount() + floor.getInfoCount().getManageCount());
                        queryBuild.setOpenCount(queryBuild.getOpenCount() + floor.getInfoCount().getOpenCount());
                        queryBuild.setFeedCount(queryBuild.getFeedCount() + floor.getInfoCount().getFeedCount());
                        queryBuild.setFireCount(queryBuild.getFireCount() + floor.getInfoCount().getFireCount());
                        queryBuild.setOutLineCount(queryBuild.getOutLineCount() + floor.getInfoCount().getOutLineCount());

                        FireAlarmBuildFloorCountBean temp2 = new FireAlarmBuildFloorCountBean();
                        temp2.setFloor(floor.getFloot());
                        temp2.setProjectSystemID(dataBean.getProjectSystemID());
                        temp2.setFireHostId(dataBean.getFireHostId());
                        temp2.setBuildName(queryBuild1.getBuildName());
                        if(floor.getInfoCount().getOutLineCount() == floor.getInfoCount().getEquipentCount()){
                            temp2.setFloorCount(floorColors[0]);//临时保存颜色
                        }else if(floor.getInfoCount().getNormalCount() == floor.getInfoCount().getEquipentCount()){
                            temp2.setFloorCount(floorColors[1]);
                        }else{
                            temp2.setFloorCount(floorColors[2]);
                        }
                        buildFloors.add(temp2);
                    }
                    dataList.add(queryBuild);
                    floorDatas.put(queryBuild1.getBuildName(),buildFloors);
                }
                buildAdapter = new FireAlarmBuildAdapter(dataList, this);
                rvBuild.setAdapter(buildAdapter);
            }else {
                buildDatas = null;
                HelperView.Toast(this , result.getMessage());
            }
        }catch (Exception e){
            HelperView.Toast(this , "建筑信息失败:"+result.getMessage());
        }

       // hideProgressDig();
    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String code) {
        super.callBackError(resultBean, code);
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
