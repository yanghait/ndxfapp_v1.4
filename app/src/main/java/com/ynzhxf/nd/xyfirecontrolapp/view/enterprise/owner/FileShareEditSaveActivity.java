package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareFileTypeBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.DialogUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class FileShareEditSaveActivity extends BaseActivity {

    public MaterialSpinner spinner;

    public String ID;

    public String typeId;

    private String fileId;

    public EditText mTitle;

    public EditText mRemark;

    public CheckBox checkBox1;
    public CheckBox checkBox2;
    public CheckBox checkBox3;

    private int selectPosition;

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_file_share_edit);
        super.onCreate(savedInstanceState);
        setBarTitle("编辑文件");

        mTitle = findViewById(R.id.file_edit_title);

        mRemark = findViewById(R.id.file_edit_remark);

        checkBox1 = findViewById(R.id.file_edit_check1);
        checkBox2 = findViewById(R.id.file_edit_check2);
        checkBox3 = findViewById(R.id.file_edit_check3);

        spinner = findViewById(R.id.file_edit_spinner_list);

        ID = getIntent().getStringExtra("id");
        fileId = getIntent().getStringExtra("fileId");
        typeId = getIntent().getStringExtra("typeId");
        title = getIntent().getStringExtra("title");

        if (!StringUtils.isEmpty(title)) {
            mTitle.setText(title);
        }

        Button fileSave = findViewById(R.id.file_edit_save);
        fileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSaveData();
            }
        });
        initCreateFile();
        // 初始化界面数据
        initData();
    }

    public void initCreateFile() {
    }

    private List<String> getListString(List<FileShareFileTypeBean> listBeans) {
        List<String> data = new LinkedList<>();
        for (int i = 0; i < listBeans.size(); i++) {
            data.add(listBeans.get(i).getF_Name());
            if (typeId == null) {
                selectPosition = 0;

            } else if (typeId.equals(listBeans.get(i).getID())) {
                selectPosition = i;
            }
        }
        return data;
    }

    public void initData() {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", ID);
        params.put("ID", fileId);
        final ProgressDialog dialog = showProgress(this, "加载中...", false);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_FILE_UPDATE_INFO))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(FileShareEditSaveActivity.this, e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("文件更新界面信息0909---", response);
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject json = jsonObject.getJSONObject("data");
                            checkBox1.setChecked(json.getBoolean("isCompet"));
                            checkBox2.setChecked(json.getBoolean("isEnterprise"));
                            checkBox3.setChecked(json.getBoolean("isCompany"));

                            final List<FileShareFileTypeBean> listBeans = new Gson().fromJson(json.getJSONArray("lsFileType").toString(),
                                    new TypeToken<List<FileShareFileTypeBean>>() {
                                    }.getType());

                            spinner.setItems(getListString(listBeans));

                            spinner.setSelectedIndex(selectPosition);

                            spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                                    typeId = listBeans.get(position).getID();
                                }
                            });
                        } catch (Exception e) {
                            HelperView.Toast(FileShareEditSaveActivity.this, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void initSaveData() {
        if (StringUtils.isEmpty(mTitle.getText().toString().trim())) {
            HelperView.Toast(this, "标题不能为空!");
            return;
        }
        if ((!checkBox1.isChecked()) && (!checkBox2.isChecked()) && (!checkBox3.isChecked())) {
            HelperView.Toast(this, "至少指定一类用户可见!");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", ID);
        params.put("ID", fileId);
        params.put("typeId", typeId);
        params.put("title", mTitle.getText().toString().trim());
        params.put("remark", mRemark.getText().toString().trim());

        params.put("isCompet", String.valueOf(checkBox1.isChecked()));
        params.put("isEnterprise", String.valueOf(checkBox2.isChecked()));
        params.put("isCompany", String.valueOf(checkBox3.isChecked()));

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_FILE_SHARE_EDIT_SAVE))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.showLoge("文件分享文件编辑保存1111---", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("文件分享文件编辑保存0909---", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            boolean isOk = object.getBoolean("success");
                            if (isOk) {
                                DialogUtil.showErrorMessage(FileShareEditSaveActivity.this, "编辑更新文件成功!", new DialogUtil.IComfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        finish();
                                    }
                                });
                            } else {
                                HelperView.Toast(FileShareEditSaveActivity.this, "保存失败!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(FileShareEditSaveActivity.this, e.getMessage());
                        }
                    }
                });
    }
}
