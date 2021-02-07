package com.ynzhxf.nd.xyfirecontrolapp.network;



import androidx.annotation.NonNull;

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

/**
 * author hbzhou
 * date 2019/6/19 10:12
 */
public class RetrofitUtils {
    private static RetrofitInterface mApiUrl;

    /**
     * 单例模式
     */
    public static RetrofitInterface getInstance() {
        if (mApiUrl == null) {
            synchronized (RetrofitUtils.class) {
                if (mApiUrl == null) {
                    mApiUrl = new RetrofitUtils().getRetrofit();
                }
            }
        }
        return mApiUrl;
    }

    private RetrofitUtils() {
    }

    public RetrofitInterface getRetrofit() {
        return initRetrofit(initOkHttp()).create(RetrofitInterface.class);
    }

    /**
     * 初始化Retrofit
     */
    @NonNull
    private Retrofit initRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(URLConstant.URL_BASE1)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * 初始化okhttp
     */
    @NonNull
    private OkHttpClient initOkHttp() {
        InputStream[] inputStream = null;
        try {
            inputStream = new InputStream[]{ApplicationPlatform.getContext().getAssets().open("ynzhxf.pem")};
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(inputStream, null, null);
        return new OkHttpClient.Builder()
                .addInterceptor(new ResultResponseInterceptor("OkHttpClient-TAG"))
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                //其他配置
                .build();
    }
}
