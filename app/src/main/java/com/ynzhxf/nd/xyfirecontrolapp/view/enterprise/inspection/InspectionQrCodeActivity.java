package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionItemListBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import okhttp3.Call;

/**
 * 巡检扫码
 */
public class InspectionQrCodeActivity extends BaseActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;

    private InspectionItemListBean inspectionItemListBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_company_qr_code);
        super.onCreate(savedInstanceState);
        setBarTitle("扫描二维码");
        FrameLayout content = findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        content.addView(mScannerView);
        if (getIntent().hasExtra("inspectionItem"))
            inspectionItemListBean = (InspectionItemListBean) getIntent().getSerializableExtra("inspectionItem");
    }

    @Override
    public void handleResult(Result result) {
        if (null == inspectionItemListBean) {
            Intent backintent = getIntent();
            backintent.putExtra("qrscan", result.getContents());
            setResult(RESULT_OK, backintent);
            finish();
        } else
            initInspectionQrResult(result);
    }

    private void initInspectionQrResult(final Result result) {
        if (inspectionItemListBean.getQrCode() != null) {
            if (!inspectionItemListBean.getQrCode().equals(result.getContents())) {
                ToastUtils.showLong("二维码与巡检项不匹配!");
                finish();
                return;
            }
        }else{
        if (!inspectionItemListBean.getID().equals(result.getContents())) {
            ToastUtils.showLong("二维码与巡检项不匹配!");
            finish();
            return;
        }
        }


        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("taskId", inspectionItemListBean.getTaskId());
        params.put("itemId", inspectionItemListBean.getID());
        params.put("qrCode", result.getContents());
        final ProgressDialog progressDialog = showProgress(this, "加载中...", false);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_INSPECTION_QR_CODE_VERIFY)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        progressDialog.dismiss();
                        HelperView.Toast(InspectionQrCodeActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        progressDialog.dismiss();
                        LogUtils.showLoge("扫码校验结果1212---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                Intent intent = getIntent();
                                intent.putExtra("qrscan", result.getContents());
                                setResult(RESULT_OK, intent);
                                finish();
//                                Intent intent = new Intent(InspectionQrCodeActivity.this, InspectionResultSaveActivity.class);
//                                intent.putExtra("taskId", taskId);
//                                intent.putExtra("Name", Name);
//                                intent.putExtra("Remark", Remark);
//                                intent.putExtra("AreaId", AreaId);
//                                intent.putExtra("projectId", projectId);
//                                intent.putExtra("itemId", result.getContents());
//                                startActivity(intent);

                            } else {
                                finish();
                                HelperView.Toast(InspectionQrCodeActivity.this, jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(InspectionQrCodeActivity.this, e.getMessage());
                        }
                    }
                });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();


    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }
}
