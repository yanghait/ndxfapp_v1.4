package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionCompanyItemBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.company.InspectionCompanyResultSaveActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * author hbzhou
 * date 2019/1/23 18:21
 */
public class InspectionItemCompanyFragment extends InspectionItemFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inspection_item, container, false);
        mRecyclerView = view.findViewById(R.id.inspection_fragment_recyclerView);
        mNoDataView = view.findViewById(R.id.all_no_data);
        mNoDataContent = view.findViewById(R.id.no_data_message);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration div = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        div.setDrawable(getResources().getDrawable(R.drawable.divider_shape));
        mRecyclerView.addItemDecoration(div);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    public static InspectionItemCompanyFragment newInstance(String systemName, String projectId, String inspectionTypeId, String taskId, int state) {

        Bundle args = new Bundle();

        args.putString("inspectionTypeId", inspectionTypeId);

        args.putString("taskId", taskId);

        args.putInt("state", state);

        args.putString("projectId", projectId);

        args.putString("systemName", systemName);

        InspectionItemCompanyFragment fragment = new InspectionItemCompanyFragment();

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected void initData() {

        HashMap<String, String> params = new HashMap<>();

        params.put("Token", HelperTool.getToken());

        params.put("taskId", taskId);

        params.put("systemTypeId", inspectionTypeId);

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_COMPANY_INSPECTION_GET_ITEM_LIST)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.aTag(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.aTag("维保公司获取巡检项列表1212---", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {

                                List<InspectionCompanyItemBean> itemListBeans = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                        new TypeToken<List<InspectionCompanyItemBean>>() {
                                        }.getType());

                                if (itemListBeans == null || itemListBeans.size() == 0) {
                                    //HelperView.Toast(mContext, "暂无数据!");
                                    mNoDataView.setVisibility(View.VISIBLE);
                                    mNoDataContent.setText("暂无数据!");
                                    return;
                                } else {
                                    mNoDataView.setVisibility(View.GONE);
                                }

                                List<InspectionCompanyItemBean> itemList = new ArrayList<>();

                                for (int i = 0; i < itemListBeans.size(); i++) {
                                    if (itemListBeans.get(i).getState() == state) {
                                        itemList.add(itemListBeans.get(i));
                                    }
                                }
                                //LogUtils.aTag("打印巡检项状态1212---", String.valueOf(state));

                                if (itemList.size() == 0) {
                                    //HelperView.Toast(mContext, "暂无更多数据!");
                                    mNoDataView.setVisibility(View.VISIBLE);
                                    mNoDataContent.setText("暂无数据!");
                                    return;
                                } else {
                                    mNoDataView.setVisibility(View.GONE);
                                }

                                CommonAdapter adapter = new CommonAdapter<InspectionCompanyItemBean>(mContext, R.layout.item_inspection_company_item, itemList) {
                                    @Override
                                    protected void convert(ViewHolder holder, final InspectionCompanyItemBean bean, int position) {
                                        TextView title = holder.getView(R.id.item_title);

                                        Button stateButton = holder.getView(R.id.item_state_btn);

                                        TextView mState = holder.getView(R.id.item_state);
                                        TextView time = holder.getView(R.id.item_time);
                                        TextView person = holder.getView(R.id.item_person);

                                        RelativeLayout itemLayout = holder.getView(R.id.item_layout);

                                        title.setText(bean.getName());

                                        time.setText(String.valueOf("完成时间: " + bean.getOverTimeShow()));

                                        person.setText(String.valueOf("巡检人: " + bean.getInspectorName()));
//
                                        switch (state) {
                                            case 0:
                                                mState.setText("待巡检");
                                                mState.setTextColor(mContext.getResources().getColor(R.color.fire_fire));
                                                itemLayout.setVisibility(View.VISIBLE);
                                                break;
                                            case 10:
                                                mState.setText("已完成");
                                                mState.setTextColor(mContext.getResources().getColor(R.color.inspection_list_btn));
                                                stateButton.setVisibility(View.VISIBLE);


                                                if (StringUtils.isEmpty(bean.getResult())) {
                                                    stateButton.setVisibility(View.INVISIBLE);
                                                } else if ("0".equals(bean.getResult())) {
                                                    stateButton.setText("异常");
                                                    stateButton.setBackground(getResources().getDrawable(R.drawable.inspection_item_btn_one));
                                                } else if ("1".equals(bean.getResult())) {
                                                    stateButton.setText("正常");
                                                    stateButton.setBackground(getResources().getDrawable(R.drawable.inspection_item_btn));
                                                }
                                                break;
                                            case 20:
                                                mState.setText("逾期");
                                                mState.setTextColor(mContext.getResources().getColor(R.color.inspection_list_text));
                                                break;
                                        }

                                        Button inspectionButton = holder.getView(R.id.item_inspection);

                                        inspectionButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                PermissionUtils.permission(PermissionConstants.LOCATION)
                                                        .rationale(new PermissionUtils.OnRationaleListener() {
                                                            @Override
                                                            public void rationale(ShouldRequest shouldRequest) {
                                                                shouldRequest.again(true);
                                                            }
                                                        })
                                                        .callback(new PermissionUtils.FullCallback() {
                                                            @Override
                                                            public void onGranted(List<String> permissionsGranted) {
                                                                if (isAdded()) {
                                                                    Intent intent = new Intent(mContext, InspectionCompanyResultSaveActivity.class);
                                                                    intent.putExtra("isFromCompanyInspect", true);
                                                                    intent.putExtra("projectId", projectId);
                                                                    intent.putExtra("itemId", bean.getID());
                                                                    intent.putExtra("taskId", taskId);
                                                                    intent.putExtra("systemName", systemName);
                                                                    intent.putExtra("systemId", inspectionTypeId);
                                                                    startActivity(intent);
                                                                }
                                                            }

                                                            @Override
                                                            public void onDenied(List<String> deniedForever, List<String> permissionsDenied) {
                                                                if (deniedForever == null || deniedForever.size() == 0) {
                                                                    PermissionUtils.launchAppDetailsSettings();
                                                                }
                                                            }
                                                        })
                                                        .request();
                                            }
                                        });
                                    }
                                };

                                mRecyclerView.setAdapter(adapter);
                            } else {
                                HelperView.Toast(mContext, jsonObject.getString("message"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(mContext, e.getMessage());
                        }
                    }
                });
    }
}
