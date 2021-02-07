package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.blankj.utilcode.util.StringUtils;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.luck.picture.lib.entity.LocalMedia;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.maintenance.OwnerGridUploadImageAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.UploadPhotoNormalBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.CompanyBackFillParamsBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.OwnerOrderDetailsInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.ICompanyBackFillParamsPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.impl.MaintenManagePresenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
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

public class CompanyBackFillActivity extends BaseActivity implements ICompanyBackFillParamsPresenter.ICompanyBackFillParamsView, View.OnClickListener {

    private MaterialSpinner spinner_type;
    private EditText ed_point;
    private MaterialSpinner spinner_method;
    private EditText ed_content;

    private GridView gridView;

    private Button btn_save;
    private Button btn_confirm;

    private ICompanyBackFillParamsPresenter presenter;

    private String typeId;
    private String methodId;

    private OwnerGridUploadImageAdapter adapter;

    private ProgressDialog dialog;

    private String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_company_backfill);
        super.onCreate(savedInstanceState);
        setBarTitle("工单回填");
        spinner_type = findViewById(R.id.type_spinner_list);
        ed_point = findViewById(R.id.ed_point);
        spinner_method = findViewById(R.id.method_spinner_list);
        ed_content = findViewById(R.id.ed_content);
        gridView = findViewById(R.id.grid_view);

        btn_save = findViewById(R.id.btn_order_save);
        btn_confirm = findViewById(R.id.btn_order_confirm);

        OwnerOrderDetailsInputBean bean = new OwnerOrderDetailsInputBean();
        bean.setToken(HelperTool.getToken());
        bean.setWorkOrderId(getIntent().getStringExtra("id"));

        presenter = MaintenManagePresenterFactory.getCompanyBackFillParamsImpl(this);
        presenter.doCompanyBackFillParams(bean);

        btn_save.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);

        initUploadImage();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_order_save:
                initConfirmData(getIntent().getStringExtra("id"), 0);
                break;
            case R.id.btn_order_confirm:
                initConfirmData(getIntent().getStringExtra("id"), 1);
                break;
        }
    }

    private void initUploadImage() {
        List<UploadPhotoNormalBean> imgUrl = new ArrayList<>();
        UploadPhotoNormalBean bean = new UploadPhotoNormalBean();
        bean.setLocalPath("1");
        imgUrl.add(bean);
        adapter = new OwnerGridUploadImageAdapter(imgUrl, this);
        gridView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 10) {
                final List<LocalMedia> selectList = new ArrayList<>();

                LocalMedia localMedia = new LocalMedia();

                localMedia.setPath(data.getStringExtra("path"));

                selectList.add(localMedia);

                final String firstFrame = data.getStringExtra("firstFrame");
                //PictureSelector.obtainMultipleResult(data);

                //LogUtils.showLoge("输出选择上传图片路径---", "007008" + data.getStringExtra("path"));
                if (selectList.size() > 0) {
                    File file = new File(selectList.get(0).getPath());
                    if (file.exists() && !file.isDirectory()) {
                        String newName = HelperTool.createUUID() + file.getName().substring(file.getName().lastIndexOf("."));
                        Map<String, String> params = new HashMap<>();
                        params.put("Token", HelperTool.getToken());
                        params.put("fileName", newName);
                        showProgressDig("提示", "图片上传中...", false);
                        OkHttpUtils.post()
                                .addFile("fileName", file.getName(), file)
                                .params(params)
                                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_OWNER_UPLOAD_IMAGE))
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        hideProgressDig();
                                        HelperView.Toast(CompanyBackFillActivity.this, e.getMessage());
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        hideProgressDig();
                                        //LogUtils.showLoge("上传图片结果1212--", response);
                                        try {
                                            JSONObject json = new JSONObject(response);
                                            boolean isOk = json.getBoolean("success");
                                            if (isOk) {
                                                //imagePath = imagePath + json.getString("data") + ",";
                                                HelperView.Toast(CompanyBackFillActivity.this, "上传完成!");
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
                                            HelperView.Toast(CompanyBackFillActivity.this, e.getMessage());
                                        }
                                    }
                                });
                    }
                }
            }
        }
    }


    private void initConfirmData(String id, final int isSubmit) {
        String point = ed_point.getText().toString().trim();
        String content = ed_content.getText().toString().trim();
        if (StringUtils.isEmpty(point) || StringUtils.isEmpty(content)) {
            HelperView.Toast(this, "故障点或维修内容不能为空!");
            return;
        }
        dialog = showProgress(this, "加载中,请稍后...", false);
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("workOrderId", id);
        params.put("faultTypeId", typeId);
        params.put("faultPlace", point);
        params.put("repairMethodId", methodId);
        params.put("repairContent", content);
        params.put("isSubmit", String.valueOf(isSubmit));

        for (UploadPhotoNormalBean bean : adapter.getImageList()) {
            if (!StringUtils.isEmpty(bean.getUploadPath())) {
                imagePath = imagePath.concat(bean.getUploadPath() + ",");
            }
        }

        if (!StringUtils.isEmpty(imagePath)) {
            params.put("ImagePath", imagePath.substring(0, imagePath.length() - 1));
        }

        //LogUtils.showLoge("打印上传返回的url路径0000----", imagePath.substring(0, imagePath.length() - 1));

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_COMPANY_BACK_FILL_CONFIRM))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(CompanyBackFillActivity.this, e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("获取回填参数4567---", response);
                        dialog.dismiss();
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean isOK = json.getBoolean("success");
                            if (isOK && isSubmit == 0) {
                                HelperView.Toast(CompanyBackFillActivity.this, "保存成功!");
                                //setResult(Activity.RESULT_OK);
                                finish();
                            } else if (isOK && isSubmit == 1) {
                                HelperView.Toast(CompanyBackFillActivity.this, "提交成功!");
                                setResult(Activity.RESULT_OK);
                                finish();
                            } else {
                                HelperView.Toast(CompanyBackFillActivity.this, json.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(CompanyBackFillActivity.this, e.getMessage());
                        }
                    }
                });
    }

    @Override
    public void callBackCompanyBackFillParams(final ResultBean<CompanyBackFillParamsBean, String> resultBean) {
        if (resultBean != null && resultBean.getData() != null && resultBean.getData().getLsFaultTypeParam().size() > 0 && resultBean.getData().getLsFixMethodParam().size() > 0) {

            modWorkOrderBean = resultBean.getData().getModWorkOrder();

            typeId = resultBean.getData().getLsFaultTypeParam().get(0).getID();
            methodId = resultBean.getData().getLsFixMethodParam().get(0).getID();

            spinner_method.setItems(getDataForMethod(resultBean.getData().getLsFixMethodParam()));

            spinner_type.setItems(getDataForType(resultBean.getData().getLsFaultTypeParam()));

            spinner_method.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    methodId = resultBean.getData().getLsFixMethodParam().get(position).getID();
                }
            });

            spinner_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    typeId = resultBean.getData().getLsFaultTypeParam().get(position).getID();
                }
            });

            if (!StringUtils.isEmpty(resultBean.getData().getModWorkOrder().getFaultPlace())) {

                spinner_type.setSelectedIndex(positionType);

                spinner_method.setSelectedIndex(positionContent);

                ed_point.setText(String.valueOf(resultBean.getData().getModWorkOrder().getFaultPlace()));

                ed_content.setText(String.valueOf(resultBean.getData().getModWorkOrder().getRepairContent()));
            }
        }
    }

    private int positionType = 0;

    private int positionContent = 0;

    private CompanyBackFillParamsBean.ModWorkOrderBean modWorkOrderBean = new CompanyBackFillParamsBean.ModWorkOrderBean();

    private List<String> getDataForType(List<CompanyBackFillParamsBean.LsFaultTypeParamBean> list) {
        List<String> listType = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            listType.add(list.get(i).getName());
            if (!StringUtils.isEmpty(modWorkOrderBean.getSpFaultTypeId()) && modWorkOrderBean.getSpFaultTypeId().equals(list.get(i).getTypeId())) {
                positionType = i;
                typeId = String.valueOf(modWorkOrderBean.getSpFaultTypeId());
            }
        }

        return listType;
    }

    private List<String> getDataForMethod(List<CompanyBackFillParamsBean.LsFixMethodParamBean> list) {
        List<String> listType = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            listType.add(list.get(i).getName());
            if (!StringUtils.isEmpty(modWorkOrderBean.getSpRepairMethodId()) && modWorkOrderBean.getSpRepairMethodId().equals(list.get(i).getTypeId())) {
                positionContent = i;
                methodId = String.valueOf(modWorkOrderBean.getSpRepairMethodId());
            }
        }
        return listType;
    }
}
