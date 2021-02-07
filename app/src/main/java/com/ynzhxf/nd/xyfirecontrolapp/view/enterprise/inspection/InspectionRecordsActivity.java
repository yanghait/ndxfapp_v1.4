package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liulishuo.filedownloader.FileDownloader;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionRecordsBackBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionTaskHomeBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.DialogUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.FileOutUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.adapter.OwnerWorkOrderDetailAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.utils.FileUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;

public class InspectionRecordsActivity extends BaseActivity {


    private RecyclerView mRecyclerView;

    private ProgressDialog dialog;

    private InspectionTaskHomeBean homeBean;

    private TextView mTitle;

    private TextView mPerson;

    private TextView mAllTime;

    private TextView mStateName;

    private boolean isCompany = false;

    private boolean isCompanyForOwner = false;

    private List<InspectionRecordsBackBean> beanRecordsList1 = new ArrayList<>();
    private List<InspectionRecordsBackBean> beanRecordsList2 = new ArrayList<>();

    private CommonAdapter adapter;

    private RelativeLayout mRecordsTitle;

    private View mRecordsSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_inspection_records);
        super.onCreate(savedInstanceState);
        setBarTitle("巡检记录");

        mRecyclerView = findViewById(R.id.inspection_records_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mTitle = findViewById(R.id.title);

        mAllTime = findViewById(R.id.all_time);

        mPerson = findViewById(R.id.person);

        mStateName = findViewById(R.id.state_name);

        mRecordsTitle = findViewById(R.id.inspect_record_title);

        mRecordsSpace = findViewById(R.id.inspect_record_space);

        isCompany = getIntent().getBooleanExtra("isCompany", false);
        isCompanyForOwner = getIntent().getBooleanExtra("isCompanyForOwner", false);

        dialog = showProgress(this, "加载中,请稍后...", false);

        homeBean = (InspectionTaskHomeBean) getIntent().getSerializableExtra("bean");

        FileDownloader.setup(this);

        initStartData();

        initInspectRecords(getIntent().getStringExtra("taskId"));
    }

    private void initStartData() {

        mTitle.setText(homeBean.getName());

        mPerson.setText(homeBean.getChargeManName());

        mStateName.setText(homeBean.getStateShow());

        switch (homeBean.getState()) {
            case 0:
                mStateName.setTextColor(getResources().getColor(R.color.fire_fire));
                break;
            case 10:
                mStateName.setTextColor(getResources().getColor(R.color.inspection_list_btn));
                break;
            case 20:
                mStateName.setTextColor(getResources().getColor(R.color.inspection_list_text));
                break;
        }

        String timeShow;
        if (!StringUtils.isEmpty(homeBean.getEndTimeShow()) && "-".equals(homeBean.getEndTimeShow())) {
            timeShow = homeBean.getStartTimeShow();
        } else {
            timeShow = String.valueOf(homeBean.getStartTimeShow() + " - " + homeBean.getEndTimeShow());
        }
        mAllTime.setText(timeShow);

        //处理筛选不同状态巡检记录

        final AlertDialog dialog = new AlertDialog.Builder(this).create();

        TextView mSelectedData = findViewById(R.id.inspection_records_screen);

        mSelectedData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUtils.beginAlertDialog(dialog, new FileUtils.OnClickInspectListener() {
                    @Override
                    public void onButtonFive() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onButtonOne() {
                        dialog.dismiss();

                        beanRecordsList1.clear();

                        beanRecordsList1.addAll(beanRecordsList2);

                        Iterator<InspectionRecordsBackBean> iterator = beanRecordsList1.iterator();
                        while (iterator.hasNext()) {
                            InspectionRecordsBackBean bean = iterator.next();
                            if (bean.getInspectResultValue() != 0) {
                                iterator.remove();
                            }
                        }

                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }

                        if (beanRecordsList1.size() == 0) {
                            showNoDataView();
                        } else {
                            hideNoDataView();
                        }
                    }

                    @Override
                    public void onButtonThree() {
                        dialog.dismiss();

                        beanRecordsList1.clear();

                        beanRecordsList1.addAll(beanRecordsList2);

                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }

                        if (beanRecordsList1.size() == 0) {
                            showNoDataView();
                        } else {
                            hideNoDataView();
                        }
                    }

                    @Override
                    public void onButtonFour() {
                        dialog.dismiss();

                        FileDownloader.setup(InspectionRecordsActivity.this);

                        final int loginType = SPUtils.getInstance().getInt("LoginType", 3);

                        DialogUtil.showSelectMessage(InspectionRecordsActivity.this, "确认导出巡检记录报表并查看吗?",
                                new DialogUtil.IComfirmCancelListener() {
                                    @Override
                                    public void onConfirm() {
                                        if (loginType == 3) {
                                            FileOutUtil.initDataForMonth(true, InspectionRecordsActivity.this,
                                                    getIntent().getStringExtra("taskId"), URLConstant.URL_BASE1 + URLConstant.URL_GET_INSPECT_OWNER_OUT_PDF);
                                        } else if (loginType == 4) {
                                            FileOutUtil.initDataForMonth(true, InspectionRecordsActivity.this,
                                                    getIntent().getStringExtra("taskId"), URLConstant.URL_BASE1 + URLConstant.URL_GET_INSPECT_COMPANY_OUT_PDF);
                                        }
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                    }

                    @Override
                    public void onButtonTwo() {
                        dialog.dismiss();

                        beanRecordsList1.clear();

                        beanRecordsList1.addAll(beanRecordsList2);

                        Iterator<InspectionRecordsBackBean> iterator = beanRecordsList1.iterator();
                        while (iterator.hasNext()) {
                            InspectionRecordsBackBean bean = iterator.next();
                            if (bean.getInspectResultValue() != 1) {
                                iterator.remove();
                            }
                        }

                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }

                        if (beanRecordsList1.size() == 0) {
                            showNoDataView();
                        } else {
                            hideNoDataView();
                        }
                    }
                });
            }
        });
    }

    private void initInspectRecords(String taskId) {

        String recordsUrl;

        if (!isCompany) {
            recordsUrl = URLConstant.URL_BASE1.concat(URLConstant.URL_INSPECTION_GET_RECORDS);
        } else {
            if (isCompanyForOwner) {
                recordsUrl = URLConstant.URL_BASE1.concat(URLConstant.URL_INSPECT_TASK_RECORDS_LIST_FOR_COMPANY);
            } else {
                recordsUrl = URLConstant.URL_BASE1.concat(URLConstant.URL_COMPANY_INSPECT_GET_RECORDS);
            }
        }
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("taskId", taskId);
        OkHttpUtils.post()
                .url(recordsUrl)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                        HelperView.Toast(InspectionRecordsActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("巡检记录2323---", response);
                        dialog.dismiss();
                        try {

                            JSONObject json = new JSONObject(response);
                            if (json.getBoolean("success")) {
                                List<InspectionRecordsBackBean> beanList = new Gson().fromJson(json.getJSONArray("data").toString(),
                                        new TypeToken<List<InspectionRecordsBackBean>>() {
                                        }.getType());
                                if (beanList == null || beanList.size() == 0) {
                                    //HelperView.Toast(InspectionRecordsActivity.this, "暂无巡检记录!");
                                    showNoDataView();
                                    return;
                                } else {
                                    hideNoDataView();
                                }

                                mRecordsSpace.setVisibility(View.VISIBLE);

                                mRecordsTitle.setVisibility(View.VISIBLE);

                                beanRecordsList1.addAll(beanList);

                                beanRecordsList2.addAll(beanList);

                                adapter = new CommonAdapter<InspectionRecordsBackBean>(InspectionRecordsActivity.this, R.layout.item_inspection_records, beanRecordsList1) {
                                    @Override
                                    protected void convert(ViewHolder holder, InspectionRecordsBackBean bean, int position) {

                                        LinearLayout mTimerTopLine = holder.getView(R.id.timer_top_line);

                                        TextView mDetailsIcon = holder.getView(R.id.item_inspect_details_icon);

                                        TextView mTitle = holder.getView(R.id.tv_title_state);

                                        TextView mArea = holder.getView(R.id.tv_company);

                                        TextView tv_person1 = holder.getView(R.id.tv_person1);

                                        TextView mAssigned = holder.getView(R.id.tv_content);

                                        TextView mStateName = holder.getView(R.id.tv_note);

                                        TextView mRemark = holder.getView(R.id.tv_note3);

                                        LinearLayout areaLayout = holder.getView(R.id.linear_tv2);

                                        mTitle.setText(bean.getInspectItemName());

                                        mArea.setText(bean.getInspectAreaName());

                                        mAssigned.setText(bean.getInspectorName());

                                        if (isCompany) {
                                            areaLayout.setVisibility(View.GONE);
                                            tv_person1.setText(bean.getSystemItemName());
                                        }

                                        if (position == 0) {
                                            mTimerTopLine.setVisibility(View.INVISIBLE);
                                        } else {
                                            mTimerTopLine.setVisibility(View.VISIBLE);
                                        }

                                        if (bean.getInspectResultValue() == 1) {
                                            mStateName.setText("正常");
                                            mStateName.setTextColor(getResources().getColor(R.color.inspection_list_btn));
                                            mTitle.setTextColor(getResources().getColor(R.color.black));
                                            mDetailsIcon.setBackground(getResources().getDrawable(R.drawable.inspect_flag_shape));
                                        } else if (bean.getInspectResultValue() == 0) {
                                            mStateName.setText("异常");
                                            mStateName.setTextColor(getResources().getColor(R.color.fire_fire));
                                            mTitle.setTextColor(getResources().getColor(R.color.fire_fire));
                                            mDetailsIcon.setBackground(getResources().getDrawable(R.drawable.inspect_flag_shape_one));
                                        }

                                        mRemark.setText(bean.getRemark());


                                        TextView tv_time_top = holder.getView(R.id.tv_time_top);

                                        TextView tv_time_bottom = holder.getView(R.id.tv_time_bottom);

                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
                                        try {
                                            Date date = formatter.parse(bean.getCreateTimeShow());
                                            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd~HH:mm:ss", Locale.CHINA);
                                            String time = formatter1.format(date);
                                            int i = time.lastIndexOf("~");
                                            tv_time_top.setText(time.substring(0, i));
                                            tv_time_bottom.setText(time.substring(i + 1, time.length()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        GridView gridView = holder.getView(R.id.item_detail_grid_view);

                                        if (!StringUtils.isEmpty(bean.getUploadUrls())) {
                                            String[] url = bean.getUploadUrls().split(",");
                                            if (url.length > 0) {
                                                List<String> paths = new ArrayList<>(Arrays.asList(url));
                                                gridView.setAdapter(new OwnerWorkOrderDetailAdapter(InspectionRecordsActivity.this, paths));
                                            }
                                        }
                                    }
                                };

                                mRecyclerView.setAdapter(adapter);

                            } else {
                                HelperView.Toast(InspectionRecordsActivity.this, json.getString("message"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
