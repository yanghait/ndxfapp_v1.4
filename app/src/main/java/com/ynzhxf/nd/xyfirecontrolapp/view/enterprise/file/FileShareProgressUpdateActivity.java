package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.share.FileShareMyFileAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.share.FileUpdateProgressAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileUpdateProgressBean;
import com.ynzhxf.nd.xyfirecontrolapp.util.FileOutUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.FileShareMyFileActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 文件下载列表页面 多任务下载 断点续传
 */
public class FileShareProgressUpdateActivity extends BaseActivity {

    RecyclerView mRecyclerView;

    private FileUpdateProgressAdapter adapter;

    List<FileUpdateProgressBean> beanList = new ArrayList<>();

    List<FileUpdateProgressBean> beanListForAdapter = new ArrayList<>();

    public static List<BaseDownloadTask> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_file_update_progress);
        super.onCreate(savedInstanceState);

        setBarTitle("下载列表");

        mRecyclerView = findViewById(R.id.file_update_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        DividerItemDecoration div = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        div.setDrawable(getResources().getDrawable(R.drawable.divider_shape_for_file));
//        mRecyclerView.addItemDecoration(div);

        FileDownloader.setup(this);

        initData();

        initListener();
    }

    public void openFileForSdk(String newPath) {
        HashMap<String, String> params = new HashMap<>();

        params.put("local", "false");

        QbSdk.openFileReader(FileShareProgressUpdateActivity.this, newPath, params, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {

            }
        });
    }

    public void openFileForThirdApp(String newPath, String name) {
        Uri uri;
        // 兼容7.0及以上版本获取URI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(FileShareProgressUpdateActivity.this, "com.ynzhxf.nd.firecontrolapp.fileprovider", new File(newPath));
        } else {
            uri = Uri.fromFile(new File(newPath));
        }
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, FileShareMyFileActivity.getMIMEType(name));//getMIMEType(myFileBeanList.get(position).getF_FileName())
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(FileShareProgressUpdateActivity.this, "sorry附件不能打开，请下载相关软件！", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {

        beanList = new Gson().fromJson(CacheDiskUtils.getInstance().getString("task"), new TypeToken<List<FileUpdateProgressBean>>() {
        }.getType());

        if (beanList != null && beanList.size() > 0) {
            beanListForAdapter.addAll(beanList);
        }
        if (tasks.size() > 0) {
            for (int i = 0; i < tasks.size(); i++) {

                BaseDownloadTask task = tasks.get(i);
                double progress = 1.00 * task.getSoFarBytes() / task.getTotalBytes() * 100.00;
                int okPro = (int) Math.round(progress);
                FileUpdateProgressBean bean = new FileUpdateProgressBean();
                bean.setFileLength(task.getTotalBytes() / 1024);
                bean.setFileName(task.getFilename());
                bean.setProgress(okPro);
                bean.setStatus(task.getStatus());
                bean.setUrl(task.getUrl());
                bean.setTask(task);

                beanListForAdapter.add(0, bean);
            }
        }

        LogUtils.showLoge("输出下载列表显示1213---", String.valueOf(beanListForAdapter.size() + "~~~~~~~~~~~~~~~~~"));

        adapter = new FileUpdateProgressAdapter(this, beanListForAdapter);

        if (beanListForAdapter.size() == 0) {
            showNoDataView();
            //HelperView.Toast(this, "暂无下载记录!");
        } else {
            hideNoDataView();
        }

        adapter.setOnItemClickListener(new FileShareMyFileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, final int position) {
                final AlertDialog dialog = new AlertDialog.Builder(FileShareProgressUpdateActivity.this).create();

                final String newPath = SPUtils.getInstance().getString(beanListForAdapter.get(position).getUrl());

                final boolean exists = FileShareMyFileAdapter.whetherExistFile(FileDownloadUtils.getDefaultSaveRootPath(), newPath.substring(newPath.lastIndexOf("/") + 1));

                FileUtils.beginDialogForDownload(dialog, new FileUtils.OnClickSelecedListener() {
                    @Override
                    public void onCancel() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onTake() {
                        dialog.dismiss();

                        if (!exists) {
                            HelperView.Toast(FileShareProgressUpdateActivity.this, "文件不存在!");
                            return;
                        }

//                        if (Build.VERSION.SDK_INT >= 28) {
//                            openFileForThirdApp(newPath, newPath.substring(newPath.lastIndexOf("/") + 1));
//                        } else {
//                            openFileForSdk(newPath);
//                        }

                        FileOutUtil.openFileReader(FileShareProgressUpdateActivity.this,newPath);
                    }

                    @Override
                    public void onNative() {
                        dialog.dismiss();

                        if (StringUtils.isEmpty(CacheDiskUtils.getInstance().getString("task", ""))) {
                            tasks.remove(position);
                            beanListForAdapter.remove(position);
                            adapter.notifyDataSetChanged();
                        } else {

                            try {
                                JSONArray jsonArray = new JSONArray(CacheDiskUtils.getInstance().getString("task", ""));

                                final String path = SPUtils.getInstance().getString(beanListForAdapter.get(position).getUrl());

                                File file = new File(path);
                                if (file.exists()) {
                                    if (file.delete()) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            if (beanListForAdapter.get(position).getUrl().equals(jsonArray.getJSONObject(i).getString("url"))) {
                                                jsonArray.remove(i);
                                                beanListForAdapter.remove(position);
                                                adapter.notifyDataSetChanged();
                                                break;
                                            }
                                        }
                                        CacheDiskUtils.getInstance().put("task", jsonArray.toString());
                                    } else {
                                        HelperView.Toast(FileShareProgressUpdateActivity.this, "删除任务失败,请稍后再试!");
                                    }
                                } else {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        if (beanListForAdapter.get(position).getUrl().equals(jsonArray.getJSONObject(i).getString("url"))) {
                                            jsonArray.remove(i);
                                            beanListForAdapter.remove(position);
                                            adapter.notifyDataSetChanged();
                                            LogUtils.showLoge("数量1313---", String.valueOf(beanListForAdapter.size()));
                                            break;
                                        }
                                        if (i == jsonArray.length() - 1) {
                                            for (int j = 0; j < tasks.size(); j++) {
                                                if (beanListForAdapter.get(position).getUrl().equals(tasks.get(j).getUrl())) {
                                                    tasks.remove(j);
                                                    break;
                                                }
                                            }
                                            LogUtils.showLoge("数量1414---", String.valueOf(beanListForAdapter.size()));
                                            beanListForAdapter.remove(position);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                    if (jsonArray.length() == 0 && beanListForAdapter.size() > 0) {
                                        for (int j = 0; j < tasks.size(); j++) {
                                            if (beanListForAdapter.get(position).getUrl().equals(tasks.get(j).getUrl())) {
                                                tasks.remove(j);
                                                break;
                                            }
                                        }
                                        LogUtils.showLoge("数量1515---", String.valueOf(beanListForAdapter.size()));

                                        beanListForAdapter.remove(position);

                                        adapter.notifyDataSetChanged();
                                    } /*else {
                                        LogUtils.showLoge("数量1616---", String.valueOf(beanListForAdapter.size()));
                                        tasks.clear();
                                        beanListForAdapter.clear();
                                        adapter.notifyDataSetChanged();
                                    }*/
                                    LogUtils.showLoge("数量1717---", String.valueOf(jsonArray.toString()));
                                    CacheDiskUtils.getInstance().put("task", jsonArray.toString());
                                }

                            } catch (JSONException e) {
                                LogUtils.showLoge("数量1919---", String.valueOf(e.getMessage()));
                                e.printStackTrace();
                            }
                        }

                        if (beanListForAdapter.size() == 0) {
                            showNoDataView();
                        }
                    }

                    @Override
                    public void onEditor() {

                    }
                });
                return true;
            }
        });

        adapter.setHolderClickListener(new FileUpdateProgressAdapter.OnViewHolderClickListener() {
            @Override
            public void OnInitData(int position) {

            }

            @Override
            public void OnClickPaused(final int position) {

                if (adapter.getPausedMap().get(position)) {

                    adapter.getPausedMap().put(position, false);

                    BaseDownloadTask task = null;
                    for (int i = 0; i < tasks.size(); i++) {
                        if (beanListForAdapter.get(position).getUrl().equals(tasks.get(i).getUrl())) {
                            task = tasks.get(i);
                        }
                    }
                    if (task != null) {
                        try {
                            task.reuse();
                            task.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                            adapter.getListHolder().get(position).selectState.setImageDrawable(getResources().getDrawable(R.mipmap.file_update_pause));
                        }
                        adapter.getListHolder().get(position).selectState.setImageDrawable(getResources().getDrawable(R.mipmap.file_downing));
                    }
                } else {

                    adapter.getPausedMap().put(position, true);
                    BaseDownloadTask task = null;
                    for (int i = 0; i < tasks.size(); i++) {
                        if (beanListForAdapter.get(position).getUrl().equals(tasks.get(i).getUrl())) {
                            task = tasks.get(i);
                        }
                    }
                    if (task != null) {
                        boolean isPaused = task.pause();
                        if (isPaused) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.getListHolder().get(position).selectState.setImageDrawable(getResources().getDrawable(R.mipmap.file_update_pause));
                                }
                            }, 500);
                        }
                    }
                }
            }
        });

        mRecyclerView.setAdapter(adapter);
    }

    private void initListener() {
        FileUtils.getInstant().setUpdateProgressListener(new FileUtils.OnProgressUpdateListener() {
            @Override
            public void OnStart(BaseDownloadTask task) {

            }

            @Override
            public void OnProgress(BaseDownloadTask task, int so, int total) {

                double progress = 1.00 * so / total * 100.00;
                int okPro = (int) Math.round(progress);
                FileUpdateProgressBean bean = new FileUpdateProgressBean();
                bean.setFileLength(total / 1024);
                bean.setFileName(task.getFilename());
                bean.setProgress(okPro);
                bean.setStatus(task.getStatus());
                bean.setUrl(task.getUrl());
                bean.setTask(task);

                for (int i = 0; i < beanListForAdapter.size(); i++) {
                    if (bean.getUrl().equals(beanListForAdapter.get(i).getUrl()) && i <= adapter.getListHolder().size() - 1) {
                        adapter.getListHolder().get(i).progressBar.setProgress(bean.getProgress());
                        adapter.getListHolder().get(i).down_ok.setVisibility(View.GONE);
                        adapter.getListHolder().get(i).selectState.setVisibility(View.VISIBLE);
                        adapter.getListHolder().get(i).selectState.setImageDrawable(getResources().getDrawable(R.mipmap.file_downing));

                        adapter.getListHolder().get(i).fileLength.setText(String.valueOf(total / 1024 + "KB"));

                        adapter.getPausedMap().put(i, false);

                        break;
                    }
                }
            }

            @Override
            public void Completed(BaseDownloadTask task) {
                for (int i = 0; i < beanListForAdapter.size(); i++) {
                    if (task.getUrl().equals(beanListForAdapter.get(i).getUrl())) {
                        if (i >= adapter.getListHolder().size()) {
                            return;
                        }
                        adapter.getListHolder().get(i).progressBar.setProgress(100);
                        adapter.getListHolder().get(i).down_ok.setVisibility(View.VISIBLE);
                        adapter.getListHolder().get(i).selectState.setVisibility(View.GONE);

                        break;
                    }
                }
            }

            @Override
            public void OnPaused(BaseDownloadTask task) {
                for (int i = 0; i < beanListForAdapter.size(); i++) {
                    if (task.getUrl().equals(beanListForAdapter.get(i).getUrl())) {

                        double progress = 1.00 * task.getSoFarBytes() / task.getTotalBytes() * 100.00;
                        int okPro = (int) Math.round(progress);
                        adapter.getListHolder().get(i).progressBar.setProgress(okPro);
                        adapter.getListHolder().get(i).down_ok.setVisibility(View.GONE);

                        adapter.getListHolder().get(i).selectState.setImageDrawable(getResources().getDrawable(R.mipmap.file_update_pause));

                        adapter.getListHolder().get(i).selectState.setVisibility(View.VISIBLE);

                        adapter.getPausedMap().put(i, true);
                        break;
                    }
                }
            }

            @Override
            public void OnError(BaseDownloadTask task) {
                for (int i = 0; i < beanListForAdapter.size(); i++) {
                    if (task.getUrl().equals(beanListForAdapter.get(i).getUrl())) {

                        adapter.getListHolder().get(i).progressBar.setProgress(beanListForAdapter.get(i).getProgress());
                        adapter.getListHolder().get(i).down_ok.setVisibility(View.GONE);

                        adapter.getListHolder().get(i).selectState.setImageDrawable(getResources().getDrawable(R.mipmap.file_update_pause));

                        adapter.getListHolder().get(i).selectState.setVisibility(View.VISIBLE);

                        adapter.getPausedMap().put(i, true);

                        break;
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).isRunning()) {
                tasks.get(i).pause();
            }
        }
    }
}
