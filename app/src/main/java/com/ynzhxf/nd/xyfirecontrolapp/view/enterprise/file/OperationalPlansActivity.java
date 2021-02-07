package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.share.FileShareMyFileAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.share.OperationalPlanAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.OperationalPlanFileBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.FileOutUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.utils.FileUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class OperationalPlansActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    private TextView projectName;

    private TextView title;

    private TextView uploadPerson;

    private TextView time;

    private String content;

    private LinearLayout mAttachment;

    private RelativeLayout mPlanContent;

    private boolean isHasPlans = false;

    private ImageView mNoDataView;

    private TextView mNoDataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_operational_plans);
        super.onCreate(savedInstanceState);
        setBarTitle("作战预案");

        mNoDataView = findViewById(R.id.no_data_img);
        mNoDataText = findViewById(R.id.no_data_text);

        projectName = findViewById(R.id.operational_plan_project_name);
        title = findViewById(R.id.operational_plan_title);
        uploadPerson = findViewById(R.id.operational_plan_person);
        time = findViewById(R.id.operational_plan_time);

        mPlanContent = findViewById(R.id.operational_plan_content);

        mAttachment = findViewById(R.id.attachment);

        mPlanContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isEmpty(content)) {
                    return;
                }
                Intent intent = new Intent(OperationalPlansActivity.this, OperationalPlanDetail.class);
                intent.putExtra("planDetail", content);
                startActivity(intent);
            }
        });

        mRecyclerView = findViewById(R.id.operational_plan_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        DividerItemDecoration div = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        div.setDrawable(getResources().getDrawable(R.drawable.divider_shape_for_file));
//        mRecyclerView.addItemDecoration(div);
        initOperationalData(getIntent().getStringExtra("projectId"));
    }

    private void initOperationalData(String projectId) {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", projectId);
        final ProgressDialog dialog = showProgress(this, "加载中...", false);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_OPERATIONAL_PLANS))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(OperationalPlansActivity.this, e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();

                        LogUtils.showLoge("输出预案9090---",response);

                        try {
                            JSONObject json = new JSONObject(response);
                            if (!json.getBoolean("success")) {

                                mAttachment.setVisibility(View.GONE);

                                projectName.setVisibility(View.GONE);

                                mPlanContent.setVisibility(View.GONE);

                                showNoDataView();
                                //HelperView.Toast(OperationalPlansActivity.this, json.getString("message"));
                                showNoDataView();
                                return;
                            } else {
                                hideNoDataView();
                            }
                            JSONObject object = json.getJSONObject("data");
                            JSONObject titleJson = object.getJSONObject("modOperationPlan");

                            uploadPerson.setText(String.valueOf("上传人:" + titleJson.getString("F_Creator_Name")));

                            projectName.setText(titleJson.getString("F_ProjectName"));

                            title.setText(titleJson.getString("F_OperationalPlanName"));

                            time.setText(titleJson.getString("F_CreateTime_Name"));

                            content = titleJson.getString("F_OperationalPlanContent");

                            if (!StringUtils.isEmpty(titleJson.getString("F_ProjectName"))) {
                                isHasPlans = true;
                            }

                            final List<OperationalPlanFileBean> beans = new Gson().fromJson(object.getJSONArray("lsFile").toString(),
                                    new TypeToken<List<OperationalPlanFileBean>>() {
                                    }.getType());

                            if (beans != null && beans.size() > 0) {

                                final OperationalPlanAdapter adapter = new OperationalPlanAdapter(null, OperationalPlansActivity.this,
                                        new FileShareMyFileAdapter.OnCheckBoxChangeListener() {
                                            @Override
                                            public void initAdapter(int position) {

                                            }

                                            @Override
                                            public void onCheckedChanged(CompoundButton compoundButton, boolean checked, int position) {

                                            }
                                        }, beans);

                                adapter.setOnItemClickListener(new FileShareMyFileAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                                        final String newPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + beans.get(position).getF_FileName();

                                        boolean exists = FileShareMyFileAdapter.whetherExistFile(FileDownloadUtils.getDefaultSaveRootPath(), beans.get(position).getF_FileName());
                                        if (exists) {
//                                            if (Build.VERSION.SDK_INT >= 28) {
//                                                openFileForThirdApp(newPath, beans.get(position).getF_FileName());
//                                            } else {
//                                                openFileForSdk(newPath);
//                                            }
                                            FileOutUtil.openFileReader(OperationalPlansActivity.this,newPath);
                                        } else {
                                            HelperView.Toast(OperationalPlansActivity.this, "文件未下载或已删除!");
                                        }
                                    }

                                    @Override
                                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                        return false;
                                    }
                                });
                                mRecyclerView.setAdapter(adapter);
                            } else {
                                if (!isHasPlans) {
                                    mAttachment.setVisibility(View.GONE);

                                    projectName.setVisibility(View.GONE);

                                    mPlanContent.setVisibility(View.GONE);

                                    showNoDataView();
                                } else {
                                    showNoDataView();
                                    mNoDataView.setVisibility(View.INVISIBLE);
                                    mNoDataText.setText("暂无附件!");
                                }
                            }

                        } catch (Exception e) {

                            mAttachment.setVisibility(View.GONE);

                            projectName.setVisibility(View.GONE);

                            mPlanContent.setVisibility(View.GONE);

                            showNoDataView();

                            e.printStackTrace();
                        }
                    }
                });
    }

    public void openFileForSdk(String newPath) {
        HashMap<String, String> params = new HashMap<>();

        params.put("local", "false");

        QbSdk.openFileReader(OperationalPlansActivity.this, newPath, params, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                LogUtils.showLogI("OnOpenFile---", s);
            }
        });
    }

    public void openFileForThirdApp(String newPath, String name) {
        Uri uri;
        // 兼容7.0及以上版本获取URI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(OperationalPlansActivity.this, "com.ynzhxf.nd.firecontrolapp.fileprovider", new File(newPath));
        } else {
            uri = Uri.fromFile(new File(newPath));
        }
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, getMIMEType(name));//getMIMEType(myFileBeanList.get(position).getF_FileName())
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(OperationalPlansActivity.this, "sorry附件不能打开，请下载相关软件！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * --获取文件类型 --
     */
    public static String getMIMEType(String filePath) {
        String type = "*/*";
        String fName = filePath;

        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }

        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") {
            return type;
        }

        for (int i = 0; i < FileUtils.MIME_MapTable.length; i++) {
            if (end.equals(FileUtils.MIME_MapTable[i][0])) {
                type = FileUtils.MIME_MapTable[i][1];
            }
        }
        return type;
    }
}
