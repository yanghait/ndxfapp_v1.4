package com.ynzhxf.nd.xyfirecontrolapp.view.message;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.AlarmMessageAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.message.UserMessagePushLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserMsgLogSetAllSeePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserMsgLogSetSeePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserPushMsgLogBeyondTimePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserPushMsgLogPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.impl.MessagePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.OverviewMessageActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file.OperationalPlansActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.OwnerMyWorkOrderActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.OwnerWorkOrderDetailsActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * 用户报警消息列表页面
 */
public class AlarmMessageListActivity extends BaseActivity implements AlarmMessageAdapter.IAlarmMessageClick,
        IUserPushMsgLogPersenter.IUserPushMsgLogView,
        IUserPushMsgLogBeyondTimePersenter.IUserPushMsgLogBeyondTimeView,
        IUserMsgLogSetSeePersenter.IUserMsgLogSetSeeView, IUserMsgLogSetAllSeePersenter.IUserMsgLogSetAllSeeView {
    //列表
    private RecyclerView recyclerView;
    //下拉刷新
    private RefreshLayout refreshLayout;

    //消息列表数据适配器
    private AlarmMessageAdapter adapter;

    //当前页码
    private int pageSize = 1;
    //消息总数
    private int totalCount = 0;
    //总页数
    private int pageTotalCount = 1;

    //每页消息数量
    private int pageCount = 20;

    //当前分页的的消息数量
    private int nowPageCount = 0;

    //消息列表
    private LinkedList<UserMessagePushLogBean> messageList;


    //用户消息分页获取
    private IUserPushMsgLogPersenter pushMsgLogPersenter;

    //获取用户超过指定时间的消息
    private IUserPushMsgLogBeyondTimePersenter userPushMsgLogBeyondTimePersenter;

    //设置消息已查看
    private IUserMsgLogSetSeePersenter setSeePersenter;

    //设置用户所有消息为已阅读
    private IUserMsgLogSetAllSeePersenter setAllSeePersenter;

    private UserMessagePushLogBean clickMessageBean;

    //当前最新时间的消息对象
    private UserMessagePushLogBean nowMaxTimeBean;

    //是否是首次加载消息列表
    private boolean isFirst = true;

    //是否还要运行
    private boolean isRun = true;

    //是否是暂停状态
    private boolean isPause = false;

    //记录流逝时间
    private int loseTime;

    // 消息分类ID
    private String typeId;

    // 消息类型
    private String messageType;

    // dialog
    private ProgressDialog dialog;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_alarm_message_list);
        super.onCreate(savedInstanceState);

        recyclerView = findViewById(R.id.rv_list);
        refreshLayout = findViewById(R.id.refreshLayout);
        messageList = new LinkedList<>();
        pushMsgLogPersenter = MessagePersenterFactory.getUserPushMsgLogPersenter(this);
        addPersenter(pushMsgLogPersenter);

        userPushMsgLogBeyondTimePersenter = MessagePersenterFactory.getUserPushMsgLogBeyondTimePersenterImpl(this);
        addPersenter(userPushMsgLogBeyondTimePersenter);

        setSeePersenter = MessagePersenterFactory.getUserMsgLogSetSeePersenterImpl(this);
        addPersenter(setSeePersenter);
        setAllSeePersenter = MessagePersenterFactory.getUserMsgLogSetAllSeePersenterImpl(this);
        addPersenter(setAllSeePersenter);

        typeId = getIntent().getStringExtra("TypeId");
        url = getIntent().getStringExtra("url");
        messageType = getIntent().getStringExtra("MessageType");
        if (StringUtils.isEmpty(typeId) || StringUtils.isEmpty(messageType)) {
            HelperView.Toast(this, "未获取到消息!");
            return;
        }
        if ("69".equals(messageType)) {
            setBarTitle("历史报警");
        } else if ("72".equals(messageType)) {
            setBarTitle("作战预案");
        } else if ("71".equals(messageType)) {
            setBarTitle("维保消息");
        } else if ("75".endsWith(messageType)) {
            setBarTitle("概览消息");
        }
        init();

        dialog = showProgress(this, "加载中,请稍后...", false);
        pushMsgLogPersenter.doUserPushMsgLog(pageSize, typeId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_message_read_all, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRun = false;
    }

    private void init() {
        adapter = new AlarmMessageAdapter(this, url, messageList, messageType, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(20000);
                if (pageSize <= pageTotalCount) {
                    pushMsgLogPersenter.doUserPushMsgLog(pageSize, typeId);
                } else {
                    HelperView.Toast(AlarmMessageListActivity.this, "没有更多记录！");
                    refreshLayout.finishLoadMore(true);
                }

            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_all_read) {
                    new AlertDialog.Builder(AlarmMessageListActivity.this)
                            .setTitle("操作提示")
                            .setMessage("您是否要将所有消息设施为已阅读状态？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {
                                    dialog.show();
                                    setAllSeePersenter.doUserMsgLogSetAllSee();
                                }
                            })
                            .create().show();
                }
                return true;
            }

        });
    }

    /**
     * 获取消息分页列表回调
     *
     * @param result
     */
    @Override
    public void callBackUserPushMsgLog(ResultBean<PagingBean<UserMessagePushLogBean>, String> result) {

        dialog.dismiss();
        refreshLayout.finishLoadMore(true);
        try {
            if (result.isSuccess()) {
                if (result.getData().getRows().size() == 0) {
                    //HelperView.Toast(this, "暂无消息!");
                }
                messageList.addAll(result.getData().getRows());
                totalCount = result.getData().getTotal();
                pageSize++;
                nowPageCount = result.getData().getRows().size();
                pageTotalCount = result.getData().getTotalPageCount();
                pageCount = result.getData().getPageCount();
                adapter.notifyDataSetChanged();

                if (messageList.size() == 0) {
                    showNoDataView();
                } else {
                    hideNoDataView();
                }
                if (isFirst) {
                    isFirst = false;
                    if (result.getData().getRows().size() > 0) {
                        nowMaxTimeBean = result.getData().getRows().get(0);
                    }
                    String ID = "";
                    if (nowMaxTimeBean != null) {
                        ID = nowMaxTimeBean.getID();
                    }
                    // dialog.show();
                    userPushMsgLogBeyondTimePersenter.doUserPushMsgLogBeyondTime(ID, typeId);
                }
            } else {
                HelperView.Toast(this, result.getMessage());
            }
        } catch (Exception e) {
            LogUtils.showLoge("callBackUserPushMsgLog---", e.getMessage());
        }
    }

    private void initGotoDetails(UserMessagePushLogBean bean) {

        switch (messageType) {
            case "69":
                //此处到历史警告消息详情
                Intent intent1 = new Intent(this, AlarmMessageInfoActivity.class);
                intent1.putExtra("data", bean);
                intent1.putExtra("projectId", bean.getAppPushMsgLogObj().getRelationID());
                startActivity(intent1);
                break;
            case "72":
                //此处到作战预案消息详情
                Intent intent3 = new Intent(this, OperationalPlansActivity.class);
                intent3.putExtra("projectId", bean.getAppPushMsgLogObj().getRelationID());
                startActivity(intent3);
                break;
            case "71":
                //此处到维保消息详情
                if (!StringUtils.isEmpty(bean.getMaintenanceState()) && !StringUtils.isEmpty(bean.getProjectId())) {
                    int state = 0;
                    switch (bean.getMaintenanceState()) {
                        case "0":
                            break;
                        case "10":
                            state = 1;
                            break;
                        case "20":
                            state = 2;
                            break;
                        case "30":
                            state = 3;
                            break;
                        case "40":
                            state = 4;
                            break;
                        case "50":
                            state = 5;
                            break;
                        case "60":
                            state = 6;
                            break;
                        case "70":
                            state = 7;
                            break;
                        case "80":
                            state = 8;
                            break;
                        case "90":
                            state = 9;
                            break;
                    }
                    int loginType = SPUtils.getInstance().getInt("LoginType");

                    SPUtils.getInstance().put("projectNodeId", bean.getProjectId());

                    Intent intent = new Intent(this, OwnerMyWorkOrderActivity.class);
                    intent.putExtra("state", state);
                    if (loginType == 4) {
                        intent.putExtra("isCompany", true);
                    }
                    startActivity(intent);

                } else {
                    int loginType = SPUtils.getInstance().getInt("LoginType");
                    Intent intent2 = new Intent(this, OwnerWorkOrderDetailsActivity.class);
                    intent2.putExtra("ID", bean.getAppPushMsgLogObj().getRelationID());
                    switch (loginType) {
                        case 2:
                            intent2.putExtra("detailType", 3);
                            break;
                        case 3:
                            intent2.putExtra("detailType", 1);
                            break;
                        case 4:
                            intent2.putExtra("detailType", 2);
                            break;
                    }
                    startActivity(intent2);
                }
                break;
            case "75":
                Intent intent = new Intent(this, OverviewMessageActivity.class);
                intent.putExtra("ID", bean.getAppPushMsgLogObj().getRelationID());
                intent.putExtra("projectId",bean.getProjectId());
                if (!StringUtils.isEmpty(bean.getLastPushTimeStr()) && bean.getFirstPushTimeStr().length() > 10) {
                    intent.putExtra("date", bean.getFirstPushTimeStr().substring(0, 10));
                }
                startActivity(intent);
                break;
        }
    }

    /**
     * 消息列表点击回调
     *
     * @param obj
     */
    @Override
    public void AlarmMessageClick(UserMessagePushLogBean obj) {
        clickMessageBean = obj;
        if (GloblePlantformDatas.UserNotSeeCount > 0) {
            GloblePlantformDatas.UserNotSeeCount = GloblePlantformDatas.UserNotSeeCount - 1;
        }

        if (!obj.isHasSee()) {
            setSeePersenter.doUserMsgLogSetSee(obj.getID());
            //showProgressDig(true);
            dialog.show();
        } else {
            //此处到消息详情
//            Intent intent = new Intent(this, AlarmMessageInfoActivity.class);
//            intent.putExtra("data", clickMessageBean);
//            startActivity(intent);

            initGotoDetails(clickMessageBean);
        }
    }

    /**
     * 获取用户消息超过指定时间的消息回调
     *
     * @param result
     */
    @Override
    public void callBackUserPushMsgLogBeyondTime(ResultBean<List<UserMessagePushLogBean>, String> result) {
        dialog.dismiss();
        try {
            if (result.isSuccess()) {
                if (result.getData().size() > 0) {
                    List<UserMessagePushLogBean> datas = result.getData();
                    GloblePlantformDatas.UserNotSeeCount = GloblePlantformDatas.UserNotSeeCount + datas.size();//添加消息数量
                    nowMaxTimeBean = datas.get(datas.size() - 1);
                    boolean needNewAdapter = false;
                    if (messageList.size() < 1) {
                        needNewAdapter = true;
                    }
                    for (int i = 0; i < datas.size(); i++) {
                        messageList.addFirst(datas.get(i));
                    }
                    if (nowPageCount == pageCount) {//当前分页数量等于实际页面数量
                        if (datas.size() < pageCount) {//获取到的数据数量小于分页数量
                            for (int i = 0; i < datas.size(); i++) {
                                messageList.removeLast();
                            }
                            totalCount++;
                        } else {
                            int queryRemovePage = datas.size() / pageCount;
                            int queryYushu = datas.size() % pageCount;
                            if (queryYushu != 0) {
                                for (int i = 0; i < queryYushu; i++) {
                                    messageList.removeLast();
                                }
                                totalCount++;
                            }
                            pageSize += queryRemovePage;
                            totalCount += queryRemovePage;
                        }
                    } else if (nowPageCount < pageCount) {//当前分页数量大于实际分页数量
                        if (datas.size() + nowPageCount <= pageCount) {

                        } else {
                            int queryRemovePage = datas.size() / pageCount;
                            int queryYushu = datas.size() % pageCount;
                            if (queryYushu != 0) {
                                for (int i = 0; i < queryYushu; i++) {
                                    messageList.removeLast();
                                }
                                totalCount++;
                            }
                            pageSize += queryRemovePage;
                            totalCount += queryRemovePage;
                        }
                    }
                    if (needNewAdapter) {
                        adapter = new AlarmMessageAdapter(this, url, messageList, messageType, this);
                        recyclerView.setAdapter(adapter);

                    } else {
                        adapter.notifyDataSetChanged();
                    }

                    if (messageList.size() == 0) {
                        showNoDataView();
                    } else {
                        hideNoDataView();
                    }

                    //播放系统提示音
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                }
            }
            Log.e("fsdfsda", "刷新..." + result.getMessage());

        } catch (Exception e) {
            Log.e(TAG, "获取报警消息失败：" + e.getMessage());
        }
        new UpdateMessageThread().start();

    }

    class UpdateMessageThread extends Thread {
        @Override
        public void run() {
            try {
                if (isRun) {
                    while (loseTime < GloblePlantformDatas.messageFlushTime) {
                        if (!isPause) {
                            loseTime += 300;
                        }
                        Thread.sleep(300);
                    }
                    loseTime = 0;
                    String ID = "";
                    if (nowMaxTimeBean != null) {
                        ID = nowMaxTimeBean.getID();
                    }
                    userPushMsgLogBeyondTimePersenter.doUserPushMsgLogBeyondTime(ID, typeId);
                }
            } catch (Exception e) {

            }
        }
    }


    /**
     * 用户点击消息设置消息已阅读回调
     *
     * @param result
     */
    @Override
    public void callBackUserMsgLogSetSee(ResultBean<Boolean, String> result) {
        //hideProgressDig();
        dialog.dismiss();
        try {
            if (result.isSuccess()) {
                for (int i = 0; i < messageList.size(); i++) {
                    if (messageList.get(i).getID().equals(clickMessageBean.getID())) {
                        messageList.get(i).setHasSee(true);
                        adapter.notifyItemChanged(i);
                        break;
                    }
                }
                //此处为跳转消息详情
//                Intent intent = new Intent(this, AlarmMessageInfoActivity.class);
//                intent.putExtra("data", clickMessageBean);
//                startActivity(intent);
                initGotoDetails(clickMessageBean);
            } else {
                HelperView.Toast(this, "设置消息阅读失败：" + result.getMessage());
            }
        } catch (Exception e) {
            HelperView.Toast(this, "设置消息阅读失败：" + e.getMessage());
        }

    }

    /**
     * 设置用户所有未读消息回调
     *
     * @param result
     */
    @Override
    public void callBackUserMsgLogSetAllSee(ResultBean<Boolean, String> result) {

        dialog.dismiss();
        try {
            if (result.isSuccess()) {
                for (UserMessagePushLogBean query : messageList) {
                    query.setHasSee(true);
                    adapter.notifyDataSetChanged();
                    GloblePlantformDatas.UserNotSeeCount = 0;
                }
            } else {
                HelperView.Toast(this, "阅读所有消息失败：" + result.getMessage());
            }
        } catch (Exception e) {
            HelperView.Toast(this, "阅读所有消息失败：" + e.getMessage());
        }

    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String code) {
        super.callBackError(resultBean, code);
        dialog.dismiss();
        refreshLayout.finishLoadMore(true);
    }
}
