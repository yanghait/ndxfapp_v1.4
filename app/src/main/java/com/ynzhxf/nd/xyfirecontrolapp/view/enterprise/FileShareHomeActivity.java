package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file.FileShareProgressUpdateActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file.FileShareSearchProjectActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file.FileShareWeFileActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file.FileTypeListActivity;

import java.util.List;

public class FileShareHomeActivity extends BaseActivity implements View.OnClickListener {

    private String id;

    private TextView projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home_file_share);
        super.onCreate(savedInstanceState);
        setBarTitle("文件分享");

        ImageButton download = findViewById(R.id.file_share_download_btn);

        LinearLayout select = findViewById(R.id.file_share_select_project);

        ImageButton btnSelect = findViewById(R.id.file_share_select_pro);

        View my_file = findViewById(R.id.file_share_my);

        View share_file = findViewById(R.id.file_share_all);

        View file_type = findViewById(R.id.file_share_type);

        projectName = findViewById(R.id.file_share_project_name);

        download.setOnClickListener(this);
        select.setOnClickListener(this);
        my_file.setOnClickListener(this);
        share_file.setOnClickListener(this);
        file_type.setOnClickListener(this);

        int loginType = SPUtils.getInstance().getInt("LoginType");

        if (loginType == 3 || loginType == 2) {
            select.setClickable(false);
            btnSelect.setVisibility(View.INVISIBLE);
        }

        id = getIntent().getStringExtra("id");

        projectName.setText(getIntent().getStringExtra("Name"));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.file_share_download_btn:

                AndPermission.with(FileShareHomeActivity.this)
                        .runtime()
                        .permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                Intent intent3 = new Intent(FileShareHomeActivity.this, FileShareProgressUpdateActivity.class);
                                startActivity(intent3);
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                ToastUtils.showLong("权限被拒绝,文件分享功能无法使用!");
                            }
                        })
                        .start();

                break;
            case R.id.file_share_select_project:
                Intent intent4 = new Intent(this, FileShareSearchProjectActivity.class);
                startActivityForResult(intent4, 66);

                break;
            case R.id.file_share_my:

                AndPermission.with(FileShareHomeActivity.this)
                        .runtime()
                        .permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                Intent intent = new Intent(FileShareHomeActivity.this, FileShareMyFileActivity.class);
                                intent.putExtra("id", id);
                                startActivity(intent);
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                ToastUtils.showLong("权限被拒绝,文件分享功能无法使用!");
                            }
                        })
                        .start();

                break;

            case R.id.file_share_all:

                AndPermission.with(FileShareHomeActivity.this)
                        .runtime()
                        .permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                Intent intent1 = new Intent(FileShareHomeActivity.this, FileShareWeFileActivity.class);
                                intent1.putExtra("id", id);
                                startActivity(intent1);
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                ToastUtils.showLong("权限被拒绝,文件分享功能无法使用!");
                            }
                        })
                        .start();
                break;

            case R.id.file_share_type:
                Intent intent2 = new Intent(this, FileTypeListActivity.class);
                intent2.putExtra("id", id);
                startActivity(intent2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 66:
                    projectName.setText(data.getStringExtra("Name"));
                    id = data.getStringExtra("ID");
                    break;
            }
        }
    }
}
