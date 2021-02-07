package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.statement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.share.FileShareMyFileAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.statistics.StatisticsStatementAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.Statement.StatementItemListBean;
import com.ynzhxf.nd.xyfirecontrolapp.util.DialogUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.FileOutUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.utils.FileUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;

import static com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file.OperationalPlansActivity.getMIMEType;


/**
 * author hbzhou
 * date 2019/3/20 13:58
 */
public class StatisticsStatementActivity extends BaseActivity implements OnDateSetListener {

    private TextView mTextStartTime;
    private TextView mTextEndTime;

    private TextView mTextName;

    private LinearLayout mOutFileLayout;

    private int select = 0;

    //开始时间毫秒
    private long startLongTime;

    //结束时间的毫秒数
    private long endLongTime;

    //时间选择控件初始时间
    private Calendar initStartTime = Calendar.getInstance();
    //时间选择控件结束时间
    private Calendar initEndTime = Calendar.getInstance();


    private String startTime;
    private String endTime;

    private int position = 1;

    private String projectId = "";

    private String projectName = "";

    private String urlOne = "";

    private String urlTwo = "";

    private ProgressDialog dialog;

    private RecyclerView mRecyclerView;

    private SmartRefreshLayout refreshLayout;

    private List<StatementItemListBean> itemListBeans = new ArrayList<>();

