package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.company;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.ChargeChildrenProjectListBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.network.RetrofitUtils;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.CompanyProjectInfoActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * author hbzhou
 * date 2019/6/28 09:54
 * 维保公司首页事件统计点击跳转的二级项目列表页面
 */
public class CompanyTwoPageActivity extends BaseActivity {

    private RecyclerView recyclerView;

    private CommonAdapter adapter;
    private int position;
    private int typePosition;
    private List<ChargeChildrenProjectListBean> homeBeanList = new ArrayList<>();
    private List<ChargeChildrenProjectListBean> normalBeanList = new ArrayList<>();

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_search_project);
        createToolBar((Toolbar) findViewById(R.id.toolbar));
        setBarTitle(getIntent().getStringExtra("TitleName"));
        position = getIntent().getIntExtra("position", 0);
        LinearLayout mSearchLayout = findViewById(R.id.search_layout);
        mSearchLayout.setVisibility(View.VISIBLE);
        TextView mStatisticsName = findViewById(R.id.statistics_name);
        TextView mStatisticsCount = findViewById(R.id.statistics_count);
        mStatisticsCount.setText(getIntent().getStringExtra("Sum"));

        TextView mTitleText = ((Toolbar) findViewById(R.id.toolbar)).findViewById(R.id.toolbar_title);

        if (position == 0) {
            typePosition = 0;
        } else {
            typePosition = 3;
        }

        switch (position) {
            case 0:
                mStatisticsName.setText("项目数量: ");
                break;
            case 1:
                mStatisticsName.setText("待巡检任务数量: ");
                break;
            case 2:
                mStatisticsName.setText("近24小时事件数量: ");
                break;
        }

        recyclerView = findViewById(R.id.search_list_view);

        SearchView searchView = findViewById(R.id.search_view);

        initRecyclerView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (StringUtils.isEmpty(query) || normalBeanList.size() == 0) {
                    ToastUtils.showLong("未发现可搜索项目信息!");
                    return true;
                }
                // 遍历list如不含有搜索字符串则移除
                homeBeanList.clear();
                homeBeanList.addAll(normalBeanList);
                Iterator<ChargeChildrenProjectListBean> it = homeBeanList.iterator();
                while (it.hasNext()) {
                    ChargeChildrenProjectListBean bean = it.next();
                    if (!bean.getName().contains(query)) {
                        it.remove();
                    }
                }
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 当搜索框内容发生变化并且内容为空时 显示全部列表
                if (StringUtils.isEmpty(newText) && normalBeanList.size() > 0) {
                    homeBeanList.clear();
                    homeBeanList.addAll(normalBeanList);
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        searchView.findViewById(androidx.appcompat.R.id.search_plate).setBackground(null);
        searchView.findViewById(androidx.appcompat.R.id.submit_area).setBackground(null);
        searchView.onActionViewExpanded();
        mTitleText.setFocusableInTouchMode(true);
        mTitleText.requestFocus();
        dialog = showProgress(this, "加载中...", false);
        getTwoPageProjectList();
    }

    private void getTwoPageProjectList() {
        RetrofitUtils.getInstance().getCompanyTwoPageProject(HelperTool.getToken(), String.valueOf(position))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean<List<ChargeChildrenProjectListBean>, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable.add(d);
                    }

                    @Override
                    public void onNext(final ResultBean<List<ChargeChildrenProjectListBean>, String> resultBean) {
                        dialog.dismiss();
                        if (resultBean.isSuccess() && resultBean.getData().size() > 0) {
                            hideNoDataView();
                            homeBeanList.clear();
                            homeBeanList.addAll(resultBean.getData());
                            normalBeanList.addAll(resultBean.getData());
                            adapter = new CommonAdapter<ChargeChildrenProjectListBean>(CompanyTwoPageActivity.this, R.layout.item_charge_two_page_selected, homeBeanList) {
                                @Override
                                protected void convert(ViewHolder holder, ChargeChildrenProjectListBean chargeCBean, int position) {
                                    TextView mTitleName = holder.getView(R.id.item_title_name);
                                    TextView mAddressName = holder.getView(R.id.item_address_name);
                                    ImageView mImageIcon = holder.getView(R.id.item_image_icon);

                                    LinearLayout mDeviceLayout = holder.getView(R.id.item_device_layout);
                                    TextView mDeviceDiagnoseResult = holder.getView(R.id.item_device_result);
                                    TextView mDeviceDiagnoseScore = holder.getView(R.id.item_device_score);
                                    TextView mDiagnoseState = holder.getView(R.id.item_diagnose_state);

                                    TextView mErrorCount = holder.getView(R.id.item_error_count);
                                    ImageView mMessageState = holder.getView(R.id.item_message_state);

                                    mDeviceLayout.setVisibility(View.GONE);
                                    mErrorCount.setVisibility(View.GONE);
                                    mMessageState.setVisibility(View.GONE);
                                    mDiagnoseState.setVisibility(View.GONE);
                                    mErrorCount.setTextColor(Color.parseColor("#d93e1c"));

                                    String titleName = chargeCBean.getName();
                                    if (!StringUtils.isEmpty(titleName) && titleName.length() >= 14) {
                                        mTitleName.setText(titleName.substring(0, 14).concat("..."));
                                    } else {
                                        mTitleName.setText(chargeCBean.getName());
                                    }
                                    mAddressName.setText(chargeCBean.getAddress());
                                    RequestBuilder<Drawable> requestErrorBuilder = Glide.with(CompanyTwoPageActivity.this).asDrawable().load(R.drawable.img_load);

                                    Glide.with(CompanyTwoPageActivity.this)
                                            .load(URLConstant.URL_BASE1 + chargeCBean.getProjectIcon())
                                            .error(requestErrorBuilder)
                                            .into(mImageIcon);

                                    switch (typePosition) {
                                        case 0://0
                                            mMessageState.setVisibility(View.VISIBLE);
                                            if (!StringUtils.isEmpty(chargeCBean.getStatisticsValue()) && "0".equals(chargeCBean.getStatisticsValue())) {
                                                mMessageState.setImageDrawable(getResources().getDrawable(R.drawable.message_ok));
                                            } else {
                                                mMessageState.setImageDrawable(getResources().getDrawable(R.drawable.message_error));
                                            }
                                            break;
                                        case 5://5
                                            mMessageState.setVisibility(View.VISIBLE);
                                            if (!StringUtils.isEmpty(chargeCBean.getStatisticsValue()) && "0".equals(chargeCBean.getStatisticsValue())) {
                                                mMessageState.setImageDrawable(getResources().getDrawable(R.drawable.message_ok));
                                            } else {
                                                mMessageState.setImageDrawable(getResources().getDrawable(R.drawable.message_error));
                                            }
                                            break;
                                        case 2://2
                                            double score = 0;
                                            mDiagnoseState.setVisibility(View.VISIBLE);
                                            try {
                                                score = Double.parseDouble(chargeCBean.getStatisticsValue());
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                                ToastUtils.showLong("获取风险评估分数出错!");
                                            }
                                            if (score <= 1.0) {
                                                mDiagnoseState.setText("安全");
                                                mDiagnoseState.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_green));
                                            } else if (score > 1.0 && score <= 1.6) {
                                                mDiagnoseState.setText("轻度");
                                                mDiagnoseState.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_yellow));

                                            } else if (score > 1.6 && score <= 2.7) {
                                                mDiagnoseState.setText("中度");
                                                mDiagnoseState.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_orange));

                                            } else if (score > 2.7 && score <= 4.5) {
                                                mDiagnoseState.setText("高度");
                                                mDiagnoseState.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_red1));
                                            } else if (score > 4.5) {
                                                mDiagnoseState.setText("严重");
                                                mDiagnoseState.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_red2));
                                            }

                                            break;
                                        case 1://1
                                            mDeviceLayout.setVisibility(View.VISIBLE);
                                            mDeviceDiagnoseResult.setText(chargeCBean.getRemark());
                                            mDeviceDiagnoseScore.setText(chargeCBean.getStatisticsValue());
                                            break;
                                        case 3://3
                                            mErrorCount.setVisibility(View.VISIBLE);
                                            mErrorCount.setText(chargeCBean.getStatisticsValue());
                                            break;
                                        case 4://4
                                            mErrorCount.setVisibility(View.VISIBLE);
                                            mErrorCount.setText(chargeCBean.getStatisticsValue());
                                            break;
                                    }
                                }
                            };

                            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                                    ProjectNodeBean bean = new ProjectNodeBean();
                                    bean.setID(homeBeanList.get(position).getID());
                                    bean.setName(homeBeanList.get(position).getName());
                                    bean.setAddress(homeBeanList.get(position).getAddress());
                                    Intent intent = new Intent(CompanyTwoPageActivity.this, CompanyProjectInfoActivity.class);
                                    intent.putExtra("projectData", bean);
                                    startActivity(intent);
                                }

                                @Override
                                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                    return false;
                                }
                            });

                            recyclerView.setAdapter(adapter);
                        } else {
                            showNoDataView();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        ToastUtils.showLong(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
