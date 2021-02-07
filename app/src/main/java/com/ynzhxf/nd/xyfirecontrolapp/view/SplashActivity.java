package com.ynzhxf.nd.xyfirecontrolapp.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import com.allenliu.versionchecklib.callback.APKDownloadListener;
import com.allenliu.versionchecklib.callback.OnCancelListener;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.blankj.utilcode.util.SPUtils;
import com.jaeger.library.StatusBarUtil;
import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.platform.LoginInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.ICheckLoginPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IVersionCheckPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.impl.PlatfromPersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.PermissionsUtil;
import com.ynzhxf.nd.xyfirecontrolapp.view.compentent.CompetentMainActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.EnterpriseMainActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.message.AlarmMessageListActivity;

import java.io.File;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 启动页
 */
public class SplashActivity extends BaseActivity implements ICheckLoginPersenter.ICheckLoginView, IVersionCheckPersenter.IVersionCheckView {

    private ICheckLoginPersenter checkLoginPersenter;
    private IVersionCheckPersenter versionCheckPersenter;
    private GloblePlantformDatas datas = GloblePlantformDatas.getInstance();
    private LoginInfoBean loginInfoBean;
    private Button btn_connect;

    private int sleepTime = 500;

    private boolean isFromClickPush = false;

    private String typeId;
    private String relationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_splash);
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initView();
                    }
                });
            }
        }, 3000);
    }

    @Override
    protected void setBarLintColor() {
        StatusBarUtil.setTranslucent(this, 0);
    }

    /**
     * 初始化
     */
    private void initView() {

        //得到当前界面的装饰视图
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            //让应用主题内容占用系统状态栏的空间,注意:下面两个参数必须一起使用 stable 牢固的
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //设置状态栏颜色为透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //btn_connect = findViewById(R.id.btn_connect);
        isFromClickPush = getIntent().getBooleanExtra("isFromClickPush", false);
        if (isFromClickPush) {
            typeId = getIntent().getStringExtra("TypeId");
            relationId = getIntent().getStringExtra("MessageType");
        }
        checkLoginPersenter = PlatfromPersenterFactory.getCheckLoginImpl(this);
        addPersenter(checkLoginPersenter);
        versionCheckPersenter = PlatfromPersenterFactory.getVersionCheckPersenterImpl(this);
        addPersenter(versionCheckPersenter);
        datas.LoadLoginInfoBean(this);

//        btn_connect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btn_connect.setVisibility(View.GONE);
//                versionCheckPersenter.doVersionCheck();
//            }
//        });

        versionCheckPersenter.doVersionCheck();
    }


    /**
     * 登陆状态检测
     */
    public void checkLoginState() {
        LoginInfoBean query = datas.getLoginInfoBean();

        //LogUtils.eTag("检测登录状态00988", query.getToken() + "~~~~~~~");

        if (HelperTool.stringIsEmptyOrNull(query.getToken())) {
            callBackCheckLogin(new ResultBean<LoginInfoBean, Map<String, String>>());
        } else {
            checkLoginPersenter.doChecklogin(query);
        }
    }

    /**
     * 验证完成完成回调
     *
     * @param resultBean
     */
    @Override
    public void callBackCheckLogin(final ResultBean<LoginInfoBean, Map<String, String>> resultBean) {
        final boolean isSuccess = resultBean.isLogin();
        if (isSuccess) {
            datas.saveLoginInfoBean(this, resultBean);
            //datas.LoadLoginInfoBean(this);
            this.loginInfoBean = resultBean.getData();

            //LogUtils.eTag("splash_token00980990---", resultBean.getData().getToken());
            //LogUtils.eTag("splash_token00980991111---", HelperTool.getToken());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    if (isSuccess) {
                        switch (loginInfoBean.getUserOrganizationType()) {
                            case "1":
                            case "2"://主管部门
                                SPUtils.getInstance().put("LoginType", 2);
                                intent = new Intent(SplashActivity.this, CompetentMainActivity.class);
                                break;
                            case "3"://业主
                                SPUtils.getInstance().put("LoginType", 3);
                                intent = new Intent(SplashActivity.this, EnterpriseMainActivity.class);
                                break;
                            case "4"://维保公司
                                SPUtils.getInstance().put("LoginType", 4);
                                intent = new Intent(SplashActivity.this, EnterpriseMainActivity.class);
                                break;
                            default:
                                finish();
                        }
                        if (isFromClickPush) {
                            intent = new Intent(SplashActivity.this, AlarmMessageListActivity.class);
                            intent.putExtra("isFromClickPush", true);
                            intent.putExtra("TypeId", typeId);
                            intent.putExtra("MessageType", relationId);
                        }

                    } else {
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                        if (isFromClickPush) {
                            intent.putExtra("isFromClickPush", true);
                            intent.putExtra("TypeId", typeId);
                            intent.putExtra("MessageType", relationId);
                        }
                    }

                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Log.d(SplashActivity.class.getSimpleName(), e.getMessage());
                }
            }
        }).start();
    }

    /**
     * 获取版本检测回调
     *
     * @param result
     */
    @Override
    public void callBackVersionCheck(Map<String, String> result) {
        //是否需要更新
        boolean needUpdate = false;
        try {
            if (result != null) {
                final String version = result.get("version");
                final String downPath = result.get("apkPath");
                PackageInfo packageInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                int localVersion = packageInfo.versionCode;
                String[] s = version.split("\\.");
                int queryTotal = Integer.parseInt(s[0]) * 100 + Integer.parseInt(s[1]) * 10 + Integer.parseInt(s[2]);
                if (queryTotal > localVersion) {
                    needUpdate = true;
                    //此处提示更新
                    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

                    if (EasyPermissions.hasPermissions(SplashActivity.this, permissions)) {
                        uploadNewversion(downPath, version);
                    } else {
                        requestPermissionsCallBack(SplashActivity.this, "发现新版本,更新需要您授予读写权限!", 77, permissions, new PermissionsUtil.IGrantCallBack() {
                            @Override
                            public void result(boolean Success, int requestCode) {
                                if (Success && requestCode == 77) {
                                    uploadNewversion(downPath, version);
                                }
                            }
                        });
                    }

                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        if (!needUpdate) {
            checkLoginState();
        }
    }

    /**
     * 下载新版本
     */
    private void uploadNewversion(String downPath, String version) {
        final String queryPath = downPath;
        final String queryVersion = version;
        AllenVersionChecker
                .getInstance()
                .downloadOnly(UIData.create().setDownloadUrl(URLConstant.URL_BASE1 + downPath).setTitle(getResources().getString(R.string.app_name)).setContent("检测到新版本" + version + "，是否需要更新?"))
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {//取消下载按钮
                        checkLoginState();
                    }
                })
                .setApkDownloadListener(new APKDownloadListener() {
                    @Override
                    public void onDownloading(int progress) {

                    }

                    @Override
                    public void onDownloadSuccess(File file) {
                        ActivityController.finishAll();
                    }

                    @Override
                    public void onDownloadFail() {
                        HelperView.Toast(SplashActivity.this, "下载新版本失败！");
                        new AlertDialog.Builder(SplashActivity.this)
                                .setTitle("操作提示")
                                .setMessage("下载新版本失败,检查网络设置！您是否要重新下载？")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        checkLoginState();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        uploadNewversion(queryPath, queryVersion);
                                    }
                                }).create().show();
                    }
                }).executeMission(this);
    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String code) {
        super.callBackError(resultBean, code);
        switch (code) {
            case IVersionCheckPersenter.TAG:
                //btn_connect.setVisibility(View.VISIBLE);
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
