package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.luck.picture.lib.entity.LocalMedia;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.maintenance.OwnerGridUploadImageAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.UploadPhotoNormalBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 巡检结果保存
 */
public class InspectionResultSaveActivity extends BaseActivity {

    protected TextView mTitleName;

    protected TextView mRemark;


    protected String areaId;

    protected String taskId;

    protected String projectId;

    protected String itemId;

    protected OwnerGridUploadImageAdapter adapter;

    protected GridView mGridView;

    protected String imagePath = "";

    protected MaterialSpinner spinner;

    protected String resultValue = "0";

    protected EditText mContent;

    protected LinearLayout isShowLayout;

    protected boolean isFromCompanyInspect = false;

    protected Button saveData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_inspection_result_save);
        super.onCreate(savedInstanceState);
        setBarTitle("巡检");

        mTitleName = findViewById(R.id.save_title);
        mRemark = findViewById(R.id.save_remark);

        mGridView = findViewById(R.id.inspect_save_grid_view);

        spinner = findViewById(R.id.result_save_spinner_list);

        String title = getIntent().getStringExtra("Name");

        String remark = getIntent().getStringExtra("Remark");

        areaId = getIntent().getStringExtra("AreaId");

        taskId = getIntent().getStringExtra("taskId");

        projectId = getIntent().getStringExtra("projectId");

        itemId = getIntent().getStringExtra("itemId");

        isFromCompanyInspect = getIntent().getBooleanExtra("isFromCompanyInspect", false);

        mContent = findViewById(R.id.save_content);

        isShowLayout = findViewById(R.id.inspect_result_save);

        initTextData(title, remark);

        saveData = findViewById(R.id.inspect_save_confirm);

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSaveData();
            }
        });

        //initSpinner();

        initSpinnerForLayout();

        initUploadImage();
    }

    protected void initTextData(String title, String remark) {
        mTitleName.setText(title);
        mRemark.setText(remark);
    }

    private void initSpinner() {

        List<String> itemList = new ArrayList<>();
        itemList.add("异常");
        itemList.add("正常");
        spinner.setItems(itemList);

        resultValue = "0";

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                resultValue = String.valueOf(position);
            }
        });
    }

    private void initSpinnerForLayout() {
        List<String> itemList = new ArrayList<>();
        itemList.add("正常");
        itemList.add("异常");

        spinner.setItems(itemList);

        resultValue = "1";

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if (position == 1) {
                    resultValue = "0";
                    //isShowLayout.setVisibility(View.VISIBLE);
                } else {
                    resultValue = "1";
                    //isShowLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    protected void initUploadImage() {
        List<UploadPhotoNormalBean> imgUrl = new ArrayList<>();
        UploadPhotoNormalBean bean = new UploadPhotoNormalBean();
        bean.setLocalPath("1");
        imgUrl.add(bean);
        adapter = new OwnerGridUploadImageAdapter(imgUrl, this);
        mGridView.setAdapter(adapter);
    }

    private void initSaveData() {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("taskId", taskId);
        params.put("projectId", projectId);
        if (!StringUtils.isEmpty(areaId)) {
            params.put("areaId", areaId);
        }

        params.put("itemId", itemId);

        params.put("itemName", mTitleName.getText().toString().trim());

        params.put("resultValue", resultValue);

        initSaveParamsForCompany(params);

        String errorContent = mContent.getText().toString().trim();
        if ("0".equals(resultValue) && StringUtils.isEmpty(errorContent)) {
            HelperView.Toast(this, "异常状态必须添加异常说明!");
            return;
        } else if (StringUtils.isEmpty(errorContent)) {
            params.put("remark", "");
        } else {
            params.put("remark", errorContent);
        }

        for (UploadPhotoNormalBean bean : adapter.getImageList()) {
            if (!StringUtils.isEmpty(bean.getUploadPath())) {
                imagePath = imagePath.concat(bean.getUploadPath() + ",");
            }
        }

        if (!StringUtils.isEmpty(imagePath)) {
            params.put("uploadUrls", imagePath.substring(0, imagePath.length() - 1));
        }

        final ProgressDialog progressDialog = showProgress(this, "加载中...", false);

        String url;
        if (isFromCompanyInspect) {
            url = URLConstant.URL_BASE1 + URLConstant.URL_COMPANY_INSPECTION_RESULT_SAVE;
        } else {
            url = URLConstant.URL_BASE1 + URLConstant.URL_INSPECTION_RESULT_SAVE;
        }
        OkHttpUtils.post()
                .url(url)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        progressDialog.dismiss();
                        HelperView.Toast(InspectionResultSaveActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        progressDialog.dismiss();
                        LogUtils.showLoge("巡检结果保存提交结果1212---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {

//                                SPUtils.getInstance().put("updateHomeItem", true);
//
//                                HelperView.Toast(InspectionResultSaveActivity.this, "已成功提交!");
//
//                                Intent intent = new Intent(InspectionResultSaveActivity.this, InspectionItemListActivity.class);
//
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                                startActivity(intent);
//
//                                finish();

                                goToResultInspectItem();
                            } else {
                                HelperView.Toast(InspectionResultSaveActivity.this, jsonObject.getString("message"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    protected void initSaveParamsForCompany(Map<String, String> params) {

    }

    protected void goToResultInspectItem() {
        SPUtils.getInstance().put("updateHomeItem", true);

        HelperView.Toast(InspectionResultSaveActivity.this, "已成功提交!");

        Intent intent = new Intent(InspectionResultSaveActivity.this, InspectionItemListActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 10) {
                //final List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

                final List<LocalMedia> selectList = new ArrayList<>();

                LocalMedia localMedia = new LocalMedia();

                localMedia.setPath(data.getStringExtra("path"));

                selectList.add(localMedia);
                //PictureSelector.obtainMultipleResult(data);

                final String firstFrame = data.getStringExtra("firstFrame");

                if (selectList.size() > 0) {
                    File file = new File(selectList.get(0).getPath());
                    if (file.exists() && !file.isDirectory()) {
                        Map<String, String> params = new HashMap<>();
                        String newName = HelperTool.createUUID() + file.getName().substring(file.getName().lastIndexOf("."));
                        params.put("Token", HelperTool.getToken());
                        params.put("fileName", newName);

                        final ProgressDialog progressDialog = showProgress(this, "加载中...", false);

                        //LogUtils.showLoge("输出巡检上传图片名1111---", newName);

                        OkHttpUtils.post()
                                .addFile("fileName", file.getName(), file)
                                .params(params)
                                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_INSPECTION_UPLOAD_IMAGE))
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        progressDialog.dismiss();
                                        HelperView.Toast(InspectionResultSaveActivity.this, e.getMessage());
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        progressDialog.dismiss();
                                        //LogUtils.showLoge("输出巡检上传图片结果1213---", response);
                                        try {
                                            JSONObject json = new JSONObject(response);
                                            boolean isOk = json.getBoolean("success");
                                            if (isOk) {
                                                //imagePath = imagePath + json.getString("data") + ",";
                                                HelperView.Toast(InspectionResultSaveActivity.this, "上传完成!");

                                                UploadPhotoNormalBean bean = new UploadPhotoNormalBean();
                                                bean.setUploadPath(json.getString("data"));
                                                if (!StringUtils.isEmpty(firstFrame)) {
                                                    bean.setLocalPath(firstFrame);
                                                    adapter.addItemData(bean);
                                                } else {
                                                    bean.setLocalPath(selectList.get(0).getPath());
                                                    adapter.addItemData(bean);
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            HelperView.Toast(InspectionResultSaveActivity.this, e.getMessage());
                                        }
                                    }
                                });
                    }
                }
            }
        }
    }
}
