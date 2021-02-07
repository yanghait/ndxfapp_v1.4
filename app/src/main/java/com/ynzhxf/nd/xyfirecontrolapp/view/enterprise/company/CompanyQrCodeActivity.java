package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.company.InspectionCompanyResultSaveActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import okhttp3.Call;

public class CompanyQrCodeActivity extends BaseActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;
    private static String ID;
    private static String projectId;

    double latitude;
    double longitude;


    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    private IGetLocationCallBack callBack;

    private static boolean flag = false;

    private double mLat = 0.0d;
    private double mLng = 0.0d;

    private double mRadius = 0.0d;
    private String mResult;

    private ProgressDialog dialog;

    private boolean isFromCompanyInspect = false;

    private String itemId;
    private String taskId;
    private String systemName;
    private String systemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_company_qr_code);
        super.onCreate(savedInstanceState);
        setBarTitle("扫码进场");
        FrameLayout content = findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        content.addView(mScannerView);
        ID = getIntent().getStringExtra("ID");
        projectId = getIntent().getStringExtra("projectId");
        flag = getIntent().getBooleanExtra("flag", false);
        isFromCompanyInspect = getIntent().getBooleanExtra("isFromCompanyInspect", false);
        if (isFromCompanyInspect) {
            setBarTitle("扫描巡检二维码");
            itemId = getIntent().getStringExtra("itemId");
            taskId = getIntent().getStringExtra("taskId");
            systemName = getIntent().getStringExtra("systemName");
            systemId = getIntent().getStringExtra("systemId");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initBaiduLocation();

        mScannerView.setResultHandler(this);

        mScannerView.startCamera();
    }

    private static ProgressDialog showProgressDigOne(Activity activity, String title, String message, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(cancelable);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    @Override
    public void handleResult(final Result result) {
        //Toast.makeText(this, "Contents = " + result.getContents() +
        // ", Format = " + result.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();
        //LogUtils.showLoge("扫描结果1213---", result.getContents() + "~~~~" + projectId);

        if (StringUtils.isEmpty(result.getContents()) || StringUtils.isEmpty(projectId) ||
                !projectId.equals(result.getContents())) {
            ToastUtils.showLong("项目与二维码不匹配!");
            finish();
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", projectId);
        dialog = showProgressDigOne(this, "提示", "加载中...", false);

        String url;
        if (isFromCompanyInspect) {
            url = URLConstant.URL_BASE1 + URLConstant.URL_COMPANY_INSPECTION_GET_GPS;
        } else {
            url = URLConstant.URL_BASE1 + URLConstant.URL_COMPANY_GET_GPS;
        }
        OkHttpUtils.post()
                .url(url)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                        HelperView.Toast(CompanyQrCodeActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("获取GPS范围1213---", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getBoolean("success")) {
                                JSONObject object = json.getJSONObject("data");
                                double Lat1 = object.getDouble("Lat");
                                double Lng1 = object.getDouble("Lng");
                                final double Radius = object.getDouble("Radius");
                                final double Lat = Radians(Lat1);
                                final double Lng = Radians(Lng1);

                                mLat = Lat;
                                mLng = Lng;
                                mRadius = Radius;
                                mResult = result.getContents();
                            } else {
                                dialog.dismiss();
                                ToastUtils.showLong(json.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            HelperView.Toast(CompanyQrCodeActivity.this, e.getMessage());
                        }
                    }
                });
    }

    public interface IGetLocationCallBack {
        void onResult();
    }

    private void setCallBack(IGetLocationCallBack callBack) {
        this.callBack = callBack;
    }

    private void initGoToMaintain(double Lat, double Lng, double Radius, String result) {
        if ((int) latitude != 0 && (int) longitude != 0) {
            latitude = Radians(latitude);
            longitude = Radians(longitude);
            double s = Math.acos(Math.cos(latitude) * Math.cos(Lat) * Math.cos(longitude - Lng) + Math.sin(latitude) * Math.sin(Lat));
            s = s * 6378137.0;
            s = Math.round(s * 10000) / 10000;

            //LogUtils.showLoge("输出相差距离3456---", String.valueOf(s));
            if (s < Radius + 100.0) {
//                Intent intent = new Intent(CompanyQrCodeActivity.this, CompanyQrCodeMaintainActivity.class);
//                intent.putExtra("id", ID);
//                intent.putExtra("projectId", result);
//                intent.putExtra("isShow", true);
//                intent.putExtra("flag", flag);
//                startActivity(intent);

                if (!isFromCompanyInspect) {
                    goToCompanyQrCodeMaintain(result, true);
                } else {
                    goToCompanyInspectResultSave(result);
                }

            } else {
//                Intent intent = new Intent(CompanyQrCodeActivity.this, CompanyQrCodeMaintainActivity.class);
//                intent.putExtra("id", ID);
//                intent.putExtra("projectId", result);
//                intent.putExtra("isShow", false);
//                intent.putExtra("flag", flag);
//                startActivity(intent);
                if (!isFromCompanyInspect) {
                    goToCompanyQrCodeMaintain(result, false);
                } else {
                    ToastUtils.showLong("此任务不在巡检范围内!");
                }
            }
        } else {
            HelperView.Toast(CompanyQrCodeActivity.this, "未获取到位置信息,请稍后再试!");
        }
    }

    private void goToCompanyQrCodeMaintain(String result, boolean isShow) {
        Intent intent = new Intent(CompanyQrCodeActivity.this, CompanyQrCodeMaintainActivity.class);
        intent.putExtra("id", ID);
        intent.putExtra("projectId", result);
        intent.putExtra("isShow", isShow);
        intent.putExtra("flag", flag);
        startActivity(intent);
        finish();
    }

    private void goToCompanyInspectResultSave(String result) {
        //ToastUtils.showLong("此任务在巡检范围内!");
        Intent intent = new Intent(CompanyQrCodeActivity.this, InspectionCompanyResultSaveActivity.class);
        intent.putExtra("itemId", itemId);
        intent.putExtra("projectId", result);
        intent.putExtra("taskId", taskId);
        intent.putExtra("isFromCompanyInspect", true);
        intent.putExtra("systemName", systemName);
        intent.putExtra("systemId", systemId);
        startActivity(intent);

    }

    private void initBaiduLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数

        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(1000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setIsNeedAltitude(false);

        option.setLocationNotify(false);

        mLocationClient.setLocOption(option);

        mLocationClient.start();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            latitude = location.getLatitude();    //获取纬度信息
            longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明

            LogUtils.showLoge("输出获取到的位置信息2018---", String.valueOf(latitude) + "---" + String.valueOf(longitude) + "---" + errorCode);
            //initGoToMaintain(Radians(25.0719525), Radians(102.6635975), 53.80845, null);
            if (mLat != 0.0d && mLng != 0.0d && mRadius != 0.0d && !StringUtils.isEmpty(mResult) && (errorCode == 61 || errorCode == 161)) {

                dialog.dismiss();
                initGoToMaintain(mLat, mLng, mRadius, mResult);

                mLat = 0.0d;
                mLng = 0.0d;
                mRadius = 0.0d;
                mResult = "";

                mLocationClient.unRegisterLocationListener(myListener);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.unRegisterLocationListener(myListener);
    }

    private static double Radians(double s) {
        return (s * Math.PI) / 180.0;
    }

}
