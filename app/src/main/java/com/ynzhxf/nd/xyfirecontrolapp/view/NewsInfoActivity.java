package com.ynzhxf.nd.xyfirecontrolapp.view;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.StringUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.NewsBean;

public class NewsInfoActivity extends BaseActivity {

    private NewsBean newsBean;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_news_info);
        super.onCreate(savedInstanceState);
        setBarTitle("通知详情");
        String url = getIntent().getStringExtra("data");
        if (url == null) {
            HelperView.Toast(this, "未找到新闻对象！");
            return;
        }
        webView = findViewById(R.id.wb_info);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//支持javascript
        settings.setUseWideViewPort(true);//适配屏幕
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(false);//支持放大缩小
        settings.setDisplayZoomControls(false);//隐藏放大缩小的按钮
        settings.setDomStorageEnabled(true);//支持Html5标签

        // 修复部分跳转链接提示选择默认浏览器的问题
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        String title = getIntent().getStringExtra("title");

        if (!StringUtils.isEmpty(title)) {
            setBarTitle(title);
        }

        webView.loadUrl(url);
    }
}
