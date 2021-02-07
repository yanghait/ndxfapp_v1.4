package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmSystemEventBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.ui.GridDividerDecoration;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.util.SystemImageUrlUtil;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
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
 * date 2019/1/15 09:21
 * 项目统计信息
 */
public class ProjectStatisticsActivity extends BaseActivity {

    private RecyclerView mRecyclerView1;

    private RecyclerView mRecyclerView2;

    private String projectId;

    private List<FireAlarmSystemEventBean> countList = new ArrayList<>();

    private List<FireAlarmSystemEventBean> topList = new ArrayList<>();

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_project_statistics);
        super.onCreate(savedInstanceState);
        setBarTitle("数据分析");

        mRecyclerView1 = findViewById(R.id.pro_statistics_recycler1);
        mRecyclerView2 = findViewById(R.id.pro_statistics_recycler2);

        // 设置RecyclerView 网格布局
        GridLayoutManager manager = new GridLayoutManager(this, 2);

        GridDividerDecoration dividerDecoration = new GridDividerDecoration(2, getResources().getDimensionPixelSize(R.dimen.item_margin), true);
        // 设置垂直布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView1.setLayoutManager(manager);
        mRecyclerView1.addItemDecoration(dividerDecoration);

        mRecyclerView2.setLayoutManager(linearLayoutManager);

        projectId = getIntent().getStringExtra("projectId");

        dialog = showProgress(this, "加载中...", false);

        //initProjectStatisticsInfo();

        SystemImageUrlUtil.initData(projectId, new SystemImageUrlUtil.IGetImageUrlCallBack() {
            @Override
            public void onResult(List<String> imageUrls) {
                initProjectStatisticsInfo(imageUrls);
            }
        });
    }

    private void initProjectStatisticsInfo(final List<String> imageUrls) {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectID", projectId);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_PROJECT_STATISTICS_INFO)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showLong(e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("项目统计返回1212---", response);
                        dialog.dismiss();
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getBoolean("success")) {
                                JSONObject okJosn = json.getJSONObject("data");
                                List<List<String>> eventTopList = new Gson().fromJson(okJosn.getJSONArray("projectEventTop20").toString(), new TypeToken<List<List<String>>>() {
                                }.getType());

                                List<List<String>> eventConutList = new Gson().fromJson(okJosn.getJSONArray("systemEventCount").toString(), new TypeToken<List<List<String>>>() {
                                }.getType());
                                if (eventConutList == null || eventConutList.size() == 0) {

                                    return;
                                }

                                if (eventTopList == null || eventTopList.size() == 0) {
                                    showNoDataView();
                                } else {
                                    hideNoDataView();
                                }

                                if (eventTopList != null && eventTopList.size() > 0) {
                                    for (int i = 0; i < eventTopList.size(); i++) {
                                        FireAlarmSystemEventBean bean = new FireAlarmSystemEventBean();
                                        if (eventTopList.get(i).size() >= 3) {
                                            bean.setID(eventTopList.get(i).get(0));
                                            bean.setName(eventTopList.get(i).get(1));
                                            bean.setNum(eventTopList.get(i).get(2));
                                        }
                                        topList.add(bean);
                                    }
                                }

                                if (eventConutList != null && eventConutList.size() > 0) {
                                    for (int i = 0; i < eventConutList.size(); i++) {
                                        FireAlarmSystemEventBean bean = new FireAlarmSystemEventBean();
                                        if (eventConutList.get(i).size() >= 5) {
                                            bean.setID(eventConutList.get(i).get(0));
                                            bean.setNum(eventConutList.get(i).get(1));
                                            bean.setName(eventConutList.get(i).get(2));
                                            bean.setContent(eventConutList.get(i).get(3));
                                            bean.setType(eventConutList.get(i).get(4));
                                        }
                                        countList.add(bean);
                                    }
                                }

                                CommonAdapter adapter1 = new CommonAdapter<FireAlarmSystemEventBean>(ProjectStatisticsActivity.this, R.layout.item_statistics_recycler_one, countList) {
                                    @Override
                                    protected void convert(ViewHolder holder, FireAlarmSystemEventBean EventBean, int position) {
                                        TextView title1 = holder.getView(R.id.item_statistics_title1);
                                        title1.setText(EventBean.getName());
                                        TextView title2 = holder.getView(R.id.item_statistics_title2);
                                        title2.setText(EventBean.getType());

                                        LinearLayout mLayout = holder.getView(R.id.fire_alarm_bg);

                                        ImageView mImages = holder.getView(R.id.statistics_images);

                                        if (imageUrls == null || StringUtils.isEmpty(imageUrls.get(position))) {
                                            Picasso.get().load(URLConstant.URL_BASE1).error(R.drawable.img_load).into(mImages);
                                        } else {
                                            Picasso.get().load(imageUrls.get(position)).error(R.drawable.img_load).into(mImages);
                                        }

                                        if ("11".equals(EventBean.getNum())) {
                                            title2.setTextColor(getResources().getColor(R.color.yellow_text_statistics1));
                                            //mLayout.setBackground(getResources().getDrawable(R.drawable.shape_statis_grad));
                                        } else {
                                            title2.setTextColor(getResources().getColor(R.color.statistics_blue));
                                            //mLayout.setBackground(getResources().getDrawable(R.drawable.shape_statis_grad_one));
                                        }

//                                        if (position % 2 == 0) {
//                                            title2.setTextColor(getResources().getColor(R.color.yellow_text_statistics1));
//                                        } else {
//                                            title2.setTextColor(getResources().getColor(R.color.yellow_text_statistics2));
//                                        }
                                    }
                                };

                                adapter1.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                                        //ToastUtils.showLong(countList.get(position).getID());
                                        //initStatisticsDetails(countList.get(position).getID());

                                        Intent intent = new Intent(ProjectStatisticsActivity.this, ProjectStatisticsDetailsActivity.class);
                                        intent.putExtra("projectSystemID", countList.get(position).getID());
                                        intent.putExtra("num", countList.get(position).getNum());
                                        intent.putExtra("title", countList.get(position).getName());
                                        intent.putExtra("projectId", projectId);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                        return false;
                                    }
                                });
                                mRecyclerView1.setAdapter(adapter1);

                                CommonAdapter adapter2 = new CommonAdapter<FireAlarmSystemEventBean>(ProjectStatisticsActivity.this, R.layout.item_statistics_recycler_two, topList) {
                                    @Override
                                    protected void convert(ViewHolder holder, FireAlarmSystemEventBean bean, int position) {
                                        TextView title = holder.getView(R.id.title);

                                        // 处理标题不显示具体位置

                                        String childName = bean.getName();

                                        if (!StringUtils.isEmpty(childName) && childName.contains("-")) {
                                            String[] splitName = childName.split("-");
                                            if (splitName.length > 3) {
                                                String okName = "";
                                                for (int i = 0; i < splitName.length; i++) {
                                                    if (i > 2) {
                                                        okName = okName.concat(splitName[i]).concat("-");
                                                    }
                                                }
                                                title.setText(okName.substring(0, okName.length() - 1));
                                            }
                                        } else {
                                            title.setText(bean.getName());
                                        }

                                        //title.setText(bean.getName());

                                        TextView num = holder.getView(R.id.num);
                                        num.setText(bean.getNum());
                                    }
                                };

                                adapter2.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                                        Intent intent = new Intent(ProjectStatisticsActivity.this, ProjectStatisticsListErrorInfo.class);

                                        intent.putExtra("ID", topList.get(position).getID());

                                        intent.putExtra("Name", topList.get(position).getName());

                                        intent.putExtra("EventId", topList.get(position).getNum());


                                        //LogUtils.showLoge("007", topList.get(position).getID() + "~~~" + topList.get(position).getName());

                                        startActivity(intent);
                                    }

                                    @Override
                                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                        return false;
                                    }
                                });
                                mRecyclerView2.setAdapter(adapter2);

                            } else {
                                ToastUtils.showLong(json.getString("message"));

                                showNoDataView();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initStatisticsDetails(String systemId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectSystemID", systemId);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_PROJECT_STATISTICS_DETAILS)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showLong(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("系统详情页返回1212---", response);
                    }
                });
    }
}
