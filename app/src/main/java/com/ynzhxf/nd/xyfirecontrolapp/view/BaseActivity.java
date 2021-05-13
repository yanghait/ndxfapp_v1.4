package com.ynzhxf.nd.xyfirecontrolapp.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jaeger.library.StatusBarUtil;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;
import com.ynzhxf.nd.xyfirecontrolapp.util.PermissionsUtil;
import com.youngfeng.snake.annotations.EnableDragToClose;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 基础Activity
 * Created by nd on 2018-07-10.
 */
@EnableDragToClose
public class BaseActivity extends AppCompatActivity implements IBaseView {


    /**
     * log记录TAG标记
     */
    protected final String TAG = this.getClass().getSimpleName();

    /**
     * 全局加载等待弹出框
     */
    protected ProgressDialog progressDialog;

    /**
     * 导航栏
     */
    protected Toolbar toolbar;

    /***
     * 导航栏标题
     */
    private String BarTitle;

    private TextView txtBaseBarTitle;

    /**
     * 防止P层持有View引用
     */
    private List<IBasePersenter> listPersenter;

    protected List<Disposable> addDisposable = new ArrayList<>();

    boolean isPORTRAIT = true;

    public void setPORTRAIT(boolean PORTRAIT) {
        isPORTRAIT = PORTRAIT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        createToolBar();
        listPersenter = new ArrayList<>();
        ActivityController.addActivity(this);
        setBarLintColor();
    }

    /**
     * 沉浸式状态栏
     */
    protected void setBarLintColor() {
        // 默认状态栏使用ToolBar的颜色
        StatusBarUtil.setColor(this, getResources().getColor(R.color.tool_bar), 0);
    }

    /**
     * 弹出全局等待进度框
     *
     * @param title      提示标题
     * @param message    提示消息
     * @param cancelable 是否可以按返回键关闭
     */
    protected void showProgressDig(String title, String message, boolean cancelable) {
        progressDialog.setCancelable(cancelable);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    /**
     * 创建标题栏
     */
    public void createToolBar() {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("");
            txtBaseBarTitle = toolbar.findViewById(R.id.toolbar_title);
            txtBaseBarTitle.setText(BarTitle);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    /**
     * 创建顶部标题栏
     *
     * @param toolbar
     */
    public void createToolBar(Toolbar toolbar) {
        if (toolbar != null) {
            toolbar.setTitle("");
            txtBaseBarTitle = toolbar.findViewById(R.id.toolbar_title);
            txtBaseBarTitle.setText(BarTitle);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    /**
     * 设置导航栏的标题
     *
     * @param barTitle
     */
    protected void setBarTitle(String barTitle) {
        this.BarTitle = barTitle;
        if (txtBaseBarTitle != null) {
            txtBaseBarTitle.setText(BarTitle);
        }
    }

    /**
     * 全局加载等待进度框
     *
     * @param cancelable 是否可以按返回键关闭
     */
    protected void showProgressDig(boolean cancelable) {
        progressDialog.setCancelable(cancelable);
        progressDialog.setTitle("操作提示");
        progressDialog.setMessage("正在加载中，请稍等......");
        progressDialog.show();
    }

    protected void showProgressDig(boolean cancelable, String msg) {
        progressDialog.setCancelable(cancelable);
        progressDialog.setTitle("操作提示");
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    protected ProgressDialog showProgress(Context context, String title, String message, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(cancelable);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    protected ProgressDialog showProgress(Context context, String msg, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(cancelable);
        progressDialog.setTitle("提示");
        progressDialog.setMessage(msg);
        progressDialog.show();
        return progressDialog;
    }

    protected void showNoDataView() {
        LinearLayout mNoDataView = findViewById(R.id.all_no_data);
        if (mNoDataView != null) {
            mNoDataView.setVisibility(View.VISIBLE);
        }
    }

    protected void hideNoDataView() {
        LinearLayout mNoDataView = findViewById(R.id.all_no_data);
        if (mNoDataView != null) {
            mNoDataView.setVisibility(View.GONE);
        }
    }


    /**
     * 关闭全局等待进度框
     */
    protected void hideProgressDig() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放引用，防止内存溢出
        for (IBasePersenter query : listPersenter) {
            query.detachView();
        }
        listPersenter.clear();
        ActivityController.removeActivity(this);
        progressDialog.cancel();
        for (Disposable d : addDisposable) {
            if (d != null && !d.isDisposed()) {
                d.dispose();
            }
        }
    }

    @Override
    protected void onResume() {
        /**
         * 强制竖屏
         */
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O && isPORTRAIT) {
            //如果是8.0版本不强制设置屏幕方向
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
        }
        super.onResume();
    }

    /**
     * 将P层引用添加到列表引用列表
     *
     * @param persenter
     */
    protected void addPersenter(IBasePersenter persenter) {
        this.listPersenter.add(persenter);
    }

    /**
     * 请求异常回调
     *
     * @param resultBean
     * @param action     请求标识
     */
    @Override
    public void callBackError(ResultBean<String, String> resultBean, String action) {
        try {
            hideProgressDig();
            if (resultBean.getCode() == 401) {//服务器未授权
                ActivityController.finishAll();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                HelperView.Toast(this, "登录超时或您的账号在其他设备上已登录!");
                return;
            }
            HelperView.Toast(this, resultBean.getMessage());//resultBean.getMessage()
            Log.e(TAG, action + " " + resultBean.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "异常处理错误：" + e.getMessage());
        }

    }

    protected void initCallBackError(Context context, int code) {
        if (code == 401) {
            ActivityController.finishAll();
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
        }
    }

    public static PermissionsUtil.IGrantCallBack callBack;

    public static void requestPermissionsCallBack(Activity activity, String title, int requestCode, String[] perm, PermissionsUtil.IGrantCallBack callBack1) {
        callBack = callBack1;
        EasyPermissions.requestPermissions(activity, title, requestCode, perm);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        if (callBack != null) {
            boolean isOK = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    isOK = false;
                    break;
                }
            }
            callBack.result(isOK, requestCode);
        }
    }
}
