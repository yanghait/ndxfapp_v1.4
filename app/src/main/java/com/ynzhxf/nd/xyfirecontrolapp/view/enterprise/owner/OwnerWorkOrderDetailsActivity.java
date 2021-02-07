package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.liulishuo.filedownloader.FileDownloader;
import com.squareup.picasso.Picasso;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.OwnerOrderDetailsInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.OwnerWorkOrderDetailsBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IOwnerOrderDetailsPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.impl.MaintenManagePresenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.util.DialogUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.FileOutUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.ScreenUtil;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.adapter.OwnerWorkOrderDetailAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 维保业主详情
 */
public class OwnerWorkOrderDetailsActivity extends BaseActivity implements IOwnerOrderDetailsPresenter.IOwnerOrderDetailsView {

    private IOwnerOrderDetailsPresenter presenter;

    private AppCompatRatingBar ratingBar;
    private TextView tv_order_details_time;

    private RecyclerView recyclerView;

    private CommonAdapter adapter;

    private ProgressDialog dialog;

    private LinearLayout ratingLayout;

    private LinearLayout mNoDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_owner_order_details);
        createToolBar((Toolbar) findViewById(R.id.toolbar));
        setBarTitle("工单详情");

        tv_order_details_time = findViewById(R.id.tv_order_details_time);
        ratingBar = findViewById(R.id.order_details_rating_bar);
        recyclerView = findViewById(R.id.order_details_rv_list);
        ratingLayout = findViewById(R.id.order_detail_rating_layout);

        TextView mOutFile = findViewById(R.id.order_records_out);

        mNoDataView = findViewById(R.id.all_no_data);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        final OwnerOrderDetailsInputBean bean = new OwnerOrderDetailsInputBean();
        bean.setToken(HelperTool.getToken());
        bean.setWorkOrderId(getIntent().getStringExtra("ID"));
        bean.setDetailType(getIntent().getIntExtra("detailType", 1));

        presenter = MaintenManagePresenterFactory.getOwnerOrderDetailsImpl(OwnerWorkOrderDetailsActivity.this);
        addPersenter(presenter);
        presenter.doOwnerOrderDetails(bean);

        dialog = showProgress(this, "加载中...", false);

        final int detailType = getIntent().getIntExtra("detailType", 1);

        FileDownloader.setup(this);

        if (detailType == 1 || detailType == 2) {
            // 只有业主和维保公司可导出报表
            mOutFile.setVisibility(View.VISIBLE);

            mOutFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogUtil.showSelectMessage(OwnerWorkOrderDetailsActivity.this, "确认导出工单记录报表并查看吗?",
                            new DialogUtil.IComfirmCancelListener() {
                                @Override
                                public void onConfirm() {
                                    if (detailType == 1) {
                                        FileOutUtil.initDataForMonth(false, OwnerWorkOrderDetailsActivity.this,
                                                getIntent().getStringExtra("ID"), URLConstant.URL_BASE1 + URLConstant.URL_GET_MAINTANCE_OWNER_OUT_PDF);
                                    } else {
                                        FileOutUtil.initDataForMonth(false, OwnerWorkOrderDetailsActivity.this,
                                                getIntent().getStringExtra("ID"), URLConstant.URL_BASE1 + URLConstant.URL_GET_MAINTANCE_COMPANY_OUT_PDF);
                                    }
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                }
            });
        }
    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String action) {
        super.callBackError(resultBean, action);
        dialog.dismiss();
    }


    @Override
    public void callBackOwnerOrderDetails(ResultBean<OwnerWorkOrderDetailsBean, String> resultBean) {
        dialog.dismiss();
        if (resultBean == null || resultBean.getData() == null || resultBean.getData().lsWorkOrderEventLog == null
                || resultBean.getData().lsWorkOrderEventLog.size() == 0) {

            mNoDataView.setVisibility(View.VISIBLE);
            mNoDataView.getLayoutParams().height = ScreenUtil.dp2px(OwnerWorkOrderDetailsActivity.this, 390);
            return;
        } else {
            mNoDataView.setVisibility(View.GONE);
        }
        adapter = new CommonAdapter<OwnerWorkOrderDetailsBean.IsWorkOrderLog>(OwnerWorkOrderDetailsActivity.this, R.layout.item_owner_order_details,
                resultBean.getData().lsWorkOrderEventLog) {
            @Override
            protected void convert(ViewHolder holder, OwnerWorkOrderDetailsBean.IsWorkOrderLog bean, int position) {

                LinearLayout mLayoutTitleLine = holder.getView(R.id.order_detail_line1);

                TextView mLine0 = holder.getView(R.id.order_detail_line0);
                if (position == 0) {
                    mLayoutTitleLine.setVisibility(View.GONE);
                }
                if (position >= 2) {
                    mLine0.setBackground(getResources().getDrawable(R.color.global_gray_text_color));
                }

                TextView tv_time_top = holder.getView(R.id.tv_time_top);
                TextView tv_time_bottom = holder.getView(R.id.tv_time_bottom);
                TextView line = holder.getView(R.id.item_order_details_line);
                ImageView img = holder.getView(R.id.item_order_details_icon);

                TextView tv_person = holder.getView(R.id.tv_person);
                TextView tv_company = holder.getView(R.id.tv_company);
                TextView tv_content = holder.getView(R.id.tv_content);
                TextView tv_note = holder.getView(R.id.tv_note);

                TextView tv_person1 = holder.getView(R.id.tv_person1);
                TextView tv_company1 = holder.getView(R.id.tv_company1);
                TextView tv_content1 = holder.getView(R.id.tv_content1);
                TextView tv_note1 = holder.getView(R.id.tv_note1);

                TextView tv_title_state = holder.getView(R.id.tv_title_state);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);

//                if(position==0){
//                    LogUtils.showLoge("输出状态state120000---", bean.getWorkOrderState() + "~~~");
//                }


                try {

                    Date date = formatter.parse(bean.getEventDateTimeShow());
                    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd~HH:mm:ss", Locale.CHINA);
                    String time = formatter1.format(date);
                    int i = time.lastIndexOf("~");
                    tv_time_top.setText(time.substring(0, i));
                    tv_time_bottom.setText(time.substring(i + 1, time.length()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Picasso.get().load(URLConstant.URL_BASE1.concat(bean.getStateIconOnUrl())).into(img);
                tv_title_state.setText(bean.getEventTag());

                tv_person.setText(bean.getEventUserName());

                tv_company.setText(bean.getEventUserCompany());
                tv_content.setText(bean.getEventContent());
                tv_note.setText(bean.getEventRemark());

                if (position > 0) {
                    Picasso.get().load(URLConstant.URL_BASE1.concat(bean.getStateIconOffUrl())).into(img);

                    line.setBackground(getResources().getDrawable(R.color.global_gray_text_color));

                    tv_time_top.setTextColor(getResources().getColor(R.color.global_gray_text_color));
                    tv_time_bottom.setTextColor(getResources().getColor(R.color.global_gray_text_color));

                    tv_person.setTextColor(getResources().getColor(R.color.global_gray_text_color));
                    tv_company.setTextColor(getResources().getColor(R.color.global_gray_text_color));
                    tv_content.setTextColor(getResources().getColor(R.color.global_gray_text_color));
                    tv_note.setTextColor(getResources().getColor(R.color.global_gray_text_color));
                    tv_title_state.setTextColor(getResources().getColor(R.color.global_gray_text_color));

                    tv_person1.setTextColor(getResources().getColor(R.color.global_gray_text_color));
                    tv_company1.setTextColor(getResources().getColor(R.color.global_gray_text_color));
                    tv_content1.setTextColor(getResources().getColor(R.color.global_gray_text_color));
                    tv_note1.setTextColor(getResources().getColor(R.color.global_gray_text_color));

                }

                GridView gridView = holder.getView(R.id.item_detail_grid_view);

                if (!StringUtils.isEmpty(bean.getImagePath())) {
                    //LogUtils.showLoge("输出每个详情状态返回文件路径009876--",bean.getImagePath());
                    String[] url = bean.getImagePath().split(",");
                    if (url.length > 0) {
                        List<String> paths = new ArrayList<>(Arrays.asList(url));
                        gridView.setAdapter(new OwnerWorkOrderDetailAdapter(OwnerWorkOrderDetailsActivity.this, paths));
                    }
                }

            }
        };
        initData(resultBean.getData().modWorkOrder);
        recyclerView.setAdapter(adapter);
    }

    private void initData(OwnerWorkOrderDetailsBean.ModWorkOrder bean) {

        //LogUtils.showLoge("输出状态state120---", bean.getState() + "~~~");

        tv_order_details_time.setText("预计完成时间:".concat(String.valueOf(bean.getEstimateEndTimeShow())));
        if (bean.getEvaluateLevel() <= 0 || bean.getState() != 70) {
            ratingLayout.setVisibility(View.GONE);
        } else {
            ratingLayout.setVisibility(View.VISIBLE);
            ratingBar.setRating((float) bean.getEvaluateLevel());
        }
    }
}
