package com.ynzhxf.nd.xyfirecontrolapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.ChargeChildrenProjectListBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.ChargeProjectInfoActivity;
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
 * date 2019/5/29 11:26
 */
public class ChargeChildrenIndexFragment extends BaseFragment {

    private List<ChargeChildrenProjectListBean> childrenList = new ArrayList<>();

    private CommonAdapter adapter;

    private RecyclerView recyclerView;

    private View mNoDataView;

    private String positionStr = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.charge_children_index_fragment, null, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        mNoDataView = view.findViewById(R.id.all_no_data);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //LogUtils.showLoge("children_onActivityCreated_001",10001+"");
        Bundle bundle = getArguments();
        if (bundle != null) {
            positionStr = bundle.getString("position");
        }
        adapter = new CommonAdapter<ChargeChildrenProjectListBean>(context, R.layout.item_charge_index_project_list, childrenList) {
            @Override
            protected void convert(ViewHolder holder, ChargeChildrenProjectListBean bean, int position) {
                TextView mTitleName = holder.getView(R.id.pro_name);
                TextView mProjectDetailsOne = holder.getView(R.id.pro_attr1_name);
                TextView mProjectDetailsTwo = holder.getView(R.id.pro_attr1_value);

                TextView mProjectScoresOne = holder.getView(R.id.pro_attr2_name);
                TextView mProjectScoreTwo = holder.getView(R.id.pro_attr2_value);
                TextView mProjectScoreExtra = holder.getView(R.id.pro_attr2_extra);

                TextView mProIndex = holder.getView(R.id.pro_index);
                mProIndex.setText(String.valueOf(position + 1));

                mTitleName.setText(bean.getName());

                mProIndex.setTextColor(context.getResources().getColor(R.color.white));
                if (position == 0) {
                    mProIndex.setBackground(context.getResources().getDrawable(R.drawable.item_project_list_cir));
                } else if (position == 1) {
                    mProIndex.setBackground(context.getResources().getDrawable(R.drawable.item_project_list_cir));
                } else if (position == 2) {
                    mProIndex.setBackground(context.getResources().getDrawable(R.drawable.item_project_list_cir));
                } else {
                    mProIndex.setBackground(context.getResources().getDrawable(R.drawable.item_main_cir_yellow));
                    mProIndex.setTextColor(context.getResources().getColor(R.color.yellow_star));
                }

                switch (positionStr) {
                    case "0":
                        mProjectDetailsOne.setText("综合评价: ");
                        mProjectDetailsTwo.setText(bean.getRemark());

                        mProjectScoresOne.setText("实时报警: ");
                        mProjectScoreTwo.setText(bean.getStatisticsValue());
                        mProjectScoreExtra.setVisibility(View.VISIBLE);

                        mProjectScoreTwo.setTextColor(context.getResources().getColor(R.color.flat_peterriver));
                        break;
                    case "1":
                        mProjectDetailsOne.setVisibility(View.INVISIBLE);
                        mProjectDetailsTwo.setVisibility(View.INVISIBLE);

                        mProjectScoresOne.setText("风险级别: ");
                        mProjectScoreTwo.setText(bean.getStatisticsValue());
                        mProjectScoreExtra.setVisibility(View.INVISIBLE);

                        double score = 0;
                        try {
                            score = Double.parseDouble(bean.getStatisticsValue());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        if (score == -1) {
                            mProjectScoreTwo.setText("暂无");
                        } else if (score <= 1.0) {
                            mProjectScoreTwo.setText("安全");
                            mProjectScoreTwo.setTextColor(context.getResources().getColor(R.color.risk_assessment_state_color_green));
                        } else if (score > 1.0 && score <= 1.6) {
                            mProjectScoreTwo.setText("轻度");
                            mProjectScoreTwo.setTextColor(context.getResources().getColor(R.color.risk_assessment_state_color_yellow));
                        } else if (score > 1.6 && score <= 2.7) {
                            mProjectScoreTwo.setText("中度");
                            mProjectScoreTwo.setTextColor(context.getResources().getColor(R.color.risk_assessment_state_color_orange));
                        } else if (score > 2.7 && score <= 4.5) {
                            mProjectScoreTwo.setText("高度");
                            mProjectScoreTwo.setTextColor(context.getResources().getColor(R.color.risk_assessment_state_color_red1));
                        } else if (score > 4.5) {
                            mProjectScoreTwo.setText("严重");
                            mProjectScoreTwo.setTextColor(context.getResources().getColor(R.color.risk_assessment_state_color_red2));
                        }
                        break;
                    case "2":
                        mProjectDetailsOne.setText("诊断结果: ");
                        mProjectDetailsTwo.setText(bean.getRemark());

                        mProjectScoresOne.setText("得分: ");
                        mProjectScoreTwo.setText(bean.getStatisticsValue());
                        mProjectScoreExtra.setVisibility(View.INVISIBLE);

                        mProjectScoreTwo.setTextColor(context.getResources().getColor(R.color.risk_assessment_state_color_orange));
                        break;
                }
            }
        };

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ProjectNodeBean bean = new ProjectNodeBean();
                bean.setID(childrenList.get(position).getID());
                bean.setName(childrenList.get(position).getName());
                bean.setAddress(childrenList.get(position).getAddress());
                Intent intent = new Intent(getActivity(), ChargeProjectInfoActivity.class);
                intent.putExtra("projectData", bean);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);

        initGetAssessmentList();
    }

    protected void initGetAssessmentList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("type", positionStr);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_CHARGE_INDEX_GET_PROJECT_LIST)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mNoDataView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("首页项目列表----", positionStr+"~~~~~~"+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                List<ChargeChildrenProjectListBean> childrenListBean = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                        new TypeToken<List<ChargeChildrenProjectListBean>>() {
                                        }.getType());
                                if (childrenListBean == null || childrenListBean.size() == 0) {
                                    mNoDataView.setVisibility(View.VISIBLE);
                                    return;
                                } else {
                                    mNoDataView.setVisibility(View.GONE);
                                }
                                childrenList.clear();
                                childrenList.addAll(childrenListBean);
                                adapter.notifyDataSetChanged();
                            } else {
                                mNoDataView.setVisibility(View.VISIBLE);
                                ToastUtils.showLong("暂无数据!");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public static ChargeChildrenIndexFragment newInstance(String position) {
        Bundle args = new Bundle();
        args.putString("position", position);
        ChargeChildrenIndexFragment fragment = new ChargeChildrenIndexFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
