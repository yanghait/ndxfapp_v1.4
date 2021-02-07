package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.utils;

import android.app.Activity;

import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class FileOptionUtil {
    public static void fileTypeDelete(final Activity activity, String ID, final IFileTypeDeleteCallBack callBack) {

        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("ID", ID);

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_FILE_TYPE_DELETE))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.showLoge("文件分类文件删除结果1111---", e.getMessage());
                        HelperView.Toast(activity, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("文件分类文件删除结果0909---", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean isOk = json.getBoolean("success");
                            callBack.onResult(isOk, json.getString("message"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(activity, e.getMessage());
                        }
                    }
                });
    }

    public interface IFileTypeDeleteCallBack {
        void onResult(boolean result, String content);
    }
}
