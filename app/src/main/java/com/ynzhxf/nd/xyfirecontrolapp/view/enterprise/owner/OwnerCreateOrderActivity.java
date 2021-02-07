package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.luck.picture.lib.entity.LocalMedia;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.maintenance.OwnerGridUploadImageAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.UploadPhotoNormalBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.OwnerImpowerInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.OwnerImpowerListBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IMainOwnerImpowerListPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.impl.MaintenManagePresenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.util.DialogUtil;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class OwnerCreateOrderActivity extends BaseActivity implements IMainOwnerImpowerListPresenter.IMainOwnerImpowerListView {

    public MaterialSpinner spinner_list;

    public EditText ed_content;

    public EditText ed_note;

    private GridView gridView;

    private Button mSave;

    private IMainOwnerImpowerListPresenter presenter;
    public String ID;


    private OwnerGridUploadImageAdapter adapter;

    public String imagePath = "";

    public String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_owner_create_order);
        super.onCreate(savedInstanceState);
        setBarTitle("新建工单");
        spinner_list = findViewById(R.id.spinner_list);

        ed_content = findViewById(R.id.create_content);

        ed_note = findViewById(R.id.create_note);
        gridView = findViewById(R.id.grid_view);

        mSave = findViewById(R.id.order_save);

        projectId = getIntent().getStringExtra("projectId");

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initGetData(projectId, ID);
            }
        });
        presenter = MaintenManagePresenterFactory.getOwnerImpowerListPersenterImpl(this);
        addPersenter(presenter);
        OwnerImpowerInputBean bean = new OwnerImpowerInputBean();
        bean.setToken(HelperTool.getToken());
        bean.setProjectID(getIntent().getStringExtra("projectId"));

        doMainImpowerList(bean);

        initUploadImage();

        initSystemListId();
    }

    public void initSystemListId() {

    }

    public void doMainImpowerList(OwnerImpowerInputBean bean) {
        presenter.doMainOwnerImpowerList(bean);
    }

    private void initGetOrderDetails() {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("workOrderId", getIntent().getStringExtra("ID"));
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_OWNER_GET_ORDER_DETAILS))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(OwnerCreateOrderActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("工单详情1212--", response);
                    }
                });
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
                //PictureSelector.obtainMultipleResult(data);

                final String firstFrame = data.getStringExtra("firstFrame");
                if (selectList.size() > 0) {
                    File file = new File(selectList.get(0).getPath());
                    if (file.exists() && !file.isDirectory()) {
                        Map<String, String> params = new HashMap<>();
                        String newName = HelperTool.createUUID() + file.getName().substring(file.getName().lastIndexOf("."));
                        params.put("Token", HelperTool.getToken());
                        params.put("fileName", newName);
                        //showProgressDig("提示", "上传中...", false);
                        final ProgressDialog progressDialog = showProgress(this,"上传中...",false);
                        //LogUtils.showLoge("输出创建工单上传图片名1111---", newName);
                        OkHttpUtils.post()
                                .addFile("fileName", file.getName(), file)
                                .params(params)
                                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_OWNER_UPLOAD_IMAGE))
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        //hideProgressDig();
                                        progressDialog.dismiss();
                                        HelperView.Toast(OwnerCreateOrderActivity.this, e.getMessage());
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        //hideProgressDig();
                                        progressDialog.dismiss();
                                        // LogUtils.showLoge("输出创建工单上传图片结果1213---", response);
                                        try {
                                            JSONObject json = new JSONObject(response);
                                            boolean isOk = json.getBoolean("success");
                                            if (isOk) {
                                                //imagePath = imagePath + json.getString("data") + ",";
                                                HelperView.Toast(OwnerCreateOrderActivity.this, "上传完成!");

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
                                            HelperView.Toast(OwnerCreateOrderActivity.this, e.getMessage());
                                        }
                                    }
                                });
                    }
                }
            }
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

    private void initGetData(String projectId, final String anthorId) {
        String content = ed_content.getText().toString().trim();
        String note = ed_note.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            HelperView.Toast(OwnerCreateOrderActivity.this, "请填写故障内容描述!");
            return;
        }
        if (StringUtils.isEmpty(anthorId) || StringUtils.isEmpty(HelperTool.getToken())) {
            ToastUtils.showLong("未获取到维保系统，无法创建工单!");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("ProjectSystemAuthorized_Id", anthorId);
        params.put("FaultContent", content);
        params.put("Remark", note);

        for (UploadPhotoNormalBean bean : adapter.getImageList()) {
            if (!StringUtils.isEmpty(bean.getUploadPath())) {
                imagePath = imagePath.concat(bean.getUploadPath() + ",");
            }
        }

        if (!StringUtils.isEmpty(imagePath)) {
            params.put("ImagePath", imagePath.substring(0, imagePath.length() - 1));
        }
        final ProgressDialog progressDialog = showProgress(this,"工单提交中...",false);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_OWNER_COMMIT_ORDER))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        progressDialog.dismiss();
                        HelperView.Toast(OwnerCreateOrderActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("提交创建的工单1331---", response + "~~~~" + imagePath.substring(0, imagePath.length() - 1));
                        progressDialog.dismiss();
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean isOk = json.getBoolean("success");
                            if (isOk) {
                                HelperView.Toast(OwnerCreateOrderActivity.this, "工单创建成功!");
                                DialogUtil.showDialogMessage(OwnerCreateOrderActivity.this, "工单创建成功!", new DialogUtil.IComfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        setResult(Activity.RESULT_OK);
                                        finish();
                                    }
                                });
                            } else {
                                HelperView.Toast(OwnerCreateOrderActivity.this, "工单创建失败,请稍后再试!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(OwnerCreateOrderActivity.this, e.getMessage());
                        }
                    }
                });
    }

    @Override
    public void callBackMainOwnerImpowerList(final ResultBean<List<OwnerImpowerListBean>, String> resultBean) {
        if (resultBean.getData() == null || resultBean.getData().size() == 0) {
            return;
        }
        spinner_list.setItems(getListString(resultBean.getData()));

        ID = resultBean.getData().get(0).getID();
        spinner_list.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                ID = resultBean.getData().get(position).getID();
            }
        });
    }

    public List<String> getListString(List<OwnerImpowerListBean> listBeans) {
        List<String> data = new LinkedList<>();
        for (int i = 0; i < listBeans.size(); i++) {
            data.add(listBeans.get(i).getProjectSystemName());
        }
        return data;
    }
}
