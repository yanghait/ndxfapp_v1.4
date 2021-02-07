package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.company;

import android.app.ProgressDialog;
import android.content.Intent;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionResultSaveActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


/**
 * author hbzhou
 * date 2019/1/24 10:47
 */
public class InspectionCompanyResultSaveActivity extends InspectionResultSaveActivity {

    private double latitude;
    private double longitude;

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    private double mLat = 0.0d;
    private double mLng = 0.0d;

    private double mRadius = 0.0d;
    private String mResult;

    private ProgressDialog dialog;

    private String systemId;
    private String systemName;

    @Override
    protected void initTextData(String title, final String remark) {

        systemId = getIntent().getStringExtra("systemId");

        systemName = getIntent().getStringExtra("systemName");

        initBaiduLocation();

        initLocationData();

        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("itemId", itemId);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_COMPANY_INSPECTION_RESULT_GET_DATA))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.aTag(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                JSONObject json = jsonObject.getJSONObject("data");

                                mRemark.setText(json.getString("ItemRemark"));

                                mTitleName.setText(json.getString("ItemName"));

                            } else {
                                ToastUtils.showLong(jsonObject.getString("message"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private static double Radians(double s) {
        return (s * Math.PI) / 180.0;
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

            LogUtils.eTag("输出获取到的当前位置信息2018---", String.valueOf(latitude) + "---" + String.valueOf(longitude) + "---" + errorCode);
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
    protected void initUploadImage() {

    }

    private void initGoToMaintain(double Lat, double Lng, double Radius, String result) {
        if ((int) latitude != 0 && (int) longitude != 0) {
            latitude = Radians(latitude);
            longitude = Radians(longitude);
            double s = Math.acos(Math.cos(latitude) * Math.cos(Lat) * Math.cos(longitude - Lng) + Math.sin(latitude) * Math.sin(Lat));
            s = s * 6378137.0;
            s = Math.round(s * 10000) / 10000.0;

            if (s < Radius + 100.0) {

                //LogUtils.eTag("当前巡检项在巡检范围内-----"+s);
                super.initUploadImage();
            } else {

                saveData.setBackground(getResources().getDrawable(R.drawable.btn_maintain_bac));
                saveData.setClickable(false);
                saveData.setText("请在有效范围内巡检");
                ToastUtils.showLong("此任务不在巡检范围内!");

                //LogUtils.eTag("当前巡检项不在巡检范围内-----");
            }
        } else {
            HelperView.Toast(InspectionCompanyResultSaveActivity.this, "未获取到位置信息,请稍后再试!");
        }
    }

    private void initLocationData() {

        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", projectId);

        dialog = showProgress(this, "提示", "加载中...", false);

        String url = URLConstant.URL_BASE1 + URLConstant.URL_COMPANY_INSPECTION_GET_GPS;

        OkHttpUtils.post()
                .url(url)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                        HelperView.Toast(InspectionCompanyResultSaveActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.eTag("获取GPS范围1213---", response);
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
                                mResult = projectId;
                            } else {
                                dialog.dismiss();
                                ToastUtils.showLong(json.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            HelperView.Toast(InspectionCompanyResultSaveActivity.this, e.getMessage());
                        }
                    }
                });

    }

    @Override
    protected void goToResultInspectItem() {

        SPUtils.getInstance().put("updateHomeItem", true);

        HelperView.Toast(InspectionCompanyResultSaveActivity.this, "已成功提交!");

        Intent intent = new Intent(InspectionCompanyResultSaveActivity.this, InspectionCompanyItemActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("selectedPosition", 1);

        startActivity(intent);

        finish();

    }

    @Override
    protected void initSaveParamsForCompany(Map<String, String> params) {

        if (!StringUtils.isEmpty(systemId) && !StringUtils.isEmpty(systemName)) {
            params.put("systemTypeId", systemId);
            params.put("systemTypeName", systemName);
        }
    }
}
