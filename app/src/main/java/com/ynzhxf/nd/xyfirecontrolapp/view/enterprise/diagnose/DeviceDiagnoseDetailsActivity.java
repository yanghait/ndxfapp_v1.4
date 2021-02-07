package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.diagnose;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.diagnose.DeviceDiagnoseDetailsItemBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.diagnose.DiagnoseDetailsListItemBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;


/**
 * author hbzhou
 * date 2019/4/16 13:53
 * 诊断分析系统详情
 */
public class DeviceDiagnoseDetailsActivity extends BaseActivity {

    private CommonAdapter adapter;

    private List<DeviceDiagnoseDetailsItemBean> detailsItemBeanList = new ArrayList<>();

    private String childId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_device_diagnose_details);
        super.onCreate(savedInstanceState);
        setBarTitle("详情");

        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);

        TextView mTextTitle = findViewById(R.id.diagnose_details_title);

        TextView mTextScore = findViewById(R.id.diagnose_details_score);

        mTextTitle.setText(getIntent().getStringExtra("Name"));

        int Score = getIntent().getIntExtra("Score", 0);

        childId = getIntent().getStringExtra("ChildId");

        mTextScore.setText(String.valueOf(Score));

        if (Score == 100) {
            mTextScore.setTextColor(getResources().getColor(R.color.device_diagnose_green));
        } else if (Score < 80) {
            mTextScore.setTextColor(getResources().getColor(R.color.device_diagnose_orange));
        } else if (Score < 100) {
            mTextScore.setTextColor(getResources().getColor(R.color.device_diagnose_yellow));
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new CommonAdapter<DeviceDiagnoseDetailsItemBean>(this, R.layout.item_diagnose_details, detailsItemBeanList) {
            @Override
            protected void convert(ViewHolder holder, DeviceDiagnoseDetailsItemBean itemBean, int position) {
                TextView mDetailTitle = holder.getView(R.id.item_details_title);
                TextView mDetailTime = holder.getView(R.id.item_details_time);
                TextView mDetailState = holder.getView(R.id.item_details_state_name);
                TextView mDetailStateDesc = holder.getView(R.id.item_details_state_desc);

                TextView mDetailReason = holder.getView(R.id.item_details_reason);
                TextView mDetailSolute = holder.getView(R.id.item_details_solution);

                mDetailTitle.setText(itemBean.getName());
                mDetailTime.setText(itemBean.getDiagnoseTime());
                //mDetailState.setText(String.valueOf(""+itemBean.getResult()));
                mDetailStateDesc.setText(String.valueOf("描述:" + itemBean.getDianoseTypeDesc()));

                SpannableString s = new SpannableString(String.valueOf("诊断结果:" + itemBean.getResult()));
                if (itemBean.getResultType() == 1 || itemBean.getResultType() == 3) {
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.device_diagnose_green)), 5, s.length(), 0);
                } else {
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.device_diagnose_orange)), 5, s.length(), 0);
                }
                mDetailState.setText(s);

                if (itemBean.getResultType() != 1 && itemBean.getResultType() != 3) {
                    Drawable rightDrawable = getResources().getDrawable(R.drawable.device_diagnose_err);
                    rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                    mDetailTitle.setCompoundDrawables(rightDrawable, null, null, null);
                } else {
                    Drawable rightDrawable = getResources().getDrawable(R.drawable.device_diagnose_nor);
                    rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                    mDetailTitle.setCompoundDrawables(rightDrawable, null, null, null);
                }

                RecyclerView mCusRecyclerView = holder.getView(R.id.custom_recycler);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DeviceDiagnoseDetailsActivity.this);
                mCusRecyclerView.setLayoutManager(linearLayoutManager);

                if (!StringUtils.isEmpty(itemBean.getCause())) {
                    mDetailReason.setVisibility(View.VISIBLE);
                    mDetailReason.setText(String.valueOf("导致原因:" + itemBean.getCause()));
                } else {
                    mDetailReason.setVisibility(View.GONE);
                }
                if (!StringUtils.isEmpty(itemBean.getSolution())) {
                    mDetailSolute.setVisibility(View.VISIBLE);
                    mDetailSolute.setText(String.valueOf("解决方案:" + itemBean.getSolution()));
                } else {
                    mDetailSolute.setVisibility(View.GONE);
                }

                if (!StringUtils.isEmpty(itemBean.getValueJson())) {
                    List<DiagnoseDetailsListItemBean> listItemBeans = new Gson().fromJson(itemBean.getValueJson(),
                            new TypeToken<List<DiagnoseDetailsListItemBean>>() {
                            }.getType());
                    if (listItemBeans != null && listItemBeans.size() > 0) {
                        CommonAdapter cusAdapter = new CommonAdapter<DiagnoseDetailsListItemBean>(DeviceDiagnoseDetailsActivity.this,
                                R.layout.item_diagnose_detail_list_item, listItemBeans) {
                            @Override
                            protected void convert(ViewHolder holder, DiagnoseDetailsListItemBean Bean, int position) {
                                TextView mContent = holder.getView(R.id.item_content);
                                mContent.setText(String.valueOf(Bean.getName() + " " + Bean.getValue()));
                            }
                        };

                        mCusRecyclerView.setAdapter(cusAdapter);
                    }
                }
            }
        };

        mRecyclerView.setAdapter(adapter);

        initData(getIntent().getStringExtra("ID"));
    }

    /**
     * 处理诊断完成页返回数据
     *
     * @param ID
     */
    private void initData(String ID) {

        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("equDiaHistID", ID);

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_DEVICE_DIAGNOSE_SYSTEM_DETAILS)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("诊断详情列表0000---", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                List<DeviceDiagnoseDetailsItemBean> detailsItemBeans = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                        new TypeToken<List<DeviceDiagnoseDetailsItemBean>>() {
                                        }.getType());

                                if (detailsItemBeans == null || detailsItemBeans.size() == 0) {
                                    showNoDataView();
                                    return;
                                } else {
                                    hideNoDataView();
                                }

                                detailsItemBeanList.addAll(detailsItemBeans);

                                if (!StringUtils.isEmpty(childId)) {
                                    Iterator<DeviceDiagnoseDetailsItemBean> iterator = detailsItemBeanList.iterator();
                                    while (iterator.hasNext()) {
                                        DeviceDiagnoseDetailsItemBean itemBean = iterator.next();
                                        if (!childId.equals(itemBean.getID())) {
                                            iterator.remove();
                                        }
                                    }
                                }

                                adapter.notifyDataSetChanged();

                            } else {
                                ToastUtils.showLong(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}
