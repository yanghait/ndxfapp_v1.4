package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareFileTypeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareFileTypeInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.share.IFileShareFileTypePresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.share.impl.FileSharePresenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.util.DialogUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.FileShareEditSaveActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.utils.FileUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 文件分享新增文件
 */
public class FileShareCreateFileActivity extends FileShareEditSaveActivity implements IFileShareFileTypePresenter.IFileShareFileTypeView {
    RelativeLayout selectFileLayout;
    LinearLayout showSize;

    private Button mSelectBtn;

    private TextView mFileSize;

    private String projectId;

    private String fileId = "";

    private String fileName;

    public IFileShareFileTypePresenter presenter;

    private int selectPosition = 0;

    @Override
    public void initCreateFile() {
        selectFileLayout = findViewById(R.id.file_add_file_layout);
        selectFileLayout.setVisibility(View.VISIBLE);
        showSize = findViewById(R.id.file_add_file_show);

        mSelectBtn = findViewById(R.id.file_create_select);
        mFileSize = findViewById(R.id.file_create_file_size);

        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 101);
            }
        });

        setBarTitle("新增文件");

        projectId = getIntent().getStringExtra("id");
    }

    @Override
    public void initData() {
        // 请求文件分类列表
        presenter = FileSharePresenterFactory.getFileShareFileTypePresenterImpl(this);
        FileShareFileTypeInputBean inputBean = new FileShareFileTypeInputBean();
        inputBean.setToken(HelperTool.getToken());
        inputBean.setProjectId(projectId);
        presenter.doFileShareFileType(inputBean);
    }

    private List<String> getListString(List<FileShareFileTypeBean> listBeans) {
        List<String> data = new LinkedList<>();
        for (int i = 0; i < listBeans.size(); i++) {
            data.add(listBeans.get(i).getF_Name());
            if (typeId == null) {
                selectPosition = 0;
                typeId = listBeans.get(0).getID();

            } else if (typeId.equals(listBeans.get(i).getID())) {
                selectPosition = i;
            }
        }
        return data;
    }

    @Override
    public void callBackFileShareFileType(final ResultBean<List<FileShareFileTypeBean>, String> resultBean) {
        if (resultBean == null || resultBean.getData().size() == 0) {
            return;
        }

        spinner.setItems(getListString(resultBean.getData()));

        spinner.setSelectedIndex(selectPosition);

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                typeId = resultBean.getData().get(position).getID();
            }
        });
    }

    @Override
    public void initSaveData() {
        if (StringUtils.isEmpty(mTitle.getText().toString().trim())) {
            HelperView.Toast(this, "标题不能为空!");
            return;
        }
        if ((!checkBox1.isChecked()) && (!checkBox2.isChecked()) && (!checkBox3.isChecked())) {
            HelperView.Toast(this, "至少指定一类用户可见!");
            return;
        }
        String remark = mRemark.getText().toString().trim();
        if (StringUtils.isEmpty(remark)) {
            remark = "";
        }
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", projectId);
        params.put("ID", fileId);
        params.put("typeId", typeId);
        params.put("title", mTitle.getText().toString().trim());
        params.put("remark", remark);

        params.put("isCompet", String.valueOf(checkBox1.isChecked()));
        params.put("isEnterprise", String.valueOf(checkBox2.isChecked()));
        params.put("isCompany", String.valueOf(checkBox3.isChecked()));

        if (StringUtils.isEmpty(fileName)) {
            HelperView.Toast(this, "请先添加上传文件！");
            return;
        }
        params.put("fileName", fileName);

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_FILE_SHARE_EDIT_SAVE))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(FileShareCreateFileActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("文件分享文件编辑保存0909---", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            boolean isOk = object.getBoolean("success");
                            if (isOk) {
                                DialogUtil.showErrorMessage(FileShareCreateFileActivity.this, "新增文件成功!", new DialogUtil.IComfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        finish();
                                    }
                                });
                            } else {
                                HelperView.Toast(FileShareCreateFileActivity.this, "保存失败!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(FileShareCreateFileActivity.this, e.getMessage());
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 101:
                    if (data.getData() != null) {
                        String path = FileUtils.getPath(this, data.getData());
                        if (path != null) {
                            File file = new File(path);
                            if (file.exists()) {
                                uploadFile(file);
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void uploadFile(final File file) {
        final ProgressDialog dialog = showProgress(this, "上传中...", false);
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", projectId);

        OkHttpUtils.post()
                .addFile("fileName", file.getName(), file)
                .params(params)
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_FILE_SHARE_UPLOAD_FILE))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                        HelperView.Toast(FileShareCreateFileActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        try {
                            JSONObject json = new JSONObject(response);

                            boolean isOk = json.getBoolean("success");

                            HelperView.Toast(FileShareCreateFileActivity.this, json.getString("message"));
                            if (isOk) {

                                showSize.setVisibility(View.VISIBLE);

                                mFileSize.setText(String.valueOf(file.length() / 1024 + "KB"));

                                fileName = json.getString("data");

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(FileShareCreateFileActivity.this, e.getMessage());
                        }
                    }
                });
    }


}
