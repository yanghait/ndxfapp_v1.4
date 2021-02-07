package com.ynzhxf.nd.xyfirecontrolapp.tool.utils;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * author hbzhou
 * date 2019/1/25 10:54
 */
public abstract class ResponseResultCallBack extends Callback<ResultBean<String, String>> {
    @Override
    public ResultBean<String, String> parseNetworkResponse(Response response, int id) {
        String resultString = response.body().toString();
        ResultBean<String, String> resultBean1 = new Gson().fromJson(resultString, new TypeToken<ResultBean<String, String>>() {
        }.getType());
        return resultBean1;
//        int code = response.code();
//        ResultBean<String, String> resultBean = new ResultBean<>();
//        resultBean.setSuccess(false);
//        resultBean.setMessage("登录超时，请重新登录!");
//        if (code == 401) {
//            Intent intent = new Intent(ApplicationPlatform.getContext(), LoginActivity.class);
//            startActivity(intent);
//            return resultBean;
//        } else {
//            if (response.isSuccessful() && response.body() != null) {
//                ResultBean<String, String> resultBean1 = new Gson().fromJson(resultString, new TypeToken<ResultBean<String, String>>() {
//                }.getType());
//                return resultBean1 == null ? resultBean : resultBean1;
//            } else {
//                resultBean.setMessage("请求出错,请重新登录!");
//                //Intent intent = new Intent(ApplicationPlatform.getContext(), LoginActivity.class);
//                //startActivity(intent);
//                return resultBean;
//            }
//        }
    }
}
