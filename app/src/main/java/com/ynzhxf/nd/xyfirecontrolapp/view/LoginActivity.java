package com.ynzhxf.nd.xyfirecontrolapp.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.platform.LoginInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.ILoginKeyGetPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.ILoginPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.impl.PlatfromPersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.util.DeviceIdUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.compentent.CompetentMainActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.EnterpriseMainActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.message.AlarmMessageListActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;


/**
 * 登陆界面
 */
public class LoginActivity extends BaseActivity implements ILoginPersenter.ILoginView, ILoginKeyGetPersenter.ILoginKeyGetView {

    //登陆按钮
    private Button btnLogin;
    //错误信息
    private TextView txtError;
    //用户名
    private EditText txtLoginName;
    //用户密码
    private EditText txtLoginPwd;
    //验证码输入
    private EditText txtCode;
    //验证码图片
    private ImageView imCode;

    private Vibrator vibrator;

    //登陆令牌
    private String key;

    private ILoginPersenter loginPersenter;

    private ILoginKeyGetPersenter loginKeyGetPersenter;

    private GloblePlantformDatas datas;

    private boolean isFromPush = false;

    private String typeId;
    private String relationId;
    private ProgressDialog dialog;

    private LinearLayout mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
        mContentView = findViewById(R.id.content_view);
        init();
        loginPersenter = PlatfromPersenterFactory.getLoginPersenterImpl(this);
        this.addPersenter(loginPersenter);
        loginKeyGetPersenter = PlatfromPersenterFactory.getLoginKeyGetPersenterImpl(this);
        datas = GloblePlantformDatas.getInstance();
        loginKeyGetPersenter.dologinKeyGetPersenter();
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        isFromPush = getIntent().getBooleanExtra("isFromClickPush", false);
        if (isFromPush) {
            typeId = getIntent().getStringExtra("TypeId");
            relationId = getIntent().getStringExtra("MessageType");
        }
        dialog = showProgress(this, "加载中,请稍后...", false);
    }

    @Override
    protected void setBarLintColor() {
        StatusBarUtil.setTranslucentForImageView(this, 0, mContentView);
    }

    /**
     * 初始化界面显示、界面控件，注册监听事件
     */
    private void init() {
        txtError = findViewById(R.id.login_error);
        txtLoginName = findViewById(R.id.login_name);
        txtLoginPwd = findViewById(R.id.login_pwd);
        btnLogin = findViewById(R.id.login_btn);
        txtCode = findViewById(R.id.txt_code);
        imCode = findViewById(R.id.im_code);
        JPushInterface.cleanTags(this, 1);//移除推送监听
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HelperTool.isFastClick()) {
                    return;
                }

                // 使用权限请求框架
                AndPermission.with(LoginActivity.this)
                        .runtime()
                        .permission(Permission.READ_PHONE_STATE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                executeLogin(DeviceUtils.getUniqueDeviceId());
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                executeLogin(null);
                                //ToastUtils.showLong("读取设备信息被拒绝,应用可能使用异常!!");
                            }
                        })
                        .start();
            }
        });
        //验证码图片点击刷新
        imCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginKeyGetPersenter.dologinKeyGetPersenter();
            }
        });
    }

    public void executeLogin(String deviceID) {
        //LogUtils.eTag("deviceId---",deviceID+"8357");
        //校验登陆信息是否正确
        if (checkInputUserInfoIsRight()) {
            // showProgressDig(false);
            String queryName = txtLoginName.getText().toString();
            String queryPwd = txtLoginPwd.getText().toString();
            String queryCode = txtCode.getText().toString();
            LoginInfoBean n = new LoginInfoBean();
            n.setUserName(queryName);
            if (StringUtils.isEmpty(deviceID) || deviceID.equals("0000000")) {
                // 获取设备唯一id老方法在android 10版本已经失效
                // deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                // 使用构造方法获取设备唯一id并保存 但卸载重装后可能改变
                deviceID = SPUtils.getInstance().getString("DeviceId");
                if (StringUtils.isEmpty(deviceID)) {
                    deviceID = DeviceIdUtil.deviceIdShort;
                    SPUtils.getInstance().put("DeviceId", deviceID);
                }
            }
            n.setDeviceUUID(deviceID);
            n.setDevicePlatform("android");
            n.setUserPwd(queryPwd);
            n.setKey(key);
            n.setCode(queryCode);
            dialog.show();
            loginPersenter.dologin(n);
        }
    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String action) {
        super.callBackError(resultBean, action);
        dialog.dismiss();
    }

    /**
     * 校验用户输入登陆信息
     *
     * @return
     */
    private boolean checkInputUserInfoIsRight() {
        boolean result = false;
        try {
            String queryName = txtLoginName.getText().toString();
            if (HelperTool.stringIsEmptyOrNull(queryName) || queryName.length() < 6) {
                txtLoginName.setFocusable(true);
                throw new Exception("请输入正确的用户名！");
            }
            String queryPwd = txtLoginName.getText().toString();
            if (HelperTool.stringIsEmptyOrNull(queryPwd) || queryPwd.length() < 6) {
                txtLoginPwd.setFocusable(true);
                throw new Exception("请输入正确的密码！");
            }
            String queryCode = txtCode.getText().toString();
            if (HelperTool.stringIsEmptyOrNull(queryCode) && queryCode.length() != 4) {
                txtCode.setFocusable(true);
                throw new Exception("请输入验证码！");
            }
            txtError.setVisibility(View.GONE);
            result = true;

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            result = false;
            txtError.setText(e.getMessage());
            txtError.setVisibility(View.VISIBLE);
            vibrator.vibrate(50);
        }
        return result;
    }


    /**
     * 登陆成功时回调
     *
     * @param resultBean
     */
    @Override
    public void callBackLogin(ResultBean<LoginInfoBean, Map<String, String>> resultBean) {
        dialog.dismiss();
        try {
            if (resultBean.isSuccess() && resultBean.getData() != null) {
                txtError.setVisibility(View.INVISIBLE);
                //存储数据到本地
                datas.saveLoginInfoBean(this, resultBean);

                //LogUtils.eTag("token00712---",datas.getLoginInfoBean().getToken()+"~~~~");
                Intent intent = null;

                switch (GloblePlantformDatas.getInstance().getLoginInfoBean().getUserOrganizationType()) {
                    case "1":
                    case "2"://主管部门
                        SPUtils.getInstance().put("LoginType", 2);
                        intent = new Intent(this, CompetentMainActivity.class);
                        break;
                    case "3"://业主
                        SPUtils.getInstance().put("LoginType", 3);
                        intent = new Intent(this, EnterpriseMainActivity.class);
                        break;
                    case "4"://维保公司
                        SPUtils.getInstance().put("LoginType", 4);
                        intent = new Intent(this, EnterpriseMainActivity.class);
                        break;
                    default:
                        finish();
                }
                Set<String> queryset = new HashSet<String>();
                queryset.add(resultBean.getData().getUserName());
                JPushInterface.addTags(this, 1, queryset);
                if (isFromPush) {
                    intent = new Intent(this, AlarmMessageListActivity.class);
                    intent.putExtra("TypeId", typeId);
                    intent.putExtra("MessageType", relationId);
                }
                startActivity(intent);
                finish();
            } else {
                key = resultBean.getPars();
                loadCode();
                txtLoginPwd.setFocusable(true);
                txtError.setText(resultBean.getMessage());
                txtError.setVisibility(View.VISIBLE);

                vibrator.vibrate(50);
            }
        } catch (Exception e) {
            HelperView.Toast(this, "登陆回调异常：" + e.getMessage());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        String deviceId = tm.getDeviceId();
                        executeLogin(deviceId);
                    }
                } else {
                    Toast.makeText(this, "您未完成设备需要的授权", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    /**
     * 登陆令牌获取回调
     *
     * @param resultBean
     */
    @Override
    public void callBackLoginKeyGet(ResultBean<String, String> resultBean) {
        dialog.dismiss();
        try {
            if (resultBean.isSuccess()) {
                key = resultBean.getData();
                loadCode();
            } else {
                HelperView.Toast(this, resultBean.getMessage());
            }
        } catch (Exception e) {
            HelperView.Toast(this, "登陆令牌失败：" + e.getMessage());
        }
    }

    /**
     * 加载验证码图片
     */
    private void loadCode() {
        Picasso.get().load(URLConstant.URL_BASE + URLConstant.URL_LOGIN_CODE + "?key=" + key).into(imCode);
    }
}
