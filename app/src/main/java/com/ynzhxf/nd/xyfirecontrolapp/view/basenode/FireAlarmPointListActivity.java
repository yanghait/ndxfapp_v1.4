package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.FireAlarmPointAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmBuildFloorCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmPointBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IFireAlarmPointListPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 火灾报警楼层探头点详细信息
 */
public class FireAlarmPointListActivity extends BaseActivity implements IFireAlarmPointListPersenter.IFireAlarmPointListView ,FireAlarmPointAdapter.IFireAlarmPointClick{

    private IFireAlarmPointListPersenter persenter;
    //探头点列表
    private RecyclerView recyclerView;

    private FireAlarmPointAdapter adapter ;

    //位置名称
    private TextView txtName;

    private TextView txtTotalCount;

    private FireAlarmBuildFloorCountBean dataBean;

    private Drawable[] colors;

    private View popuRoot;

    private PopupWindow popupWindow;
    private TextView txtFullName;
    private TextView txtState;

    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fire_alarm_point_list);
        super.onCreate(savedInstanceState);
        setBarTitle("楼层探头列表");
        Object queryObj = getIntent().getSerializableExtra("data");
        if(queryObj == null){
            HelperView.Toast(this , "未找到相关信息");
            return;
        }
        //showProgressDig(false);
        dialog = showProgress(this,"加载中...",false);
        colors = new Drawable[]{
                getResources().getDrawable(R.drawable.fire_point_normal),
                getResources().getDrawable(R.drawable.fire_point_action),
                getResources().getDrawable(R.drawable.fire_point_fault),
                getResources().getDrawable(R.drawable.fire_point_close),
                getResources().getDrawable(R.drawable.fire_point_manage),
                getResources().getDrawable(R.drawable.fire_point_open),
                getResources().getDrawable(R.drawable.fire_point_feed),
                getResources().getDrawable(R.drawable.fire_point_fire),
                getResources().getDrawable(R.drawable.fire_point_outline)
        };
        dataBean = (FireAlarmBuildFloorCountBean)queryObj;
        txtName = findViewById(R.id.txt_name);
        txtTotalCount = findViewById(R.id.txt_total_count);
        recyclerView = findViewById(R.id.rv_list);
        popuRoot = getLayoutInflater().inflate(R.layout.popu_fire_point , null);
        popupWindow = new PopupWindow(popuRoot, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT ,true);
        txtFullName = popuRoot.findViewById(R.id.node_name);
        txtState = popuRoot.findViewById((R.id.node_discribe));
        persenter = NodeBasePersenterFactory.getFireAlarmPointListPersenter(this);
        addPersenter(persenter);
        initView();
        persenter.doFireAlarmPointList(dataBean.getProjectSystemID() , dataBean.getFireHostId() , dataBean.getBuildName() ,dataBean.getFloor());

        //initPointData();

        //initAlarmHistoryInfo();
    }

    private void initAlarmHistoryInfo() {

//        if (StringUtils.isEmpty(hostID) || StringUtils.isEmpty(projectSystemID) || StringUtils.isEmpty(userID)) {
//            ToastUtils.showLong("未获取到探头信息!");
//            return;
//        }
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("ProjectSystemID", dataBean.getProjectSystemID());
        params.put("FireHostId", dataBean.getFireHostId());
        params.put("BuildName", dataBean.getBuildName());
        params.put("floor", String.valueOf(dataBean.getFloor()));

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE+URLConstant.URL_FIRE_ALRM_FLOOR_POINT_INFO)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showLong(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("火灾探头信息返回返回1212---", response);
                    }
                });
    }

    private void initView(){
        txtName.setText(dataBean.getBuildName()+dataBean.getFloor()+"层");
        GridLayoutManager manager = new GridLayoutManager(this , 9);
        recyclerView.setLayoutManager(manager);
        popuRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                popupWindow.dismiss();
            }
        });
    }


    /**
     * 火灾报警楼层探头列表数据回调
     * @param result
     */
    @Override
    public void callBackFireAlarmPointList(ResultBean<List<FireAlarmPointBean>, String> result) {
        //hideProgressDig();
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        try{
            if(result.isSuccess()){
                txtTotalCount.setText("设备总数："+result.getData().size());
                adapter = new FireAlarmPointAdapter( result.getData(),this,colors);
                recyclerView.setAdapter(adapter);

            }else{
                HelperView.Toast(this , result.getMessage());
            }
        }catch (Exception e){
            HelperView.Toast(this , "探头列表失败:"  + result.getMessage());
        }

    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String action) {
        super.callBackError(resultBean, action);
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }

    /**
     * 点击探头点回掉
     * @param query
     */
    @Override
    public void fireAlarmFloorClick(FireAlarmPointBean query , View view) {
        try{
//            txtFullName.setText("位置:" + query.getFullPositionName());
//            txtState.setText("状态:" + query.getTransformState());
//            int windowPos[] = calculatePopWindowPos(view, popuRoot);
            //popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, windowPos[0], windowPos[1]);

            //LogUtils.showLoge("火灾报警hostid1212---",dataBean.getFireHostId());

            Intent intent = new Intent(FireAlarmPointListActivity.this,FireAlarmProbeInfoActivity.class);
            intent.putExtra("projectSystemID",dataBean.getProjectSystemID());
            intent.putExtra("resource", "2");
            intent.putExtra("hostID",dataBean.getFireHostId());
            intent.putExtra("positionName",query.getFullPositionName().concat("-").concat(query.getTransformState()));
            intent.putExtra("userID",query.getUserID());
            startActivity(intent);

        }catch (Exception e){
            HelperView.Toast(this , "探头回调失败:"+e.getMessage());
        }
    }

    private  int[] calculatePopWindowPos(View anchorView, View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        int anchorWidth = anchorView.getWidth();
        // 获取屏幕的高宽
        final int screenHeight = this.getResources().getDisplayMetrics().heightPixels;
        final int screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        boolean isNeedShowLeft = (screenWidth - anchorLoc[0] - anchorWidth < windowWidth);
        if(isNeedShowLeft){
            windowPos[0] = anchorLoc[0] - windowWidth;
        }else{
            windowPos[0] = anchorLoc[0];
        }
        return windowPos;
    }
}
