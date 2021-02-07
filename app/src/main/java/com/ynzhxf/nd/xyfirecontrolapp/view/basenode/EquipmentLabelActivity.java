package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.EquipmentLabelAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.TreeGridBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.LabelNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectSystemBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IEquipmentLabelPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ILabelWriteValuePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.ui.RotateImage;
import com.ynzhxf.nd.xyfirecontrolapp.ui.WaterImageAnimation;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备标签实时数据
 */
public class EquipmentLabelActivity extends BaseActivity implements IEquipmentLabelPersenter.IEquipmentLabelView, ILabelWriteValuePersenter.ILabelWriteValueView, EquipmentLabelAdapter.ILableInfoClick, EquipmentLabelAdapter.ILabelControlClick {

    private IEquipmentLabelPersenter persenter;


    private ILabelWriteValuePersenter labelWriteValuePersenter;

    //线程是否暂停
    private boolean isRun = true;

    //是否处于暂停状态
    private boolean isPause = false;

    //系统对象
    private ProjectSystemBean systemBean;

    //距离上一次请求的时间
    private int lastRequestCount = 0;
    //集合列表
    private List<LabelNodeBean> labelList;


    private RecyclerView rev;

    private TextView txtDescribe;

    private EquipmentLabelAdapter adapter;
    //是否是首次加载
    private boolean isFirst = true;


    //密码输入弹窗
    private AlertDialog alertDialogPwd;

    //密码输入错误消息提示
    private TextView txtError;

    //密码输入框
    private EditText etxtPwd;
    //取消操作按钮
    private Button btnCancelOperation;
    //确定操作按钮
    private Button btnSureOperation;
    private TextView txtOperationTitle;

    //缓存正确的操作密码
    private String cacheSurePwd = "";

    //当前选中的标签缓存
    private LabelNodeBean SelectLabelBean;

