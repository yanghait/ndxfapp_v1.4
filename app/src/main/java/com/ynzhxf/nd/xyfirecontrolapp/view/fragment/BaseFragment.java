package com.ynzhxf.nd.xyfirecontrolapp.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;
import com.ynzhxf.nd.xyfirecontrolapp.view.ActivityController;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * 碎片基础节点
 * Created by nd on 2018-07-16.
 */

public class BaseFragment extends Fragment implements IBaseView {
    /**
     * log记录TAG标记
     */
    protected final String TAG = this.getClass().getSimpleName();

    /**
     * 全局加载等待弹出框
     */
    protected ProgressDialog progressDialog;

    /**
     * 容器对象
     */

    protected Context context;

    /**
     * 防止P层持有View引用
     */
    private List<IBasePersenter> listPersenter;

    protected List<Disposable> addDisposable = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(context);
        return super.onCreateView(inflater, container, savedInstanceState);
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

    /**
     * 解决有时会不显示dialog
     * @param context
     * @param title
     * @param message
     * @param cancelable
     * @return
     */
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        listPersenter = new ArrayList<>();
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
    public void onDestroy() {
        super.onDestroy();
        // 遍历list 释放请求引用 防止内存泄漏
        for (Disposable d : addDisposable) {
            if (d != null && !d.isDisposed()) {
                d.dispose();
            }
        }
        //释放引用，防止内存溢出
        for (IBasePersenter query : listPersenter) {
            query.detachView();
        }
        listPersenter.clear();
    }

    /**
     * 将P层引用添加到列表引用列表
     *
     * @param persenter
     */
    protected void addPersenter(IBasePersenter persenter) {
        this.listPersenter.add(persenter);
    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String code) {
        try {
            hideProgressDig();
            if (resultBean.getCode() == 401) {//服务器未授权
                ActivityController.finishAll();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
            HelperView.Toast(context, resultBean.getMessage());
            Log.e(TAG, code + " " + resultBean.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "异常处理错误：" + e.getMessage());
        }
    }
}
