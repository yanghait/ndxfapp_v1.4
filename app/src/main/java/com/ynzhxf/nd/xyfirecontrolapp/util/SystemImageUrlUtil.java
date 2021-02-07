package com.ynzhxf.nd.xyfirecontrolapp.util;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectSystemBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * author hbzhou
 * date 2019/3/2 15:50
 */
public class SystemImageUrlUtil {

    public static void initData(String ID, final IGetImageUrlCallBack callBack) {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("userName", HelperTool.getUsername());
        params.put("proID", ID);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE + URLConstant.URL_PROJECT_SYS_LIST)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.onResult(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        LogUtils.showLoge("获取到的系统列表1212---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            List<ProjectSystemBean> systemList = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                    new TypeToken<List<ProjectSystemBean>>() {
                                    }.getType());

                            if (systemList == null || systemList.size() == 0) {
                                callBack.onResult(null);
                            } else {
                                //  处理返回系统列表图标路径 给统计分析使用
                                List<String> images = new ArrayList<>();

                                for (int i = 0; i < systemList.size(); i++) {
                                    if (systemList.get(i).getProjectSysType() != null) {
                                        images.add(URLConstant.URL_BASE1 + systemList.get(i).getProjectSysType().getImageUrl());
                                    } else {
                                        images.add(null);
                                    }
                                }
                                callBack.onResult(images);
                            }
                        } catch (Exception e) {
                            callBack.onResult(null);
                            e.printStackTrace();
                        }
                    }
                });
    }

    public interface IGetImageUrlCallBack {
        void onResult(List<String> imageUrls);
    }
}
