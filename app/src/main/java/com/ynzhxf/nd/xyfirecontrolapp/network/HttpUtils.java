package com.ynzhxf.nd.xyfirecontrolapp.network;


import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.tool.ApplicationPlatform;
import com.ynzhxf.nd.xyfirecontrolapp.tool.utils.ResultResponseInterceptor;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtils {

    private static final int DEFAULT_TIMEOUT = 20; //连接 超时的时间，单位：秒
    private static final OkHttpClient client = new OkHttpClient.Builder().
            connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).
            readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).
            writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).build();
    private static HttpUtils httpUtils;
    private static Retrofit retrofit;
    private static RetrofitInterface retrofitInterface;

    public synchronized static RetrofitInterface getRetrofit() {
        //初始化retrofit的配置
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URLConstant.URL_BASE)
                    .client(initOkHttp())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            retrofitInterface = retrofit.create(RetrofitInterface.class);
        }
        return retrofitInterface;
    }

    private static OkHttpClient initOkHttp() {
        InputStream[] inputStream = null;
        try {
            inputStream = new InputStream[]{ApplicationPlatform.getContext().getAssets().open("ynzhxf.pem")};
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(inputStream, null, null);
        return new OkHttpClient.Builder()
                .addInterceptor(new ResultResponseInterceptor("OkHttpClient-OLD-TAG"))
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                //其他配置
                .build();
    }
}
