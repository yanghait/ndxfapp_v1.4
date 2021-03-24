package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
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

    Spinner region_spinner, fcSys_spinner;

    TextView reg_NFCCode_txt;

    Button reg_qrcode_btn, local_btn;

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
        fcSys_spinner = this.findViewById(R.id.fcSys_spinner);

        reg_NFCCode_txt = this.findViewById(R.id.reg_NFCCode_txt);

        reg_qrcode_btn = this.findViewById(R.id.reg_qrcode_btn);
        local_btn = this.findViewById(R.id.local_btn);
    }

    private void initListener() {

        reg_NFCCode_txt.setOnClickListener(v -> {
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
                                    startActivityForResult(intent, QRSCAN_CODE);
                                    break;
                                case 1:
                                    intent = new Intent(RegDeviceCodeActivity.this, NFCReadActivity.class);
                                    startActivityForResult(intent, NFC_CODE);
                                    break;
                            }
                        }
                    }).create();
            dialog.show();
        });

        reg_qrcode_btn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(reg_NFCCode_txt.getText())) {
                HelperView.Toast(RegDeviceCodeActivity.this, "请先扫描二维码或NFC");
            } else {
                bindCheckPointCode(reg_NFCCode_txt.getText().toString());
            }
        });
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

    //巡检点选项
    private void updatePointSpinner(List<CheckPointBean> checkPointBeans) {
        List<String> ponitNameList = new ArrayList<>();
        for (CheckPointBean checkPointBean : checkPointBeans) {
            ponitNameList.add(checkPointBean.getName());
        }
        ArrayAdapter<String> sysAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ponitNameList);
        sysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fcSys_spinner.setAdapter(sysAdapter);
        fcSys_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                checkPonitId = checkPointBeans.get(i).getID();
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
                                updatePointSpinner(checkPointBeans);
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
    private void bindCheckPointCode(String qrCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("inspectItemId", checkPonitId);
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
                                reg_NFCCode_txt.setText("");
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
                reg_NFCCode_txt.setText(data.getStringExtra("qrscan"));
            } else if (requestCode == NFC_CODE) {
                reg_NFCCode_txt.setText(data.getStringExtra("NFCread"));
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
    }
}