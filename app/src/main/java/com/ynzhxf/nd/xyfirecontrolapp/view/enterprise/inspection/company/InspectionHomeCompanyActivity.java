package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.company;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.CompanyInspectionTaskTypeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectStateNumBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionTaskHomeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionTaskHomeInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.inspection.impl.InspectionTaskPresenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.tool.utils.ResponseResultCallBack;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionRecordsActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionTaskHomeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;


/**
 * author hbzhou
 * date 2019/1/22 09:13
 * 维保公司巡检首页
 */
public class InspectionHomeCompanyActivity extends InspectionTaskHomeActivity {

    protected List<CheckBox> checkBoxList = new ArrayList<>();

    protected List<CompanyInspectionTaskTypeBean> typeBeanList = new ArrayList<>();

    protected LinearLayout mLayout2;

    protected LinearLayout mLayout3;

    @Override
    protected void initTabLayout() {
        super.initTabLayout();

        mTaskThree.setVisibility(View.GONE);
    }

    @Override
    protected void setSelected(String state) {
        inputBean.setCompany(true);
        super.setSelected(state);
    }

    @Override
    protected void doRequestData() {

        presenter = InspectionTaskPresenterFactory.getTaskInspectionHomePresenterImpl(this);

        inputBean = new InspectionTaskHomeInputBean();

        inputBean.setToken(HelperTool.getToken());
        inputBean.setState(selectedState);
        inputBean.setProjectId(projectId);
        inputBean.setPageSize("20");
        inputBean.setCompany(true);
        currentPosition = 1;
        inputBean.setPageIndex(String.valueOf(currentPosition));
        inputBean.setStartTime(HelperTool.MillTimeToStringDate(startLongTime));
        inputBean.setEndTime(HelperTool.MillTimeToStringDate(endLongTime));

        presenter.doTaskInspectionHomeList(inputBean);
    }