    private boolean isResetData = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_equipment_label);
        super.onCreate(savedInstanceState);
        setBarTitle("实时数据");
        WaterImageAnimation.start();
        RotateImage.start();
        Intent intent = getIntent();
        Object queryObj = intent.getSerializableExtra("data");
        if (queryObj == null) {
            HelperView.Toast(this, "未发现系统");
            return;
        }
        systemBean = (ProjectSystemBean) queryObj;
        persenter = NodeBasePersenterFactory.getEquipmentLabelPersenterImpl(this);
        addPersenter(persenter);
        labelWriteValuePersenter = NodeBasePersenterFactory.getLabelWriteValuePersenterImpl(this);
        addPersenter(labelWriteValuePersenter);

        txtDescribe = findViewById(R.id.txt_describe);
        rev = findViewById(R.id.rv_list);

        //初始化操作密码输入框
        View vPwd = getLayoutInflater().inflate(R.layout.alert_option_pwd, null);
        alertDialogPwd = new AlertDialog.Builder(this).setTitle("").setView(vPwd).create();
        btnSureOperation = vPwd.findViewById(R.id.btn_sure);
        btnCancelOperation = vPwd.findViewById(R.id.btn_cancel);
        txtError = vPwd.findViewById(R.id.txt_error);
        etxtPwd = vPwd.findViewById(R.id.etxt_option_pwd);
        txtOperationTitle = vPwd.findViewById(R.id.txt_title);

        //设置标签列表
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rev.setLayoutManager(manager);
        init();//初始化事件
        persenter.doRequestEquipmentLabel(systemBean.getID());
    }

    private void init() {
        //设置标签列表滚动事件
        rev.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                try {
                    View stickview = recyclerView.findChildViewUnder(0, 0);
                    if (stickview != null) {
                        Object queryObj = stickview.getTag();
                        if (queryObj != null) {
                            LabelNodeBean queryBean = (LabelNodeBean) queryObj;
                            String tempName = queryBean.getName();
                            if (queryBean.getNodeLevel() != 6) {
                                queryBean = adapter.getLabelBeanByID(queryBean.getParentID());
                                if (queryBean != null) tempName = queryBean.getName();
                            }
                            txtDescribe.setText(tempName);
                        }
                    }
                } catch (Exception e) {

                }
            }
        });

        //注册确认密码按钮点击事件
        btnSureOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queyrPwd = etxtPwd.getText().toString();
                if (queyrPwd == null || queyrPwd.length() < 6) {
                    txtError.setText("请输入正确的密码！");
                    return;
                }
                txtError.setText("");
                showProgressDig(false, "操作中，请稍后.....");
                labelWriteValuePersenter.doLabelWriteValue(SelectLabelBean.getID(), queyrPwd);

            }
        });
        //注册取消按钮点击事件
        btnCancelOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogPwd.hide();
            }
        });
    }


    @Override
    public void callBackError(ResultBean<String, String> resultBean, String code) {
        super.callBackError(resultBean, code);
    }

    /**
     * 设备标签实时数据请求完成回调
     *
     * @param result
     */
    @Override
    public void callBackEquipmentLabel(ResultBean<List<TreeGridBean<LabelNodeBean>>, String> result) {
        try {
            if (result.isSuccess()) {
                labelList = analysisResponse(result.getData());
                if (isFirst) {
                    isFirst = false;
                    labelList = analysisResponse(result.getData());
                    adapter = new EquipmentLabelAdapter(this, labelList, this, this);
                    rev.setAdapter(adapter);

                    /*LogUtils.eTag("输出第一个标签9090---", labelList.get(0).getTagValueType() + "~~~~" + labelList.size()+"~~~"+
                            labelList.get(1).getTagValueType().getID()+"~~~"+labelList.get(1).getTagValueType().getName());*/
                } else {
                    adapter.loadMap(labelList);
                    adapter.notifyDataSetChanged();
                }
                if (labelList.size() > 0) {
                    hideNoDataView();
                    txtDescribe.setVisibility(View.VISIBLE);
                } else {
                    showNoDataView();
                    txtDescribe.setVisibility(View.GONE);
                }
            } else {
                HelperView.Toast(this, result.getMessage());
            }
        } catch (Exception e) {

        }
        new UpdataThread().start();

        //new UpdateThread().start();

    }


    /**
     * 请求成功，解析请求返回的数据
     *
     * @param datas
     */
    public List<LabelNodeBean> analysisResponse(List<TreeGridBean<LabelNodeBean>> datas) {
        List<LabelNodeBean> queryList = new ArrayList<>();
        for (TreeGridBean<LabelNodeBean> query : datas) {
            queryList.add(query.node);
            for (TreeGridBean<LabelNodeBean> query1 : query.getChildren()) {
                queryList.add(query1.node);
            }
        }
        return queryList;
    }

    /**
     * 点击查看标签历史记录回调
     *
     * @param labelNodeBean
     */
    @Override
    public void LableInfoClick(LabelNodeBean labelNodeBean) {
        Intent intent = new Intent(this, LabelInfoRecordActivity.class);
        intent.putExtra("data", labelNodeBean);
        startActivity(intent);
    }

    /**
     * 点击控制开关按钮回调
     *
     * @param labelNodeBean
     */
    @Override
    public void LabelControlClick(LabelNodeBean labelNodeBean) {
        txtOperationTitle.setText(labelNodeBean.getName());
        SelectLabelBean = labelNodeBean;
        showAlertDialogPwd();
    }

    /**
     * 显示密码输入窗体
     */
    private void showAlertDialogPwd() {
        if (cacheSurePwd.length() > 0) {
            etxtPwd.setText(cacheSurePwd);
        } else {
            etxtPwd.setText("");
        }
        etxtPwd.setFocusable(true);
        alertDialogPwd.show();
    }

    /**
     * 对控制标签的写值操作数据请求回调
     *
     * @param result
     */
    @Override
    public void callBackLabelWriteValue(ResultBean<String, String> result) {
        hideProgressDig();
        if (result.isSuccess()) {//请求成功！
            this.alertDialogPwd.hide();
            this.cacheSurePwd = etxtPwd.getText().toString();
        } else {
            this.txtError.setText(result.getMessage());
        }
    }

    private class UpdataThread extends Thread {
        @Override
        public void run() {
            try {
                if (isRun) {
                    while (lastRequestCount < GloblePlantformDatas.realDataUpdateTime) {
                        if (!isPause) {
                            lastRequestCount += 300;
                        }
                        Thread.sleep(300);
                    }
                    lastRequestCount = 0;
                    Log.e(TAG, "实时数据请求");
                    persenter.doRequestEquipmentLabel(systemBean.getID());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private class UpdateThread extends Thread {
        @Override
        public void run() {
            try {
                if (isRun) {
//                    while (lastRequestCount < 2) {
//                        if (!isPause) {
//                            lastRequestCount += 300;
//                        }
//                        Thread.sleep(300);
//                    }

                    Thread.sleep(2000);

                    //lastRequestCount = 0;

                    Log.e(TAG, "实时数据请求0520");
                    //persenter.doRequestEquipmentLabel(systemBean.getID());

                    List<LabelNodeBean> data = adapter.getListData();

                    if (data == null || data.size() == 0) {
                        return;
                    }

                    for (int i = 0; i < data.size(); i++) {
                        if (!StringUtils.isEmpty(data.get(i).getNowAlarmType())) {
                            int state = Integer.parseInt(data.get(i).getNowAlarmType());
                            LabelNodeBean bean = data.get(i);
                            if (state > 0 && bean.getColorState() == 0) {
                                bean.setColorState(1);
                            } else if (state > 0 && bean.getColorState() == 1) {
                                bean.setColorState(0);
                            }
                            adapter.notifyDataSetChanged();
//                            if (state > 0 && isResetData) {
//                                isResetData = false;
//                                LabelNodeBean bean = data.get(i);
//                                bean.setColorState(1);
//                                adapter.notifyDataSetChanged();
//                            } else if (state > 0 && !isResetData) {
//                                isResetData = true;
//                                LabelNodeBean bean = data.get(i);
//                                bean.setColorState(0);
//                                adapter.notifyDataSetChanged();
//                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRun = false;
        alertDialogPwd.dismiss();
        WaterImageAnimation.stop();
        RotateImage.stop();
    }
}
