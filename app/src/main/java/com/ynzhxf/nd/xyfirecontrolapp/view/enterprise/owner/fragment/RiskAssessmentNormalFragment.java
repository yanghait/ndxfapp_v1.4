package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.assessment.BuildingListEntityBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.ui.MyLoadMenuBackHeader;
import com.ynzhxf.nd.xyfirecontrolapp.ui.NormalDividerItemDecoration;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.assessment.RiskAssessmentFloorListActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.fragment.BaseFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;


/**
 * author hbzhou
 * date 2019/4/29 14:16
 * 风险评估不同等级建筑物列表
 */
public class RiskAssessmentNormalFragment extends BaseFragment {

    public RefreshLayout refreshLayout;
    public RecyclerView recyclerView;
    public LinearLayout mNoDataView;

    protected String projectId;
    private int state;

    protected CommonAdapter adapter;

    protected List<BuildingListEntityBean> buildingListBeans = new ArrayList<>();

    protected int currentPosition = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_my_order, null);
        refreshLayout = view.findViewById(R.id.owner_my_order_refreshLayout);
        recyclerView = view.findViewById(R.id.owner_mt_order_rv_list);

        mNoDataView = view.findViewById(R.id.all_no_data);

        MyLoadMenuBackHeader myLoadHeader = new MyLoadMenuBackHeader(getActivity());
        refreshLayout.setRefreshHeader(myLoadHeader);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        NormalDividerItemDecoration div = new NormalDividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        div.setDrawable(getResources().getDrawable(R.drawable.divider_shape));
        recyclerView.addItemDecoration(div);
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    public static RiskAssessmentNormalFragment newInstance(int state, String projectId) {
        Bundle args = new Bundle();
        args.putInt("state", state);
        args.putString("projectId", projectId);
        RiskAssessmentNormalFragment fragment = new RiskAssessmentNormalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {
            projectId = bundle.getString("projectId");
            state = bundle.getInt("state");
        }

        adapter = new CommonAdapter<BuildingListEntityBean>(getActivity(), R.layout.item_risk_assessment_normal, buildingListBeans) {
            @Override
            protected void convert(ViewHolder holder, BuildingListEntityBean buildBean, int position) {

                //if (position < buildingListBeans.size() - 1) {

                LinearLayout mItemView = holder.getView(R.id.item_view);
                mItemView.setBackgroundColor(getResources().getColor(R.color.white));

                TextView mBuildingTitle = holder.getView(R.id.item_risk_title);
                TextView mBuildingStateName = holder.getView(R.id.item_risk_state_name);

                TextView mBuildingR = holder.getView(R.id.item_risk_state_back1);
                TextView mBuildingR3 = holder.getView(R.id.item_risk_state_back2);
                TextView mBuildingR1 = holder.getView(R.id.item_risk_state_back3);
                TextView mBuildingR2 = holder.getView(R.id.item_risk_state_back4);
                TextView mBuildingR0 = holder.getView(R.id.item_risk_state_back5);

                mBuildingTitle.setText(buildBean.getBuildName());

                initItemTextFlag(mBuildingR, buildBean.getR());
                initItemTextFlag(mBuildingR3, buildBean.getR3());
                initItemTextFlag(mBuildingR1, buildBean.getR1());
                initItemTextFlag(mBuildingR2, buildBean.getR2());
                initItemTextFlag(mBuildingR0, buildBean.getR0());

                double score = buildBean.getR_Max();

                if (score <= 1.0) {
                    mBuildingStateName.setText("安全");
                    mBuildingStateName.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_green));
                } else if (score > 1.0 && score <= 1.6) {
                    mBuildingStateName.setText("轻度");
                    mBuildingStateName.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_yellow));

                } else if (score > 1.6 && score <= 2.7) {
                    mBuildingStateName.setText("中度");
                    mBuildingStateName.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_orange));

                } else if (score > 2.7 && score <= 4.5) {
                    mBuildingStateName.setText("高度");
                    mBuildingStateName.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_red1));
                } else if (score > 4.5) {
                    mBuildingStateName.setText("严重");
                    mBuildingStateName.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_red2));
                }
                //}

                /*else {

                    View mItemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_risk_assessment_add_view, null);

                    LinearLayout mLayout = holder.getView(R.id.item_view);

                    mLayout.setBackgroundColor(getResources().getColor(R.color.menu_normal_bac));

                    mLayout.removeAllViews();

                    mLayout.addView(mItemView);
                }*/
            }
        };

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), RiskAssessmentFloorListActivity.class);
                intent.putExtra("JsonData", buildingListBeans.get(position).getFloorRiskJson());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        recyclerView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentPosition = 1;
                buildingListBeans.clear();
                initGetAssessmentList();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                currentPosition++;
                initGetAssessmentList();
            }
        });

        initGetAssessmentList();
    }

    /**
     * 获取评估列表前十条
     */
    protected void initGetAssessmentList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", projectId);
        params.put("PageIndex", String.valueOf(currentPosition));

        switch (state) {
            case 0:
                params.remove("safeLevel");
                break;
            case 1:
                params.put("safeLevel", String.valueOf(0));
                break;
            case 2:
                params.put("safeLevel", String.valueOf(1));
                break;
            case 3:
                params.put("safeLevel", String.valueOf(2));
                break;
            case 4:
                params.put("safeLevel", String.valueOf(3));
                break;
            case 5:
                params.put("safeLevel", String.valueOf(4));
                break;
        }

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_RISK_ASSESSMENT_GET_BUILD_RISK_LOG)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (currentPosition == 1) {
                            refreshLayout.finishRefresh();
                        } else {
                            refreshLayout.finishLoadMore();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        LogUtils.showLoge("风险列表类型09000---", response);

                        if (currentPosition == 1) {
                            refreshLayout.finishRefresh();
                        } else {
                            refreshLayout.finishLoadMore();
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {

                                List<BuildingListEntityBean> listEntityBeans = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                        new TypeToken<List<BuildingListEntityBean>>() {
                                        }.getType());

                                if (listEntityBeans == null || listEntityBeans.size() == 0) {
                                    if (buildingListBeans.size() == 0) {
                                        mNoDataView.setVisibility(View.VISIBLE);
                                    } else {
                                        mNoDataView.setVisibility(View.GONE);
                                    }
                                    return;
                                } else {
                                    mNoDataView.setVisibility(View.GONE);
                                }

                                if (currentPosition == 1) {
                                    buildingListBeans.clear();
                                }

                                buildingListBeans.addAll(listEntityBeans);

                                adapter.notifyDataSetChanged();
                            } else {
                                mNoDataView.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initItemTextFlag(TextView mFlag, double score) {
        if (score <= 1.0) {
            mFlag.setText("安全");
            mFlag.setBackground(getResources().getDrawable(R.drawable.item_risk_text_shape));
            mFlag.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_green));
        } else if (score > 1.0 && score <= 1.6) {
            mFlag.setText("轻度");
            mFlag.setBackground(getResources().getDrawable(R.drawable.item_risk_text_shape_yellow));
            mFlag.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_yellow));
        } else if (score > 1.6 && score <= 2.7) {
            mFlag.setText("中度");
            mFlag.setBackground(getResources().getDrawable(R.drawable.item_risk_text_shape_orange));
            mFlag.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_orange));
        } else if (score > 2.7 && score <= 4.5) {
            mFlag.setText("高度");
            mFlag.setBackground(getResources().getDrawable(R.drawable.item_risk_text_shape_red1));
            mFlag.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_red1));
        } else if (score > 4.5) {
            mFlag.setText("严重");
            mFlag.setBackground(getResources().getDrawable(R.drawable.item_risk_text_shape_red2));
            mFlag.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_red2));
        }
    }
}
