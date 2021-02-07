package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.StringUtils;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsReaderView;
import com.tencent.smtt.sdk.ValueCallback;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.HashMap;

public class FileShareShowDetailActivity extends AppCompatActivity implements IBaseView {


    private TbsReaderView tbsReaderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_file_details);

        ImageButton back = findViewById(R.id.tool_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayout showFileDetails = findViewById(R.id.file_details_view);

        //实例化TbsReaderView，然后将它装入我们准备的容器
        tbsReaderView = new TbsReaderView(this, readerCallback);

        showFileDetails.addView(tbsReaderView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        String path = getIntent().getStringExtra("path");
        if (StringUtils.isEmpty(path)) {
            return;
        }
        openFile(path);
    }

    //下面的回调必须要实现，暂时没找到此回调的用处
    TbsReaderView.ReaderCallback readerCallback = new TbsReaderView.ReaderCallback() {
        @Override
        public void onCallBackAction(Integer integer, Object o, Object o1) {

        }
    };

    private void openFile(String path) {
        Bundle bundle = new Bundle();
        //文件路径
        bundle.putString("filePath", path);
        //临时文件的路径，必须设置，否则会报错
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getAbsolutePath());
        //准备
        boolean result = tbsReaderView.preOpen(getFileType(path), false);

        HashMap<String,String> params = new HashMap<>();
        params.put("local","false");

        QbSdk.openFileReader(this, path, params, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {

            }
        });
//        if (result) {
//            //预览文件
//           tbsReaderView.openFile(bundle);
//        } else {
//            HelperView.Toast(this, "不支持此类型文件格式预览!");
//        }
    }

    private String getFileType(String path) {
        if (!StringUtils.isEmpty(path)) {
            return path.substring(path.lastIndexOf(".") + 1);
        }
        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tbsReaderView != null) {
            tbsReaderView.onStop();
        }
    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String code) {

    }
}