    @Override
    protected void initTaskType(final CheckBox typeBox1, final CheckBox typeBox2) {

        HashMap<String, String> params = new HashMap<>();

        params.put("Token", HelperTool.getToken());

        final ProgressDialog dialog = showProgress(this, "加载中,请稍后...", false);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_COMPANY_INSPECTION_TYPE_LIST)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        com.blankj.utilcode.util.LogUtils.aTag(e.getMessage());
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        LogUtils.showLoge("输出请求返回id101----",String.valueOf(id));
//                        LogUtils.showLoge("获取任务类型1212---", response);
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getBoolean("success")) {

                                final List<CompanyInspectionTaskTypeBean> beanList = new Gson().fromJson(json.getJSONArray("data").toString(),
                                        new TypeToken<List<CompanyInspectionTaskTypeBean>>() {
                                        }.getType());

                                if (beanList == null || beanList.size() == 0) {
                                    HelperView.Toast(InspectionHomeCompanyActivity.this, "未获取到任务类型!");
                                    return;
                                }

                                typeBeanList.addAll(beanList);

                                for (int i = 0; i < typeBeanList.size(); i++) {
                                    checkBoxList.get(i).setText(typeBeanList.get(i).getName());
                                }
                                if (typeBeanList.size() >= 3) {
                                    checkBoxList.get(2).setVisibility(View.VISIBLE);
                                }

                                if (typeBeanList.size() > 3) {
                                    mLayout2.setVisibility(View.VISIBLE);
                                }

                                if (typeBeanList.size() > 6) {
                                    mLayout3.setVisibility(View.VISIBLE);
                                }


                                switch (typeBeanList.size()) {
                                    case 1:
                                        checkBoxList.get(1).setVisibility(View.INVISIBLE);
                                        checkBoxList.get(2).setVisibility(View.INVISIBLE);
                                        break;
                                    case 2:
                                        checkBoxList.get(2).setVisibility(View.INVISIBLE);
                                        break;
                                    case 3:
                                        break;
                                    case 4:
                                        checkBoxList.get(4).setVisibility(View.INVISIBLE);
                                        checkBoxList.get(5).setVisibility(View.INVISIBLE);
                                        break;

                                    case 5:
                                        checkBoxList.get(5).setVisibility(View.INVISIBLE);
                                        break;
                                    case 6:
                                        break;
                                    case 7:
                                        break;
                                }

                            } else {
                                HelperView.Toast(InspectionHomeCompanyActivity.this, json.getString("message"));
                            }
                        } catch (JSONException e) {
                            HelperView.Toast(InspectionHomeCompanyActivity.this, e.getMessage());
                        }
                    }
                });
    }

    @Override
    protected void initData() {

        TextView mAddTask = findViewById(R.id.inspect_add_task);
        mAddTask.setText("创建任务");

        //  判断是否显示创建任务按钮
        InspectionUtils.initData(projectId, null, new InspectionUtils.OnShowSettingInspectCallBack() {
            @Override
            public void OnResult(int type) {
                //
            }

            @Override
            public void OnCompanyResult(int type) {
                dialog.dismiss();
                if (SPUtils.getInstance().getInt("LoginType") == 4 && (type == 2 || type == 3)) {
                    mCreateTask.setVisibility(View.VISIBLE);
                    //LogUtils.showLoge("显示创建任务按钮1111", "ok");
                }
            }

            @Override
            public void onShowRealVideo(boolean isShow) {

            }
        });

        //initDataTask();
    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String action) {
        super.callBackError(resultBean, action);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void initDialogShow() {
        //
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        if (isRefresh == 2) {
            refreshLayout.finishRefresh();
        } else if (isRefresh == 3) {
            refreshLayout.finishLoadMore();
        }
    }

    @Override
    public void initOnClick() {
        //
        tv_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFlag = 0;
                showTimeSelect();
            }
        });
        tv_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFlag = 1;
                showTimeSelect();
            }
        });

        mCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InspectionHomeCompanyActivity.this, InspectionCompanyCreateTaskActivity.class);

                intent.putExtra("projectId", projectId);

                startActivityForResult(intent, 20);
            }
        });
    }

    protected void initDataTask() {

        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
//        params.put("State", "");
//        params.put("PageIndex", "1");
//        params.put("PageSize", "20");
//        params.put("projectId", projectId);

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_COMPANY_INSPECTION_TYPE_LIST)
                .params(params)
                .build()
                .execute(new ResponseResultCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.showLoge("error123", e.getMessage());
                    }

                    @Override
                    public void onResponse(ResultBean<String, String> response, int id) {
                        LogUtils.showLoge("message1", response.getMessage());
                        LogUtils.showLoge("data2", response.getData());
                        LogUtils.showLoge("code3", String.valueOf(response.getCode()));
                        LogUtils.showLoge("success4", String.valueOf(response.isSuccess()));
                        LogUtils.showLoge("login5", String.valueOf(response.isLogin()));
                    }
                });
    }

    @Override
    protected void initDrawLayout() {
        //
        TextView selectButton = findViewById(R.id.inspection_home_screen);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(mRightMenu);
            }
        });

        mDrawerLayout = findViewById(R.id.inspect_layout);
        mDrawerLayout.addDrawerListener(mSimpleDrawerListener);
        mRightMenu = findViewById(R.id.inspect_right_menu);

        TextView mInspectType = findViewById(R.id.inspect_type);

        mInspectType.setText("巡检频次");

        mLayout2 = findViewById(R.id.sliding_layout2);

        mLayout3 = findViewById(R.id.sliding_layout3);

        Button resetButton = findViewById(R.id.sliding_reset);

        Button confirmButton = findViewById(R.id.sliding_confirm);

        final CheckBox typeBox1 = findViewById(R.id.sliding_type1);
        final CheckBox typeBox2 = findViewById(R.id.sliding_type2);
        final CheckBox typeBox3 = findViewById(R.id.sliding_type3);
        final CheckBox typeBox4 = findViewById(R.id.sliding_type4);
        final CheckBox typeBox5 = findViewById(R.id.sliding_type5);
        final CheckBox typeBox6 = findViewById(R.id.sliding_type6);
        final CheckBox typeBox7 = findViewById(R.id.sliding_type7);

        checkBoxList.add(typeBox1);
        checkBoxList.add(typeBox2);
        checkBoxList.add(typeBox3);

        checkBoxList.add(typeBox4);
        checkBoxList.add(typeBox5);
        checkBoxList.add(typeBox6);
        checkBoxList.add(typeBox7);

        final CheckBox stateBox1 = findViewById(R.id.sliding_state1);
        final CheckBox stateBox2 = findViewById(R.id.sliding_state2);
        final CheckBox stateBox3 = findViewById(R.id.sliding_state3);

        stateBox3.setVisibility(View.INVISIBLE);

        initTaskType(null, null);

        typeBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    typeBox2.setChecked(false);
                    typeBox3.setChecked(false);
                    typeBox4.setChecked(false);
                    typeBox5.setChecked(false);
                    typeBox6.setChecked(false);
                    typeBox7.setChecked(false);
                    selectedTypeId = String.valueOf(typeBeanList.get(0).getID());
                }
            }
        });
        typeBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    typeBox1.setChecked(false);
                    typeBox3.setChecked(false);
                    typeBox4.setChecked(false);
                    typeBox5.setChecked(false);
                    typeBox6.setChecked(false);
                    typeBox7.setChecked(false);
                    selectedTypeId = String.valueOf(typeBeanList.get(1).getID());
                }
            }
        });

        typeBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    typeBox2.setChecked(false);
                    typeBox1.setChecked(false);
                    typeBox4.setChecked(false);
                    typeBox5.setChecked(false);
                    typeBox6.setChecked(false);
                    typeBox7.setChecked(false);
                    selectedTypeId = String.valueOf(typeBeanList.get(2).getID());
                }
            }
        });
        typeBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    typeBox2.setChecked(false);
                    typeBox3.setChecked(false);
                    typeBox1.setChecked(false);
                    typeBox5.setChecked(false);
                    typeBox6.setChecked(false);
                    typeBox7.setChecked(false);
                    selectedTypeId = String.valueOf(typeBeanList.get(3).getID());
                }
            }
        });

        typeBox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    typeBox2.setChecked(false);
                    typeBox3.setChecked(false);
                    typeBox4.setChecked(false);
                    typeBox1.setChecked(false);
                    typeBox6.setChecked(false);
                    typeBox7.setChecked(false);
                    selectedTypeId = String.valueOf(typeBeanList.get(4).getID());
                }
            }
        });
        typeBox6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    typeBox2.setChecked(false);
                    typeBox3.setChecked(false);
                    typeBox4.setChecked(false);
                    typeBox5.setChecked(false);
                    typeBox1.setChecked(false);
                    typeBox7.setChecked(false);
                    selectedTypeId = String.valueOf(typeBeanList.get(5).getID());
                }
            }
        });

        typeBox7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    typeBox2.setChecked(false);
                    typeBox3.setChecked(false);
                    typeBox4.setChecked(false);
                    typeBox5.setChecked(false);
                    typeBox6.setChecked(false);
                    typeBox1.setChecked(false);
                    selectedTypeId = String.valueOf(typeBeanList.get(6).getID());
                }
            }
        });


        stateBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    selectedState = "0";
                    stateBox2.setChecked(false);
                    stateBox3.setChecked(false);
                }
            }
        });
        stateBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    selectedState = "10";
                    stateBox1.setChecked(false);
                    stateBox3.setChecked(false);
                }
            }
        });
        stateBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    selectedState = "20";
                    stateBox1.setChecked(false);
                    stateBox2.setChecked(false);
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeBox1.setChecked(false);
                typeBox2.setChecked(false);
                typeBox3.setChecked(false);
                typeBox4.setChecked(false);
                typeBox5.setChecked(false);
                typeBox6.setChecked(false);
                typeBox7.setChecked(false);


                stateBox1.setChecked(false);
                stateBox2.setChecked(false);
                stateBox3.setChecked(false);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(mRightMenu);

                if (!typeBox1.isChecked() && !typeBox2.isChecked() && !typeBox3.isChecked() &&
                        !typeBox4.isChecked() && !typeBox5.isChecked() && !typeBox6.isChecked() && !typeBox7.isChecked()) {
                    selectedTypeId = "";
                    inputBean.setInspectTypeId("");
                } else {
                    inputBean.setInspectTypeId(selectedTypeId);
                }
