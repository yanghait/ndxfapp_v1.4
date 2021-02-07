package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.charge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.ChargeChildrenProjectListBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.ChargeIndexAreaDataBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.ChargeRightMenuDataBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.ui.AreaSelectedPopupWindow;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.ChargeProjectInfoActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import razerdp.basepopup.BasePopupWindow;


/**
 * author hbzhou
 * date 2019/5/30 15:15
 * 主管部门事件统计点击跳转的二级筛选页
 */
public class ChargeIndexPageSelectedActivity extends BaseActivity {

    protected DrawerLayout mDrawerLayout;

    protected LinearLayout mRightMenu;

    private ChargeIndexAreaDataBean mAreaDataBean;

    private View mPageLine;

    private String mRightSelectedText = "";
    private int mRightSelectedPosition;

    private String mAreaId = "";

    private CommonAdapter adapter;
    private List<ChargeChildrenProjectListBean> childrenList = new ArrayList<>();

    private RecyclerView mRecyclerView;

    private TextView mAreaName;
    private TextView mEventName;
    private TextView mEventCount;

    private AreaSelectedPopupWindow popupWindow;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.sliding_menu_charge);
        super.onCreate(savedInstanceState);
        setBarTitle("统计分析");

        mAreaName = findViewById(R.id.area_name);

        mEventName = findViewById(R.id.event_name);
        mEventCount = findViewById(R.id.event_count);

        mPageLine = findViewById(R.id.page_select_line);

        mRecyclerView = findViewById(R.id.recyclerView);

        mRightSelectedText = getIntent().getStringExtra("RightSelectedText");
        mRightSelectedPosition = getIntent().getIntExtra("RightSelectedPosition", 0);
        mEventName.setText(mRightSelectedText.concat(": "));
        AreaSelectedPopupWindow.selectedPosition[0] = 0;
        AreaSelectedPopupWindow.selectedPosition[1] = 0;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        initDrawerLayout();

        getAreaTypeListData();

        initOnclick();

        initDataAdapter();
    }

    private void initDataAdapter() {
        adapter = new CommonAdapter<ChargeChildrenProjectListBean>(this, R.layout.item_charge_two_page_selected, childrenList) {
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
                RequestBuilder<Drawable> requestErrorBuilder = Glide.with(ChargeIndexPageSelectedActivity.this).asDrawable().load(R.drawable.img_load);

                Glide.with(ChargeIndexPageSelectedActivity.this)
                        .load(URLConstant.URL_BASE1 + chargeCBean.getProjectIcon())
                        .error(requestErrorBuilder)
                        .into(mImageIcon);

                switch (mRightSelectedPosition) {
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
                bean.setID(childrenList.get(position).getID());
                bean.setName(childrenList.get(position).getName());
                bean.setAddress(childrenList.get(position).getAddress());
                Intent intent = new Intent(ChargeIndexPageSelectedActivity.this, ChargeProjectInfoActivity.class);
                intent.putExtra("projectData", bean);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 根据区域ID和事件类型获取项目列表
     */
    private void getProjectTypeData() {
        dialog = showProgress(this,"加载中...",false);
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("areaId", mAreaId);
        params.put("type", mRightSelectedText);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_CHARGE_INDEX_PROJECT_LIST_DATA)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                        ToastUtils.showLong("获取项目列表出错,请稍后再试!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        //LogUtils.showLoge("获取项目分类列表999---", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getBoolean("success")) {
                                List<ChargeChildrenProjectListBean> childrenListBean = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                        new TypeToken<List<ChargeChildrenProjectListBean>>() {
                                        }.getType());
                                if (childrenListBean == null || childrenListBean.size() == 0) {
                                    showNoDataView();
                                    //ToastUtils.showLong("未获取到项目列表!");
                                    mEventCount.setText("0");
                                    return;
                                } else {
                                    hideNoDataView();
                                }
                                childrenList.clear();
                                adapter.notifyDataSetChanged();
                                childrenList.addAll(childrenListBean);
                                adapter.notifyDataSetChanged();

                                mEventCount.setText(String.valueOf(childrenList.size()));
                            } else {
                                ToastUtils.showLong(jsonObject.getString("message"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 处理区域名点击弹出筛选弹框事件
     */
    private void initOnclick() {

        mAreaName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAreaDataBean != null && mAreaDataBean.getNodeLevel() == 3) {
                    return;
                }
                if (mAreaDataBean == null) {
                    ToastUtils.showLong("正在加载区域数据,请稍后再试!");
                    return;
                }
                popupWindow = new AreaSelectedPopupWindow(ChargeIndexPageSelectedActivity.this, mAreaDataBean);

                popupWindow.setAlignBackground(true);

                popupWindow.showPopupWindow(mPageLine);

                // 省市县区弹框弹起时箭头向上
                Drawable mRightDrawable = getResources().getDrawable(R.drawable.charge_top_icon);
                mRightDrawable.setBounds(0, 0, mRightDrawable.getMinimumWidth(), mRightDrawable.getMinimumHeight());
                mAreaName.setCompoundDrawables(null, null, mRightDrawable, null);

                popupWindow.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Drawable mRightDrawable = getResources().getDrawable(R.drawable.charge_down_icon);
                        mRightDrawable.setBounds(0, 0, mRightDrawable.getMinimumWidth(), mRightDrawable.getMinimumHeight());
                        mAreaName.setCompoundDrawables(null, null, mRightDrawable, null);
                    }
                });
            }
        });
    }

    /**
     * 使用EventBus回调省市县区点击事件
     *
     * @param childrenNodes
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAreaClickEvent(ChargeIndexAreaDataBean.ChildrenNodesBeanX childrenNodes) {
        mAreaId = childrenNodes.getID();
        mAreaName.setText(childrenNodes.getName());
        popupWindow.dismiss();
        getProjectTypeData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 动态获取省市县区等区域数据
     */
    private void getAreaTypeListData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_CHARGE_INDEX_GET_AREA_DATA)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showLong("获取区域数据出错,请稍后再试!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("获取到的省市区县数据000---", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            List<ChargeIndexAreaDataBean> dataBeans = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                    new TypeToken<List<ChargeIndexAreaDataBean>>() {
                                    }.getType());
                            if (dataBeans == null || dataBeans.size() == 0) {
                                ToastUtils.showLong("未发现区域数据!");
                                return;
                            }

                            mAreaDataBean = dataBeans.get(0);

                            mAreaId = mAreaDataBean.getID();

                            mAreaName.setText(mAreaDataBean.getName());

                            //如果是省市级才显示可展开箭头
                            if (mAreaDataBean.getNodeLevel() == 1 || mAreaDataBean.getNodeLevel() == 2) {
                                Drawable mRightDrawable = getResources().getDrawable(R.drawable.charge_down_icon);
                                mRightDrawable.setBounds(0, 0, mRightDrawable.getMinimumWidth(), mRightDrawable.getMinimumHeight());
                                mAreaName.setCompoundDrawables(null, null, mRightDrawable, null);
                            }

                            getProjectTypeData();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 动态获取右侧菜单事件类型数据
     * 并处理点击操作
     *
     * @param mRecyclerView
     */
    private void getRightMenuData(final RecyclerView mRecyclerView) {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_CHARGE_INDEX_PROJECT_TYPE)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showLong("获取事件类型出错!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("获取项目右侧分类000---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                final List<String> beanList = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                        new TypeToken<List<String>>() {
                                        }.getType());
                                if (beanList == null || beanList.size() == 0) {
                                    ToastUtils.showLong("未获取到事件类型!");
                                    return;
                                }

                                final List<ChargeRightMenuDataBean> menuDataBeans = new ArrayList<>();
                                for (int i = 0; i < beanList.size(); i++) {
                                    ChargeRightMenuDataBean dataBean = new ChargeRightMenuDataBean();
                                    dataBean.setmRightData(beanList.get(i));
                                    if (!StringUtils.isEmpty(mRightSelectedText) && mRightSelectedText.equals(beanList.get(i))) {
                                        dataBean.setmSelectFlag(true);
                                    } else {
                                        dataBean.setmSelectFlag(false);
                                    }
                                    menuDataBeans.add(dataBean);
                                }
                                CommonAdapter adapter = new CommonAdapter<ChargeRightMenuDataBean>(ChargeIndexPageSelectedActivity.this, R.layout.item_right_menu_page, menuDataBeans) {
                                    @Override
                                    protected void convert(ViewHolder holder, final ChargeRightMenuDataBean dataBean, final int position) {
                                        final TextView mItemName = holder.getView(R.id.slide_item_name);
                                        mItemName.setText(dataBean.getmRightData());

                                        View mItemLine = holder.getView(R.id.slide_item_line);
                                        if (position == menuDataBeans.size() - 1) {
                                            mItemLine.setVisibility(View.INVISIBLE);
                                        } else {
                                            mItemLine.setVisibility(View.VISIBLE);
                                        }
                                        if (dataBean.ismSelectFlag()) {
                                            mItemName.setTextColor(getResources().getColor(R.color.device_diagnose_orange));
                                        } else {
                                            mItemName.setTextColor(getResources().getColor(R.color.black));
                                        }

                                        mItemName.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                mDrawerLayout.closeDrawer(mRightMenu);
                                                mItemName.setTextColor(getResources().getColor(R.color.device_diagnose_orange));

                                                for (int i = 0; i < menuDataBeans.size(); i++) {
                                                    ChargeRightMenuDataBean bean = menuDataBeans.get(i);
                                                    if (i == position) {
                                                        bean.setmSelectFlag(true);
                                                    } else {
                                                        bean.setmSelectFlag(false);
                                                    }
                                                }

                                                mRightSelectedText = dataBean.getmRightData();
                                                mEventName.setText(mRightSelectedText.concat(": "));
                                                mRightSelectedPosition = position;

                                                notifyDataSetChanged();

                                                getProjectTypeData();
                                            }
                                        });

                                    }
                                };
                                mRecyclerView.setAdapter(adapter);
                            } else {
                                ToastUtils.showLong(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 处理DrawerLayout开关 并动态获取右侧菜单列表数据
     */
    private void initDrawerLayout() {
        RecyclerView mMenuRecyclerView = findViewById(R.id.menu_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMenuRecyclerView.setLayoutManager(layoutManager);
        TextView mSelectButton = findViewById(R.id.charge_home_screen);
        mDrawerLayout = findViewById(R.id.charge_slide_layout);
        mDrawerLayout.addDrawerListener(mSimpleDrawerListener);
        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(mRightMenu);
            }
        });
        mRightMenu = findViewById(R.id.charge_right_menu);

        getRightMenuData(mMenuRecyclerView);

    }

    protected DrawerLayout.SimpleDrawerListener mSimpleDrawerListener = new DrawerLayout.SimpleDrawerListener() {
        @Override
        public void onDrawerOpened(View drawerView) {
            //档DrawerLayout打开时，让整体DrawerLayout布局可以响应点击事件
            drawerView.setClickable(true);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
        }
    };
}
