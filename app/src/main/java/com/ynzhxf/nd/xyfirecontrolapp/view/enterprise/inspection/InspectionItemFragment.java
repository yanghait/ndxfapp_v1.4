package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionItemListBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.NFCReadActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.fragment.BaseFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

public class InspectionItemFragment extends BaseFragment {

    protected RecyclerView mRecyclerView;

    protected String projectId;

    protected String inspectionTypeId;

    protected String taskId;

    protected int state = 0;

    protected LinearLayout mNoDataView;

    protected Context mContext;

    protected String systemName;

    protected TextView mNoDataContent;

    private static final int QRSCAN_CODE = 1001;

    private static final int NFC_CODE = 1002;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static InspectionItemFragment newInstance(String projectId, String inspectionTypeId, String taskId, int state) {

        Bundle args = new Bundle();

        args.putString("projectId", projectId);

        args.putString("inspectionTypeId", inspectionTypeId);

        args.putInt("state", state);

        args.putString("taskId", taskId);

        InspectionItemFragment fragment = new InspectionItemFragment();

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        projectId = getArguments().getString("projectId");

        inspectionTypeId = getArguments().getString("inspectionTypeId");

        state = getArguments().getInt("state");

        taskId = getArguments().getString("taskId");

        systemName = getArguments().getString("systemName");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LogUtils.showLoge("InspectionItemFragment_onActivityCreated", "1515虎虎虎1313" + projectId + "~~~~~~" + inspectionTypeId + "~~~" + taskId);

        initData();
    }

    protected void initData() {

        if (StringUtils.isEmpty(projectId) || StringUtils.isEmpty(taskId) || StringUtils.isEmpty(inspectionTypeId)) {
            HelperView.Toast(getActivity(), "暂无数据!");
            mNoDataView.setVisibility(View.VISIBLE);
            return;
        }

        HashMap<String, String> params = new HashMap<>();

        params.put("Token", HelperTool.getToken());

        params.put("projectId", projectId);

        params.put("taskId", taskId);

        if (StringUtils.isEmpty(inspectionTypeId)) {
            inspectionTypeId = "";
        }

        params.put("inspectTypeId", inspectionTypeId);

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_INSPECTION_ITEM_LIST)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //HelperView.Toast(mContext, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        LogUtils.showLoge("onResponse9090---", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {

                                List<InspectionItemListBean> itemListBeans = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                        new TypeToken<List<InspectionItemListBean>>() {
                                        }.getType());

                                if (itemListBeans == null || itemListBeans.size() == 0) {
                                    //HelperView.Toast(mContext, "暂无数据!");
                                    mNoDataView.setVisibility(View.VISIBLE);
                                    mNoDataContent.setText("您不是本任务巡检人\n 没有可操作巡检项");
                                    return;
                                } else {
                                    mNoDataView.setVisibility(View.GONE);
                                }

                                List<InspectionItemListBean> itemList = new ArrayList<>();

                                for (int i = 0; i < itemListBeans.size(); i++) {
                                    if (itemListBeans.get(i).getState() == state) {
                                        itemList.add(itemListBeans.get(i));
                                    }
                                }

                                if (itemList.size() == 0) {
                                    //HelperView.Toast(mContext, "暂无更多数据!");
                                    mNoDataView.setVisibility(View.VISIBLE);
                                    mNoDataContent.setText("暂无数据!");
                                    return;
                                } else {
                                    mNoDataView.setVisibility(View.GONE);
                                }

                                CommonAdapter adapter = new CommonAdapter<InspectionItemListBean>(mContext, R.layout.item_inspection_item_list, itemList) {
                                    @Override
                                    protected void convert(ViewHolder holder, final InspectionItemListBean bean, int position) {
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
                                                if (bean.getResult() == 0) {
                                                    stateButton.setText("异常");
                                                    stateButton.setBackground(getResources().getDrawable(R.drawable.inspection_item_btn_one));
                                                } else if (bean.getResult() == 1) {
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
                                                goToQrCode(bean);
                                            }
                                        });
                                    }
                                };

                                mRecyclerView.setAdapter(adapter);
                            } else {
                                HelperView.Toast(mContext, jsonObject.getString("message"));

                                LogUtils.showLoge("message", jsonObject.getString("message"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(mContext, e.getMessage());
                        }
                    }
                });
    }

    InspectionItemListBean mBean;
    int select = 0;

    protected void goToQrCode(InspectionItemListBean bean) {
        mBean = bean;

        final String[] items = {"二维码", "NFC"};
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("选择巡检方式")
                .setSingleChoiceItems(items, select, (dlg, which) -> {
                    select = which;
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = null;
                        switch (select) {
                            case 0:
                                intent = new Intent(mContext, InspectionQrCodeActivity.class);
                                intent.putExtra("inspectionItem", bean);
                                startActivityForResult(intent, QRSCAN_CODE);
                                break;
                            case 1:
                                intent = new Intent(mContext, NFCReadActivity.class);
                                intent.putExtra("inspectionItem", bean);
                                startActivityForResult(intent, NFC_CODE);
                                break;
                        }
                    }
                }).create();
        dialog.show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == QRSCAN_CODE) {
                Intent intent = new Intent(getContext(), InspectionResultSaveActivity.class);
                intent.putExtra("taskId", taskId);
                intent.putExtra("Name", mBean.getName());
                intent.putExtra("Remark", mBean.getRemark());
                intent.putExtra("AreaId", mBean.getAreaId());
                intent.putExtra("projectId", projectId);
                intent.putExtra("itemId", mBean.getID());
                startActivity(intent);
            } else if (requestCode == NFC_CODE) {
                Intent intent = new Intent(getContext(), InspectionResultSaveActivity.class);
                intent.putExtra("taskId", taskId);
                intent.putExtra("Name", mBean.getName());
                intent.putExtra("Remark", mBean.getRemark());
                intent.putExtra("AreaId", mBean.getAreaId());
                intent.putExtra("projectId", projectId);
                intent.putExtra("itemId", mBean.getID());
                startActivity(intent);
            }
        }

    }
}