//                if (!stateBox1.isChecked() && !stateBox2.isChecked() && !stateBox3.isChecked()) {
//                    selectedState = "";
//                    inputBean.setState("");
//                } else {
                inputBean.setState(selectedState);
//                }

                inputBean.setToken(HelperTool.getToken());
                inputBean.setProjectId(projectId);
                inputBean.setPageSize("20");
                currentPosition = 1;
                inputBean.setPageIndex(String.valueOf(currentPosition));

                homeBeanList.clear();

                dialog.show();

                presenter.doTaskInspectionHomeList(inputBean);

                initInspectStateNum();
            }
        });
    }

    @Override
    protected void goToInspectItemList(int position) {
        //super.goToInspectItemList(position);

        Intent intent = new Intent(InspectionHomeCompanyActivity.this, InspectionCompanySystemListActivity.class);

        intent.putExtra("projectId", homeBeanList.get(position).getProjectId());

        intent.putExtra("inspectionTypeId", homeBeanList.get(position).getInspectTypeId());

        intent.putExtra("taskId", homeBeanList.get(position).getID());

        intent.putExtra("state", homeBeanList.get(position).getState());

        //LogUtils.showLoge("打印状态1010---", String.valueOf(homeBeanList.get(position).getState()));

        startActivity(intent);
    }

    @Override
    protected void goToInspectRecords(InspectionTaskHomeBean homeBean) {
        //
        Intent intent = new Intent(InspectionHomeCompanyActivity.this, InspectionRecordsActivity.class);

        intent.putExtra("taskId", homeBean.getID());

        intent.putExtra("bean", homeBean);

        intent.putExtra("isCompany", true);

        startActivity(intent);
    }

    @Override
    protected void initInspectStateNum() {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", projectId);
        params.put("inspectFrequencyId", selectedTypeId);
        params.put("startTime", HelperTool.MillTimeToStringDate(startLongTime));
        params.put("endTime", HelperTool.MillTimeToStringDate(endLongTime));
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_INSPECT_HOME_STATE_NUM_FOR_COMPANY))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        //LogUtils.showLoge("数量返回9090---", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (!jsonObject.getBoolean("success")) {
                                return;
                            }

                            List<InspectStateNumBean> stateNumBeanList = new Gson().fromJson(
                                    jsonObject.getJSONArray("data").toString(), new TypeToken<List<InspectStateNumBean>>() {
                                    }.getType()
                            );
                            if (stateNumBeanList != null && stateNumBeanList.size() > 0) {
                                for (int i = 0; i < stateNumBeanList.size(); i++) {
                                    switch (stateNumBeanList.get(i).getInspectTaskState()) {
                                        case -1:
                                            mNumOne.setText(String.valueOf(stateNumBeanList.get(i).getCount()));
                                            break;
                                        case 0:
                                            mNumTwo.setText(String.valueOf(stateNumBeanList.get(i).getCount()));
                                            break;
                                        case 10:
                                            mNumThree.setText(String.valueOf(stateNumBeanList.get(i).getCount()));
                                            break;
                                        case 20:
                                            mNumFour.setText(String.valueOf(stateNumBeanList.get(i).getCount()));
                                            break;
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 20:
                    homeBeanList.clear();
                    currentPosition = 1;

                    Calendar queryTime = Calendar.getInstance();
                    queryTime.add(Calendar.MINUTE, 2);
                    endLongTime = queryTime.getTimeInMillis();
                    inputBean.setPageIndex(String.valueOf(currentPosition));
                    inputBean.setEndTime(HelperTool.MillTimeToStringDate(endLongTime));

                    presenter.doTaskInspectionHomeList(inputBean);
                    break;
                case 25:
                    if (homeBeanList.size() > 0) {
                        InspectionTaskHomeBean bean = homeBeanList.get(data.getIntExtra("position", 0));
                        bean.setChargeManName(data.getStringExtra("Name"));
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

}
