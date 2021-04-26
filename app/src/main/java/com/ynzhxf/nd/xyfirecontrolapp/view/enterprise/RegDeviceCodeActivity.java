package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.CheckInfoAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.CheckPointBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionAreaListBackBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.NFCReadActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionQrCodeActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class RegDeviceCodeActivity extends BaseActivity {

    Spinner region_spinner;

    RecyclerView checkInfo_recycler;

    CheckInfoAdapter checkInfoAdapter;

    String projectId = "", regionId = "", checkPonitId = "";

    private static final int NFC_CODE = 1001, QRSCAN_CODE = 1002;

    int select = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_reg_device_code);
        super.onCreate(savedInstanceState);
        setBarTitle("设备二维码注册");
        projectId = getIntent().getStringExtra("projectId");
        getRegionInfo();
        initLayout();
        initListener();
    }


    private void initLayout() {
        region_spinner = this.findViewById(R.id.region_spinner);
        checkInfo_recycler = this.findViewById(R.id.checkInfo_recycler);
        checkInfoAdapter = new CheckInfoAdapter(this);
        checkInfo_recycler.setLayoutManager(new LinearLayoutManager(this));
        checkInfo_recycler.setAdapter(checkInfoAdapter);

    }

    private void initListener() {
        checkInfoAdapter.setOnStateBtnClickListener((position, checkPointInfo) -> {
            select = 0;
            final String[] items = {"二维码", "NFC"};
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("选择巡检方式")
                    .setSingleChoiceItems(items, select, (dlg, which) -> {
                        select = which;
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = null;
                            switch (select) {
                                case 0:
                                    intent = new Intent(RegDeviceCodeActivity.this, InspectionQrCodeActivity.class);
                                    intent.putExtra("index", position);
                                    intent.putExtra("checkPoint", checkPointInfo);
                                    startActivityForResult(intent, QRSCAN_CODE);
                                    break;
                                case 1:
                                    intent = new Intent(RegDeviceCodeActivity.this, NFCReadActivity.class);
                                    intent.putExtra("index", position);
                                    intent.putExtra("checkPoint", checkPointInfo);
                                    startActivityForResult(intent, NFC_CODE);
                                    break;
                            }
                        }
                    }).create();
            dialog.show();
        });

//        reg_qrcode_btn.setOnClickListener(v -> {
//            if (TextUtils.isEmpty(reg_NFCCode_txt.getText())) {
//                HelperView.Toast(RegDeviceCodeActivity.this, "请先扫描二维码或NFC");
//            } else {
//                bindCheckPointCode(reg_NFCCode_txt.getText().toString());
//            }
//        });
    }


    //巡检区域选项
    private void updateSpinner(List<InspectionAreaListBackBean> inspectionAreaListBackBeans) {
        List<String> sysNameList = new ArrayList<>();
        for (InspectionAreaListBackBean areaListBackBean : inspectionAreaListBackBeans) {
            sysNameList.add(areaListBackBean.getName());
        }
        ArrayAdapter<String> sysAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sysNameList);
        sysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        region_spinner.setAdapter(sysAdapter);
        region_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                regionId = inspectionAreaListBackBeans.get(i).getID();
                getCheckPoition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    //获取巡检区域
    private void getRegionInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", projectId);
        final ProgressDialog progressDialog = showProgress(this, "加载中...", false);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_INSPECTION_AREA_LIST)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        progressDialog.dismiss();
                        HelperView.Toast(RegDeviceCodeActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                List<InspectionAreaListBackBean> areaListBackBeans = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<InspectionAreaListBackBean>>() {
                                }.getType());
                                updateSpinner(areaListBackBeans);
                            } else {
                                HelperView.Toast(RegDeviceCodeActivity.this, jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(RegDeviceCodeActivity.this, e.getMessage());
                        }
                    }
                });
    }

    //获取巡检区域下的巡检点
    private void getCheckPoition() {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("inspectAreaId", regionId);
        final ProgressDialog progressDialog = showProgress(this, "加载中...", false);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_INSPECTION_AREA_POINT_LIST)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        progressDialog.dismiss();
                        HelperView.Toast(RegDeviceCodeActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                List<CheckPointBean> checkPointBeans = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<CheckPointBean>>() {
                                }.getType());
                                checkInfoAdapter.update(checkPointBeans);
                            } else {
                                HelperView.Toast(RegDeviceCodeActivity.this, jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(RegDeviceCodeActivity.this, e.getMessage());
                        }
                    }
                });
    }

    //绑定巡检点标签
    private void bindCheckPointCode(CheckPointBean checkPointBean, int index, String qrCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("inspectItemId", checkPointBean.getID());
        params.put("qrCode", qrCode);
        final ProgressDialog progressDialog = showProgress(this, "加载中...", false);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_INSPECTION_POINT_BINDIND)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        progressDialog.dismiss();
                        HelperView.Toast(RegDeviceCodeActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                HelperView.Toast(RegDeviceCodeActivity.this, "注册成功!");
                                checkPointBean.setQrCode(qrCode);
                                checkInfoAdapter.updateItem(index, checkPointBean);
                            } else {
                                HelperView.Toast(RegDeviceCodeActivity.this, jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(RegDeviceCodeActivity.this, e.getMessage());
                        }
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == QRSCAN_CODE) {
                CheckPointBean bean = (CheckPointBean) data.getSerializableExtra("checkPoint");
                int index = data.getIntExtra("index", 0);
                String qrCode = data.getStringExtra("qrscan");
                bindCheckPointCode(bean, index, qrCode);
            } else if (requestCode == NFC_CODE) {
                CheckPointBean bean = (CheckPointBean) data.getSerializableExtra("checkPoint");
                int index = data.getIntExtra("index", 0);
                String nfcCode = data.getStringExtra("NFCread");
                bindCheckPointCode(bean, index, nfcCode);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
    }
}