    private StatisticsStatementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_statistics_statement);
        super.onCreate(savedInstanceState);
        setBarTitle("统计报表");

        refreshLayout = findViewById(R.id.refreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);
        mTextStartTime = findViewById(R.id.txt_start_time);
        mTextEndTime = findViewById(R.id.txt_end_time);
        mTextName = findViewById(R.id.statement_name);
        mOutFileLayout = findViewById(R.id.statement_out_file);
        LinearLayout mTextNameLayout = findViewById(R.id.statement_name_layout);

        ImageButton mDownImage = findViewById(R.id.statement_name_down);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        projectId = getIntent().getStringExtra("projectId");
        projectName = getIntent().getStringExtra("projectName");
        urlOne = getIntent().getStringExtra("urlOne");
        urlTwo = getIntent().getStringExtra("urlTwo");

        setBarTitle(getIntent().getStringExtra("title"));

        int projectNum = SPUtils.getInstance().getInt("ProjectNum");

        if (projectNum > 1) {
            mDownImage.setVisibility(View.VISIBLE);
            mTextNameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StatisticsStatementActivity.this, StatementSearchActivity.class);
                    startActivityForResult(intent, 7);
                }
            });
        }

        mTextName.setText(projectName);

        dialog = showProgress(this, "加载中...", false);

        FileDownloader.setup(this);

        init();

        initData();
    }

    private void init() {
        initStartTime.add(Calendar.YEAR, -1);
        initEndTime.add(Calendar.YEAR, 1);
        Calendar queryTime = Calendar.getInstance();
        endLongTime = queryTime.getTimeInMillis();
        queryTime.add(Calendar.YEAR, -1);
        startLongTime = queryTime.getTimeInMillis();

        // 初始化时间
        mTextStartTime.setText(HelperTool.MillTimeToYearMon(startLongTime));
        mTextEndTime.setText(HelperTool.MillTimeToYearMon(endLongTime));

        startTime = HelperTool.MillTimeToYearMon(startLongTime);
        endTime = HelperTool.MillTimeToYearMon(endLongTime);

        mTextStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = 0;
                showTimeSelect();
            }
        });

        mTextEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = 1;
                showTimeSelect();
            }
        });


        mOutFileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.showSelectMessage(StatisticsStatementActivity.this, "确认导出当月未出报表并查看？",
                        new DialogUtil.IComfirmCancelListener() {
                            @Override
                            public void onConfirm() {
                                dialog.show();
                                initDataForMonth();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                position++;
                initData();
            }
        });

        adapter = new StatisticsStatementAdapter(this, itemListBeans);

        adapter.setOnItemClickListener(new StatisticsStatementAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final StatisticsStatementAdapter.ViewHolder holder, int position) {

                if (StringUtils.isEmpty(itemListBeans.get(position).getFileUrl())) {
                    HelperView.Toast(StatisticsStatementActivity.this, "文件未下载或已删除!");
                    return;
                }

                final String fileName = itemListBeans.get(position).getFileUrl().substring(itemListBeans.get(position).getFileUrl().lastIndexOf("/") + 1);

                final String newPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + fileName;

                boolean exists = FileShareMyFileAdapter.whetherExistFile(FileDownloadUtils.getDefaultSaveRootPath(), fileName);
                if (exists) {
                    DialogUtil.showSelectMessageForButton(StatisticsStatementActivity.this, "打开", "重新下载",
                            "直接打开还是重新下载最新报表?", new DialogUtil.IComfirmCancelListener() {
                                @Override
                                public void onConfirm() {

                                    File file = new File(newPath);
                                    if (file.exists()) {
                                        file.delete();
                                    }

                                    // 处理下载文件逻辑
                                    dialog.show();
                                    LinkedHashMap<Integer, String> hashMap = new LinkedHashMap<>();
                                    hashMap.put(0, itemListBeans.get(holder.getAdapterPosition()).getFileUrl());

                                    FileUtils.initQueueDownload(hashMap, new FileUtils.FileDownloadClick() {
                                        @Override
                                        public void onStart(BaseDownloadTask task) {

                                        }

                                        @Override
                                        public void onCompleted(BaseDownloadTask task) {
                                            dialog.dismiss();

                                            if (StringUtils.isEmpty(fileName)) {
                                                HelperView.Toast(StatisticsStatementActivity.this, "报表未下载成功或已删除!");
                                                return;
                                            }

                                            final String newPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + fileName;

                                            openFilePath(fileName, newPath);
                                        }

                                        @Override
                                        public void onError(BaseDownloadTask task) {
                                            dialog.dismiss();
                                            ToastUtils.showLong("暂无可导出本月报表!");
                                        }
                                    });

                                }

                                @Override
                                public void onCancel() {
//                                    if (Build.VERSION.SDK_INT >= 28) {
//                                        openFileForThirdApp(newPath, fileName);
//                                    } else {
//                                        openFileForSdk(newPath);
//                                    }
                                    openFilePath(fileName, newPath);
                                }
                            });
                } else {
                    HelperView.Toast(StatisticsStatementActivity.this, "文件未下载或已删除!");
                }
            }

            @Override
            public boolean onItemLongClick(View view, StatisticsStatementAdapter.ViewHolder holder, int position) {
                return false;
            }
        });

        mRecyclerView.setAdapter(adapter);
    }

    private void openFilePath(String fileName, String newPath) {
        boolean exists = FileShareMyFileAdapter.whetherExistFile(FileDownloadUtils.getDefaultSaveRootPath(), fileName);
        if (exists) {
//            if (Build.VERSION.SDK_INT >= 28) {
//                openFileForThirdApp(newPath, fileName);
//            } else {
//                openFileForSdk(newPath);
//            }
            FileOutUtil.openFileReader(StatisticsStatementActivity.this,newPath);
        } else {
            HelperView.Toast(StatisticsStatementActivity.this, "文件未下载或已删除!");
        }
    }

    private void showTimeSelect() {

        String queryTitle = "开始时间选择";
        if (select == 1) {
            queryTitle = "结束时间选择";
        }
        long selectTime = startLongTime;
        if (select == 1) {
            selectTime = endLongTime;
        }
        TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)//回调
                .setToolBarTextColor(getResources().getColor(R.color.fire_fire))
                .setCancelStringId("取消")//取消按钮
                .setSureStringId("确定")//确定按钮
                .setTitleStringId(queryTitle)//标题
                .setYearText("年")//Year
                .setMonthText("月")//Month
                .setCyclic(false)//是否可循环
                .setMinMillseconds(initStartTime.getTimeInMillis())//最小日期和时间
                .setMaxMillseconds(initEndTime.getTimeInMillis())//最大日期和时间
                .setCurrentMillseconds(selectTime)
                .setThemeColor(getResources().getColor(R.color.tool_bar))
                .setType(Type.YEAR_MONTH)//类型
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))//未选中的文本颜色
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.tool_bar))//当前文本颜色
                .setWheelItemTextSize(14)//字体大小
                .build();

        mDialogAll.show(getSupportFragmentManager(), "ALL");
    }

    protected void initDataForMonth() {

        if (StringUtils.isEmpty(projectId)) {
            ToastUtils.showLong("未发现项目信息,无法导出报表!");
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("ProjectId", projectId);

        //LogUtils.showLoge("params0000---", HelperTool.getToken() + "~~~" + projectId);

        OkHttpUtils.post()
                .params(params)
                .url(urlOne)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //LogUtils.showLoge("获取业主报表列表008---", e.getMessage());
                        dialog.dismiss();
                        ToastUtils.showLong("暂未发现可导出报表!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("获取业主报表列表008---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {

                                if (StringUtils.isEmpty(jsonObject.getString("data"))) {
                                    ToastUtils.showLong("暂无可导出本月报表!");
                                    return;
                                }

                                final String fileName = jsonObject.getString("data").substring(jsonObject.getString("data").lastIndexOf("/") + 1);

                                FileDownloader.getImpl().clearAllTaskData();

                                // 处理下载文件逻辑
                                LinkedHashMap<Integer, String> hashMap = new LinkedHashMap<>();
                                hashMap.put(0, jsonObject.getString("data"));

                                FileUtils.initQueueDownload(hashMap, new FileUtils.FileDownloadClick() {
                                    @Override
                                    public void onStart(BaseDownloadTask task) {

                                    }

                                    @Override
                                    public void onCompleted(BaseDownloadTask task) {
                                        dialog.dismiss();

                                        if (StringUtils.isEmpty(fileName)) {
                                            HelperView.Toast(StatisticsStatementActivity.this, "报表未下载或已删除!");
                                            return;
                                        }

                                        final String newPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + fileName;

                                        openFilePath(fileName, newPath);
                                    }

                                    @Override
                                    public void onError(BaseDownloadTask task) {
                                        dialog.dismiss();

                                        ToastUtils.showLong("暂无可导出本月报表!");
                                    }
                                });

                            } else {
                                dialog.dismiss();
                                ToastUtils.showLong("暂无可导出本月报表!");
                            }
                        } catch (Exception e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                });
    }

    protected void initData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("StartTime", startTime);
        params.put("EndTime", endTime);
        params.put("PageIndex", String.valueOf(position));
        params.put("PageSize", "10");
        params.put("ProjectId", projectId);

        LogUtils.showLoge("params0000---", HelperTool.getToken() + "~~~" + startTime + "~~~" + endTime + "~~~" + projectId +
                position + "~~~~" + "10");

        OkHttpUtils.post()
                .params(params)
                .url(urlTwo)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.showLoge("获取业主报表列表9090---", e.getMessage());
                        dialog.dismiss();
                        showNoDataView();
                        refreshLayout.finishLoadMore();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("获取业主报表列表9090---", response);
                        dialog.dismiss();
                        refreshLayout.finishLoadMore();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getBoolean("success")) {

                                hideNoDataView();

                                List<StatementItemListBean> beanList = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                        new TypeToken<List<StatementItemListBean>>() {
                                        }.getType());

                                if (itemListBeans.size() == 0 && (beanList == null || beanList.size() == 0)) {
                                    showNoDataView();
                                    return;
                                } else {
                                    hideNoDataView();
                                }

                                if (itemListBeans.size() > 0 && (beanList == null || beanList.size() == 0)) {
                                    ToastUtils.showLong("暂无更多数据!");
                                    return;
                                }

                                itemListBeans.addAll(beanList);

                                adapter.notifyDataSetChanged();

                            } else {
                                ToastUtils.showLong(jsonObject.getString("message"));
                                showNoDataView();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millSeconds) {
        if (select == 0) {
            if (millSeconds >= endLongTime) {
                HelperView.Toast(this, "开始时间不能大于等于结束时间!");
                return;
            }
            startLongTime = millSeconds;
        } else {
            if (millSeconds <= startLongTime) {
                HelperView.Toast(this, "结束时间不能小于等于结束时间!");
                return;
            }
            endLongTime = millSeconds;
        }

        mTextStartTime.setText(HelperTool.MillTimeToYearMon(startLongTime));
        mTextEndTime.setText(HelperTool.MillTimeToYearMon(endLongTime));

        startTime = HelperTool.MillTimeToYearMon(startLongTime);
        endTime = HelperTool.MillTimeToYearMon(endLongTime);

        position = 1;

        itemListBeans.clear();

        dialog.show();

        initData();
    }

    public void openFileForSdk(String newPath) {
        HashMap<String, String> params = new HashMap<>();

        params.put("local", "false");

        QbSdk.openFileReader(StatisticsStatementActivity.this, newPath, params, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                LogUtils.showLogI("OnOpenFile---", s);
            }
        });
    }

    public void openFileForThirdApp(String newPath, String name) {
        Uri uri;
        // 兼容7.0及以上版本获取URI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(StatisticsStatementActivity.this, "com.ynzhxf.nd.firecontrolapp.fileprovider", new File(newPath));
        } else {
            uri = Uri.fromFile(new File(newPath));
        }
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, getMIMEType(name));//getMIMEType(myFileBeanList.get(position).getF_FileName())
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(StatisticsStatementActivity.this, "sorry附件不能打开，请下载相关软件！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 7) {
                projectId = data.getStringExtra("ID");
                projectName = data.getStringExtra("Name");
                mTextName.setText(projectName);

                itemListBeans.clear();

                position = 1;

                initData();
            }
        }
    }
}
