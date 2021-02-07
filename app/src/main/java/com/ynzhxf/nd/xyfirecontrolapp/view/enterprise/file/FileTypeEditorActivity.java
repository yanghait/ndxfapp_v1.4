package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.StringUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.DialogUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 文件分类更新和添加分类
 */
@EnableDragToClose
public class FileTypeEditorActivity extends BaseActivity {

    private String projectId;

    private String typeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.file_type_editor);
        super.onCreate(savedInstanceState);

        projectId = getIntent().getStringExtra("projectId");

        typeId = getIntent().getStringExtra("typeId");

        final int optionId = getIntent().getIntExtra("optionId", 1);
        if (optionId == 1) {
            setBarTitle("新增文件分类");
        } else {
            setBarTitle("编辑文件分类");
        }

        final EditText title = findViewById(R.id.file_type_editor_title);

        final EditText remark = findViewById(R.id.file_type_editor_remark);

        Button confirm = findViewById(R.id.file_type_editor_btn);

        title.setText(getIntent().getStringExtra("title"));

        remark.setText(getIntent().getStringExtra("remark"));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initConfirm(title.getText().toString().trim(), remark.getText().toString().trim(), optionId);
            }
        });
    }

    private void initConfirm(String title, String remark, final int optionId) {
        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(remark)) {
            HelperView.Toast(this, "请输入备注或标题!");
            return;
        }

        Map<String, String> params = new HashMap<>();

        String url;

        if (optionId == 1) {
            params.put("Token", HelperTool.getToken());
            params.put("projectId", projectId);
            params.put("name", title);
            params.put("remark", remark);
            url = URLConstant.URL_BASE1.concat(URLConstant.URL_FILE_TYPE_ADD);
        } else {
            params.put("Token", HelperTool.getToken());
            params.put("projectId", projectId);
            params.put("ID", typeId);
            params.put("name", title);
            params.put("remark", remark);
            url = URLConstant.URL_BASE1.concat(URLConstant.URL_FILE_TYPE_UPDATE);
        }

        OkHttpUtils.post()
                .url(url)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.showLoge("文件分类文件编辑结果1111---", e.getMessage());
                        HelperView.Toast(FileTypeEditorActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("文件分类文件编辑结果0909---", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean isOk = json.getBoolean("success");

                            String content;
                            if (isOk) {
                                if (optionId == 1) {
                                    content = "新增文件分类成功!";
                                } else {
                                    content = "更新文件分类成功!";
                                }
                            } else {
                                content = json.getString("message");
                            }
                            DialogUtil.showDialogMessage(FileTypeEditorActivity.this, content, new DialogUtil.IComfirmListener() {
                                @Override
                                public void onConfirm() {
                                    finish();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(FileTypeEditorActivity.this, e.getMessage());
                        }
                    }
                });

    }
}
