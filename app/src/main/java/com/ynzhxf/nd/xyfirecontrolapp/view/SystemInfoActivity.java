package com.ynzhxf.nd.xyfirecontrolapp.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.suke.widget.SwitchButton;
import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.ILoginOutPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IUserPushStatePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IUserPushStateSettingPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.impl.PlatfromPersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.util.DialogUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.PackageInfoUtil;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.utils.DataCleanManager;

import cn.jpush.android.api.JPushInterface;

/**
 * 当前系统信息详情页
 */
public class SystemInfoActivity extends BaseActivity implements ILoginOutPersenter.ILoginOutView, IUserPushStateSettingPersenter.IUserPushStateSettingView, IUserPushStatePersenter.IUserPushStateView {
    private ILoginOutPersenter loginOutPersenter;
    private IUserPushStatePersenter userPushStatePersenter;
    private IUserPushStateSettingPersenter pushStateSettingPersenter;
    private GloblePlantformDatas datas = GloblePlantformDatas.getInstance();

    //退出登陆
    private View btnLoginOut;
    //密码修改
    private View btnPwdChange;

    //用户消息提醒
    private SwitchButton swMessge;

    private boolean initState = true;

    private ProgressDialog dialog;

    private LinearLayout mClearData;

    private TextView mDataSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_system_info);
        setBarTitle("系统设置");
        super.onCreate(savedInstanceState);
        btnLoginOut = findViewById(R.id.login_out);
        btnPwdChange = findViewById(R.id.pwd_change);
        swMessge = findViewById(R.id.sw_control_message);
        TextView mPackageName = findViewById(R.id.package_name);
        LinearLayout mAboutOur = findViewById(R.id.about_our);

        mClearData = findViewById(R.id.clear_data);
        mDataSize = findViewById(R.id.clear_data_sum);

        loginOutPersenter = PlatfromPersenterFactory.getLoginOutPersenterImpl(this);
        addPersenter(loginOutPersenter);
        userPushStatePersenter = PlatfromPersenterFactory.getUserPushStatePersenterImpl(this);
        addPersenter(userPushStatePersenter);
        pushStateSettingPersenter = PlatfromPersenterFactory.getUserPushStateSettingPersenterImpl(this);
        addPersenter(pushStateSettingPersenter);
        dialog = showProgress(this, "加载中...", false);
        initView();
        userPushStatePersenter.dolUserPushStatePersenter();

        mAboutOur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SystemInfoActivity.this, NewsInfoActivity.class);
                intent.putExtra("data", "http://www.ynndgs.com/");
                intent.putExtra("title", "关于我们");
                startActivity(intent);
            }
        });

        mPackageName.setText(PackageInfoUtil.packageName(this));
    }

    private void initView() {

        swMessge.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                if (!initState) {
                    dialog.show();
                    pushStateSettingPersenter.dolUserPushStateSettingPersenter();
                } else {
                    initState = false;
                }

            }
        });

        //修改密码窗口
        btnPwdChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SystemInfoActivity.this, UserPwdChangeActivity.class);
                startActivity(intent);
            }
        });

        btnLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SystemInfoActivity.this)
                        .setTitle("操作提示")
                        .setMessage("您确认要退出登陆吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showProgressDig(false);
                                loginOutPersenter.dologinOut();
                                JPushInterface.cleanTags(SystemInfoActivity.this, 1);
                            }
                        }).create().show();


            }
        });

        mClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.showSelectMessage(SystemInfoActivity.this, "确定清除缓存吗?", new DialogUtil.IComfirmCancelListener() {
                    @Override
                    public void onConfirm() {
                        DataCleanManager.clearAllCache(SystemInfoActivity.this);

                        String totalSize = "";
                        try {
                            totalSize = DataCleanManager.getTotalCacheSize(SystemInfoActivity.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        mDataSize.setText(totalSize);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

        String totalSize = "";
        try {
            totalSize = DataCleanManager.getTotalCacheSize(SystemInfoActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mDataSize.setText(totalSize);
    }

    /**
     * 异常回调
     *
     * @param resultBean
     * @param code       请求标识
     */
    @Override
    public void callBackError(ResultBean<String, String> resultBean, String code) {
        dialog.dismiss();
        switch (code) {
            case ILoginOutPersenter.TAG://退出登陆
                loginOut();
                break;
        }
    }

    /**
     * 退出登陆回调(先退出再说，服务死了也没关系)
     *
     * @param result
     */
    @Override
    public void callBackLoginOut(ResultBean<String, String> result) {
        loginOut();
    }

    /**
     * 退出登陆回调
     */
    private void loginOut() {
        JPushInterface.cleanTags(this,1);
        datas.clearLoginInfo(this);
        finish();
        ActivityController.finishAll();
        startActivity(new Intent(this, LoginActivity.class));
    }

    /**
     * 设置用户消息接收状态请求回调
     *
     * @param resultBean
     */
    @Override
    public void callBackrUserPushStateSetting(ResultBean<Boolean, String> resultBean) {
        dialog.dismiss();
        try {
            if (resultBean.isSuccess()) {
                if (resultBean.getData() != null) {

                    boolean query = resultBean.getData();

                    if (query) {
                        // 定义极光消息推送服务状态
                        //JPushInterface.resumePush(getApplicationContext());
                        HelperView.Toast(this, "消息提醒已开启");
                    } else {
                        //JPushInterface.stopPush(getApplicationContext());
                        HelperView.Toast(this, "消息提醒已关闭");
                    }
                }
            } else {
                HelperView.Toast(this, resultBean.getMessage());
            }
        } catch (Exception e) {
            HelperView.Toast(this, "消息状态设置失败:" + e.getMessage());
        }

    }

    /**
     * 获取用户消息接受状态
     *
     * @param resultBean
     */
    @Override
    public void callBackrUserPushState(ResultBean<Boolean, String> resultBean) {
        dialog.dismiss();
        try {
            if (resultBean.isSuccess()) {
                if (resultBean.getData() != null) {
                    boolean query = resultBean.getData();
                    swMessge.setChecked(query);
                    if (!query) {
                        initState = false;
                    }
                }
            } else {
                HelperView.Toast(this, resultBean.getMessage());
            }
        } catch (Exception e) {
            HelperView.Toast(this, "消息提醒状态失败：" + e.getMessage());
        }

    }
}
