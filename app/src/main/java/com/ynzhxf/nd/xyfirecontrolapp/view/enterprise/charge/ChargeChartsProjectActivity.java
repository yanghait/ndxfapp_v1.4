package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.charge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargeChartsHistoryAlarmListBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargeExtraContentBean;
import com.ynzhxf.nd.xyfirecontrolapp.network.RetrofitUtils;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * author hbzhou
 * date 2019/11/1 18:29
 */
public class ChargeChartsProjectActivity extends BaseActivity {
    private ProgressDialog dialog;
    private RecyclerView recyclerView;
    private List<ChargeChartsHistoryAlarmListBean.LsHistoryAlarmProjectBean> projectBeans = new ArrayList<>();
    private String typeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_charts_project_list_show);
        super.onCreate(savedInstanceState);
        setBarTitle(getIntent().getStringExtra("title"));
        recyclerView = findViewById(R.id.project_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        dialog = showProgress(this, "加载中...", false);
        typeId = getIntent().getStringExtra("typeId");

        if (StringUtils.isEmpty(typeId)) {
            dialog.dismiss();
            Bundle bundle = getIntent().getExtras();
            projectBeans.addAll((List<ChargeChartsHistoryAlarmListBean.LsHistoryAlarmProjectBean>) bundle.getSerializable("list"));
            initData();
        } else {
            initChartsHistoryAlarm();
        }
    }

    private void initData() {

        BaseQuickAdapter quickAdapter = new BaseQuickAdapter<ChargeChartsHistoryAlarmListBean.LsHistoryAlarmProjectBean, BaseViewHolder>(R.layout.charge_charts_project_list, projectBeans) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, ChargeChartsHistoryAlarmListBean.LsHistoryAlarmProjectBean item) {
                TextView title = helper.getView(R.id.item_project_title);
                TextView type = helper.getView(R.id.item_project_type);
                TextView count = helper.getView(R.id.item_project_count);
                TextView address = helper.getView(R.id.item_project_location);

                title.setText(item.getName());
                type.setText(item.getBusinessTypeName());
                count.setText(String.valueOf(item.getAlarmCount()));
                address.setText(item.getAddress());
            }
        };

        quickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(ChargeChartsProjectActivity.this, ChargeChartsProjectDetailsActivity.class);
                intent.putExtra("ID", projectBeans.get(position).getID());
                startActivity(intent);

                // 测试跳转至项目详情页 根据不同项目id显示相关内容 可展现不同项目详情内容
                // 主管部门项目详情页 使用的是复杂列表 滑动时的性能更好 页面更流畅我们都是知道的 哈哈
                // 同时注意项目详情有两个不同item界面 要分别区分数据并显示 使用list能够优化列表的加载性能 减少内存的占用
            }
        });

        View view = LayoutInflater.from(this).inflate(R.layout.all_no_data, null);
        view.setVisibility(View.VISIBLE);
        quickAdapter.setEmptyView(view);

        recyclerView.setAdapter(quickAdapter);
    }

    private void initChartsHistoryAlarm() {
        RetrofitUtils.getInstance().getChargeChartsHistoryAlarmData(HelperTool.getToken(), "7")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean<ChargeChartsHistoryAlarmListBean, ChargeExtraContentBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResultBean<ChargeChartsHistoryAlarmListBean, ChargeExtraContentBean> resultBean) {
                        dialog.dismiss();
                        if (resultBean.isSuccess() && resultBean.getData() != null) {

                            for (int i = 0; i < resultBean.getData().getLsProjectWithAreaBusiness().size(); i++) {
                                ChargeChartsHistoryAlarmListBean.LsHistoryAlarmProjectBean bean = resultBean.getData().getLsProjectWithAreaBusiness().get(i);
                                if (!StringUtils.isEmpty(typeId) && typeId.equals(bean.getBusinessTypeId())) {
                                    projectBeans.add(bean);
                                }
                            }
                            initData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });
    }
}
