package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionAreaListBackBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionItemListBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class InspectionAssignedTaskFragment extends InspectionItemFragment {

    private List<InspectionAreaListBackBean> areaListHome = new ArrayList<>();

    private List<InspectionItemListBean> itemListHome = new ArrayList<>();

    private CommonAdapter adapter1;
    private CommonAdapter adapter2;

    private Context mContext;

    private LinearLayout mNoDataView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inspection_item, container, false);
        mRecyclerView = view.findViewById(R.id.inspection_fragment_recyclerView);
        mNoDataView = view.findViewById(R.id.all_no_data);
        TextView mNoDataContent = view.findViewById(R.id.no_data_message);
        mNoDataContent.setText("暂无数据!");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static InspectionAssignedTaskFragment newInstance(String projectId, int state, String taskId) {

        Bundle args = new Bundle();

        args.putString("projectId", projectId);

        args.putInt("state", state);

        args.putString("taskId", taskId);

        InspectionAssignedTaskFragment fragment = new InspectionAssignedTaskFragment();

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected void initData() {

        if (StringUtils.isEmpty(projectId)) {
            HelperView.Toast(mContext, "暂无数据!");
            return;
        }

        HashMap<String, String> params = new HashMap<>();

        params.put("Token", HelperTool.getToken());

        params.put("projectId", projectId);

        //params.put("taskId", taskId);

        //params.put("inspectTypeId", inspectionTypeId);
        //URL_INSPECTION_ITEM_LIST_ONE

        String url;
        if (state == 0) {
            url = URLConstant.URL_BASE1 + URLConstant.URL_INSPECTION_AREA_LIST;
        } else {
            url = URLConstant.URL_BASE1 + URLConstant.URL_INSPECTION_ITEM_LIST_ONE;
        }

        OkHttpUtils.post()
                .url(url)//URL_INSPECTION_AREA_LIST
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(getActivity(), e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("巡检项列表返回1313---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {

                                if (state == 0) {
                                    List<InspectionAreaListBackBean> areaList = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                            new TypeToken<List<InspectionAreaListBackBean>>() {
                                            }.getType());
                                    if (areaList == null || areaList.size() == 0) {
                                        //HelperView.Toast(mContext, "暂无数据!");
                                        mNoDataView.setVisibility(View.VISIBLE);
                                        return;
                                    }else{
                                        mNoDataView.setVisibility(View.GONE);
                                    }

                                    areaListHome.addAll(areaList);

                                    adapter1 = new CommonAdapter<InspectionAreaListBackBean>(mContext, R.layout.item_assigned_task_list, areaListHome) {
                                        @Override
                                        protected void convert(ViewHolder holder, final InspectionAreaListBackBean areaBean, final int position) {
                                            TextView areaName = holder.getView(R.id.item_are_name);

                                            TextView inspectName = holder.getView(R.id.item_are_inspect_name);

                                            areaName.setText(areaBean.getName());

                                            inspectName.setText(String.valueOf("巡检人:" + areaBean.getInspectorName()));

                                            inspectName.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = new Intent(mContext, InspectionSetPersonActivity.class);

                                                    intent.putExtra("position", position);

                                                    intent.putExtra("taskId", taskId);

                                                    intent.putExtra("state", state);

                                                    intent.putExtra("areaId", areaBean.getID());

                                                    intent.putExtra("url", URLConstant.URL_BASE1.concat(URLConstant.URL_INSPECTION_SET_AREA_PERSON));

                                                    startActivityForResult(intent, 6);
                                                }
                                            });
                                        }
                                    };
                                    mRecyclerView.setAdapter(adapter1);
                                } else if (state == 1) {

                                    List<InspectionItemListBean> itemList = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                            new TypeToken<List<InspectionItemListBean>>() {
                                            }.getType());
                                    if (itemList == null || itemList.size() == 0) {
                                        //HelperView.Toast(mContext, "暂无数据!");
                                        mNoDataView.setVisibility(View.VISIBLE);
                                        return;
                                    }else{
                                        mNoDataView.setVisibility(View.GONE);
                                    }

                                    itemListHome.addAll(itemList);

                                    adapter2 = new CommonAdapter<InspectionItemListBean>(mContext, R.layout.item_assigned_task_list, itemListHome) {
                                        @Override
                                        protected void convert(ViewHolder holder, final InspectionItemListBean areaBean, final int position) {
                                            TextView areaName = holder.getView(R.id.item_are_name);

                                            TextView inspectName = holder.getView(R.id.item_are_inspect_name);

                                            areaName.setText(areaBean.getName());

                                            inspectName.setText(String.valueOf("巡检人:" + areaBean.getInspectorName()));

                                            inspectName.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = new Intent(mContext, InspectionSetPersonActivity.class);

                                                    intent.putExtra("position", position);

                                                    intent.putExtra("taskId", taskId);

                                                    intent.putExtra("state", state);

                                                    intent.putExtra("areaId", areaBean.getID());

                                                    intent.putExtra("url", URLConstant.URL_BASE1.concat(URLConstant.URL_INSPECTION_SET_FIRE_PERSON));

                                                    startActivityForResult(intent, 7);
                                                }
                                            });
                                        }
                                    };
                                    mRecyclerView.setAdapter(adapter2);

                                }
                            } else {
                                HelperView.Toast(mContext, jsonObject.getString("message"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {

            LogUtils.showLoge("分配任务onActivityResult1313---", "~~~" + requestCode + "~~~~");

            switch (requestCode) {
                case 6:
                    int position = data.getIntExtra("position", 0);
                    if (position <= areaListHome.size() - 1) {
                        InspectionAreaListBackBean areaBean = areaListHome.get(position);
                        areaBean.setInspectorName(String.valueOf("" + data.getStringExtra("Name")));
                        if (adapter1 != null) {
                            adapter1.notifyDataSetChanged();
                        }
                    }
                    break;
                case 7:
                    int position1 = data.getIntExtra("position", 0);
                    if (position1 <= itemListHome.size() - 1) {
                        InspectionItemListBean areaBean = itemListHome.get(position1);
                        areaBean.setInspectorName(String.valueOf("" + data.getStringExtra("Name")));
                        if (adapter2 != null) {
                            adapter2.notifyDataSetChanged();
                        }
                    }
                    break;
            }
        }
    }
}
