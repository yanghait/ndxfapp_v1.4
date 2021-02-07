package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionCreateTaskBackBean;
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

import okhttp3.Call;


/**
 * author hbzhou
 * date 2019/1/7 21:36
 */
public class InspectionTypeCreateTaskActivity extends BaseActivity {


    private String taskID;

    public MaterialSpinner spinner_list;

    private String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_create_inspection_tasks);
        super.onCreate(savedInstanceState);

        setBarTitle("创建自定义任务");

        spinner_list = findViewById(R.id.spinner_list);

        projectId = getIntent().getStringExtra("projectId");

        final Button createTask = findViewById(R.id.create_inspect_task);

        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTasks();
            }
        });

        initTaskType();
    }

    private void createTasks() {

        HashMap<String, String> params = new HashMap<>();

        params.put("Token", HelperTool.getToken());

        params.put("projectId", projectId);

        if (StringUtils.isEmpty(taskID)) {
            HelperView.Toast(this, "未获取到任务类型!");
            return;
        }
        params.put("inspectTypeId", taskID);

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_INSPECTION_CREATE_TASK)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(InspectionTypeCreateTaskActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("创建任务2323---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                DialogUtil.showErrorMessage(InspectionTypeCreateTaskActivity.this, "创建成功!", new DialogUtil.IComfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        setResult(Activity.RESULT_OK);
                                        finish();
                                    }
                                });
                            } else {
                                HelperView.Toast(InspectionTypeCreateTaskActivity.this, jsonObject.getString("message"));
                            }

                        } catch (Exception e) {
                            HelperView.Toast(InspectionTypeCreateTaskActivity.this, e.getMessage());
                        }
                    }
                });


    }

    private void initTaskType() {

        HashMap<String, String> params = new HashMap<>();

        params.put("Token", HelperTool.getToken());

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_INSPECTION_GET_TASK_TYPE)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.showLogd("获取自定义任务类型1515---", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("获取自定义任务类型1212---", response);

                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getBoolean("success")) {

                                json.getJSONArray("data");

                                final List<InspectionCreateTaskBackBean> beanList = new Gson().fromJson(json.getJSONArray("data").toString(),
                                        new TypeToken<List<InspectionCreateTaskBackBean>>() {
                                        }.getType());

                                if (beanList == null || beanList.size() == 0) {
                                    HelperView.Toast(InspectionTypeCreateTaskActivity.this, "未获取到任务类型!");
                                    return;
                                }

                                spinner_list.setItems(getListString(beanList));

                                taskID = beanList.get(0).getID();

                                spinner_list.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                                        taskID = beanList.get(position).getID();
                                    }
                                });


                            } else {
                                HelperView.Toast(InspectionTypeCreateTaskActivity.this, json.getString("message"));
                            }
                        } catch (Exception e) {

                        }
                    }
                });
    }

    public List<String> getListString(List<InspectionCreateTaskBackBean> listBeans) {
        List<String> data = new LinkedList<>();
        for (int i = 0; i < listBeans.size(); i++) {
            data.add(listBeans.get(i).getName());
        }
        return data;
    }
}
