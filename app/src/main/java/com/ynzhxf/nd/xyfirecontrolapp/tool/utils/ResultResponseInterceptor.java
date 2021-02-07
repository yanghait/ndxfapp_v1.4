package com.ynzhxf.nd.xyfirecontrolapp.tool.utils;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ynzhxf.nd.xyfirecontrolapp.tool.ApplicationPlatform;
import com.ynzhxf.nd.xyfirecontrolapp.view.ActivityController;
import com.ynzhxf.nd.xyfirecontrolapp.view.LoginActivity;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;


/**
 * author hbzhou
 * date 2019/1/25 15:54
 */
public class ResultResponseInterceptor implements Interceptor {

    public static final String TAG = "OkHttpUtils";
    private String tag;

    public ResultResponseInterceptor(String tag) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        this.tag = tag;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logForRequest(request);
        Response response = chain.proceed(request);
        return logForResponse(response);
    }

    private Response logForResponse(Response response) {
        try {
            //===>response log
            Log.e(tag, "========response'log=======");
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            Log.e(tag, "url : " + clone.request().url());
            Log.e(tag, "code : " + clone.code());
            Log.e(tag, "protocol : " + clone.protocol());
            if (clone.code() == 401) {
                JPushInterface.cleanTags(ApplicationPlatform.getContext(), 1);
                ActivityController.finishAll();
                ToastUtils.showLong("登录超时或您的账号在其他设备上已登录!");
                Intent intent = new Intent(ApplicationPlatform.getContext(), LoginActivity.class);
                startActivity(intent);
                return null;
            }
            if (!TextUtils.isEmpty(clone.message()))
                Log.e(tag, "message : " + clone.message());

            //if (showResponse) {
            ResponseBody body = clone.body();
            if (body != null) {
                MediaType mediaType = body.contentType();
                if (mediaType != null) {
                    Log.e(tag, "responseBody's contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                        String resp = body.string();
                        //Log.e(tag, "responseBody's content : " + resp);
                        LogUtils.json(LogUtils.E, "responseBody's content : " + resp);

                        body = ResponseBody.create(mediaType, resp);
                        return response.newBuilder().body(body).build();
                    } else {
                        Log.e(tag, "responseBody's content : " + " maybe [file part] , too large too print , ignored!");
                    }
                } else {
                    Log.e(tag, "mediaType is null : ");
                }
            } else {
                Log.e(tag, "ResponseBody is null : ");
            }
            //}
            Log.e(tag, "========response'log=======end");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private void logForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();

            Log.e(tag, "========request'log=======");
            Log.e(tag, "method : " + request.method());
            Log.e(tag, "url : " + url);
            if (headers != null && headers.size() > 0) {
                Log.e(tag, "headers : " + headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    Log.e(tag, "requestBody's contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                        Log.e(tag, "requestBody's content : " + bodyToString(request));
                    } else {
                        Log.e(tag, "requestBody's content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
            Log.e(tag, "========request'log=======end");
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")
            )
                return true;
        }
        return false;
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}
