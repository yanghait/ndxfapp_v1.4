package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.ProjectSystemListAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.SystemListMessageCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectSystemBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectSystemListPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.ui.GridDividerDecoration;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.WaterCannonListActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 项目下的系统列表
 */
public class ProjectSystemListActivity extends BaseActivity implements IProjectSystemListPersenter.IIProjectSystemListView, ProjectSystemListAdapter.IProjectSystemOnClick {

    //系统列表
    private RecyclerView rvList;

    //系统列表获取
    private IProjectSystemListPersenter persenter;

    //项目对象
    private ProjectNodeBean projectNodeBean;

    private ProgressDialog dialog;

    private List<ProjectSystemBean> systemBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_project_system_list);
        super.onCreate(savedInstanceState);
        setBarTitle("系统列表");
        Intent intent = getIntent();
        Object queryObj = intent.getSerializableExtra("data");
        if (queryObj == null) {
            HelperView.Toast(this, "未选择项目！");
            finish();
            return;
        }
        projectNodeBean = (ProjectNodeBean) queryObj;
        persenter = NodeBasePersenterFactory.getProjectSystemListPersenterImpl(this);
        addPersenter(persenter);
        rvList = findViewById(R.id.rv_list);
        // 设置RecyclerView 网格布局
        GridLayoutManager manager = new GridLayoutManager(this, 2);

        GridDividerDecoration dividerDecoration = new GridDividerDecoration(2, getResources().getDimensionPixelSize(R.dimen.margin_top_bottom), true);

        rvList.addItemDecoration(dividerDecoration);

        rvList.setLayoutManager(manager);
        //
        dialog = showProgress(this, "加载中...", false);

        persenter.doProjectSystemList(projectNodeBean.getID());
        // 历史报警入口
        TextView historicalAlarm = findViewById(R.id.historical_title);
        historicalAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectSystemListActivity.this, ProjectHistoryAlarmActivity.class);
                intent.putExtra("data", projectNodeBean);
                startActivity(intent);
            }
        });

        //initData();
    }

    private void initData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("userName", HelperTool.getUsername());
        params.put("proID", projectNodeBean.getID());
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE + URLConstant.URL_PROJECT_SYS_LIST)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                        com.blankj.utilcode.util.LogUtils.aTag("输出系统列表9999---", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        com.blankj.utilcode.util.LogUtils.aTag("输出系统列表1212---", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

//                            String str = jsonObject.getString("extra");
//
//                            JSONArray jsonArray = new JSONArray(str);

                            //LogUtils.showLoge("输出系统列表extra23456---", jsonArray.toString());

                            Object object = jsonObject.get("extra");

                            JSONArray jsonArray = null;
                            if (object instanceof JSONArray) {
                                jsonArray = (JSONArray) object;
                            } else if (object instanceof String) {
                                jsonArray = new JSONArray((String) object);
                            }
                            if (jsonArray != null) {
                                List<SystemListMessageCountBean> messageCountBeans = new Gson().fromJson(jsonArray.toString(),
                                        new TypeToken<List<SystemListMessageCountBean>>() {
                                        }.getType());

                                List<ProjectSystemBean> systemList = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                        new TypeToken<List<ProjectSystemBean>>() {
                                        }.getType());
                                if (messageCountBeans == null || messageCountBeans.size() == 0 || systemList == null || systemList.size() == 0) {
                                    return;
                                }

                                systemBeanList.clear();
                                systemBeanList.addAll(systemList);
                                ProjectSystemListAdapter adapter = new ProjectSystemListAdapter(systemBeanList, messageCountBeans, ProjectSystemListActivity.this);
                                rvList.setAdapter(adapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    //请求系统列表回掉
    @Override
    public void callBackProjectSystemList(ResultBean<List<ProjectSystemBean>, String> result) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        try {
            if (result.isSuccess()) {
                //LogUtils.showLoge("result123---",result.getExtra());

                JSONArray jsonArray = new JSONArray(result.getExtra());
                List<SystemListMessageCountBean> messageCountBeans = new Gson().fromJson(jsonArray.toString(),
                        new TypeToken<List<SystemListMessageCountBean>>() {
                        }.getType());
                if (messageCountBeans == null || messageCountBeans.size() == 0) {
                    return;
                }

                systemBeanList.clear();
                systemBeanList.addAll(result.getData());
                ProjectSystemListAdapter adapter = new ProjectSystemListAdapter(systemBeanList, messageCountBeans, this);
                rvList.setAdapter(adapter);


//                List<SystemListMessageCountBean> messageCountBeans = new Gson().fromJson(result.getExtra(),
//                        new TypeToken<List<SystemListMessageCountBean>>() {
//                        }.getType());
//                if (messageCountBeans == null || messageCountBeans.size() == 0) {
//                    return;
//                }

                //LogUtils.showLoge("输出系统列表消息数量1212---", String.valueOf(result.getExtra().size()));

//                if (result.getExtra().size() > 0) {
//                    LogUtils.showLoge("输出系统列表消息数量1212---", String.valueOf(result.getExtra().get(0).getID()));
//                }
            }
        } catch (Exception e) {
            HelperView.Toast(this, "系统列表失败:" + e.getMessage());
        }

    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String action) {
        super.callBackError(resultBean, action);
        //ToastUtils.showLong("标记5050---"+resultBean.getMessage());
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    //系统列表点击回掉
    @Override
    public void ProjectSystemOnClick(ProjectSystemBean projectSystemBean) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (!StringUtils.isEmpty(projectSystemBean.getProjectSystemTypeID()) &&
                "11".equals(projectSystemBean.getProjectSystemTypeID())) {
            Intent intent = new Intent(this, FireAlarmHostActivity.class);
            intent.putExtra("data", projectSystemBean);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, EquipmentLabelActivity.class);
            intent.putExtra("data", projectSystemBean);
            startActivity(intent);
        }
    }

    @Override
    public void ProjectSystemOnRealDataClick(ProjectSystemBean projectSystemBean) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        Intent intent = new Intent(ProjectSystemListActivity.this, ProjectRealAlarmActivity.class);
        intent.putExtra("data", projectNodeBean);
        intent.putExtra("isFromRealData", true);
        intent.putExtra("ProSysID", projectSystemBean.getID());
        startActivity(intent);
    }
}
